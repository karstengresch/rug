package com.atomist.rug.runtime.js.interop

import java.lang.reflect.{Method, Modifier}

import com.atomist.graph.GraphNode
import com.atomist.rug.runtime.js.JavaScriptObject
import com.atomist.rug.runtime.js.interop.jsScalaHidingProxy.MethodValidator
import com.atomist.rug.runtime.js.nashorn.NashornJavaScriptObject
import jdk.nashorn.api.scripting.{AbstractJSObject, ScriptObjectMirror}
import jdk.nashorn.internal.runtime.ScriptRuntime
import org.springframework.util.ReflectionUtils

import scala.collection.JavaConverters._
import scala.util.control.NonFatal

/**
  * Use this if you want to expose a structure in JavaScript
  * with TypeScript-friendly arrays and null in place of Option.
  * Simply uses reflection.
  * Also, enables suppression of some methods.
  * Exposes no-arg methods as properties but allows invocation with parameters.
  * If you wish to expose a no-arg method as a function (because it has
  * side effects etc) use
  *
  * @see ExposeAsFunction
  */
class jsScalaHidingProxy private(
                                  val target: Any,
                                  methodsToHide: Set[String],
                                  methodValidator: MethodValidator,
                                  returnNotToProxy: Any => Boolean
                                ) extends AbstractJSProxy {

  override def getMember(name: String): AnyRef = {
    val r = name match {
      case _ if methodsToHide.contains(name) =>
        // Make JavaScript folks feel at home.
        ScriptRuntime.UNDEFINED
      case _ if jsSafeCommittingProxy.MagicJavaScriptMethods.contains(name) =>
        super.getMember(name)
      case _ =>
        try {
          ReflectionUtils.getAllDeclaredMethods(target.getClass).find(_.getName == name) match {
            case Some(m) if methodValidator(m) =>
              if (m.getParameterCount == 0 && !m.isAnnotationPresent(classOf[ExposeAsFunction]))
                m.invoke(target) match {
                  case s: ScriptObjectBackedTreeNode => jsScalaHidingProxy(s)
                  case n: GraphNode => n
                  case o if !o.isInstanceOf[String] &&
                    !o.isInstanceOf[JavaScriptObject] &&
                    !o.isInstanceOf[ScriptObjectMirror] &&
                    !o.isInstanceOf[NashornJavaScriptArray[_]]=> jsScalaHidingProxy(o)
                  case y: AnyRef => y
                }
              else {
                new FunctionProxyToReflectiveInvocation(m)
              }
            case _ =>
              ScriptRuntime.UNDEFINED
          }
        }
        catch {
          case _: NoSuchMethodException =>
            ScriptRuntime.UNDEFINED
        }
    }

    (r match {
      case seq: Seq[_] => new NashornJavaScriptArray(
        seq.map(new jsScalaHidingProxy(_, methodsToHide, methodValidator, returnNotToProxy))
          .asJava)
      case opt: Option[AnyRef]@unchecked => opt.orNull
      case x => x
    }) match {
      case null => null
      case ScriptRuntime.UNDEFINED => ScriptRuntime.UNDEFINED
      case x if returnNotToProxy(x) => x
      case arr: NashornJavaScriptArray[_] => arr
      case s: String => s
      case i: Integer => i
      case fun: FunctionProxyToReflectiveInvocation => fun
      case js: ScriptObjectMirror => js
      case njo: NashornJavaScriptObject => njo.som
      case x => jsScalaHidingProxy(x)
    }
  }

  private class FunctionProxyToReflectiveInvocation(m: Method)
    extends AbstractJSObject {

    override def isFunction: Boolean = true

    override def call(thiz: scala.Any, args: AnyRef*): AnyRef = {
      try {
        val fixed = args.collect {
          case o: ScriptObjectMirror => new NashornJavaScriptObject(o)
          case x => x
        }
        m.invoke(target, fixed: _*) match {
          case s: ScriptObjectBackedTreeNode => jsScalaHidingProxy(s)
          case n: GraphNode => n
          case o
            if !o.isInstanceOf[String] &&
              !o.isInstanceOf[JavaScriptObject] &&
              !o.isInstanceOf[ScriptObjectMirror] &&
              !o.isInstanceOf[NashornJavaScriptArray[_]]  => jsScalaHidingProxy(o)
          case o: NashornJavaScriptObject => o.som
          case y: AnyRef => y
        }
      }
      catch {
        case iex: IllegalArgumentException =>
          throw new IllegalArgumentException(s"Illegal ${args.size} arguments for ${target.getClass}.${m.getName}: [$args]", iex)
        case t: Throwable =>
          t.printStackTrace()//TODO remove
          throw t
        case NonFatal(t) => throw t
      }
    }
  }

  override def equals(that: Any): Boolean = that match {
    case jsp: jsScalaHidingProxy =>
      this.target == jsp.target
    case _ => false
  }

  override def toString: String =
    s"${getClass.getSimpleName} wrapping [$target]"
}

object jsScalaHidingProxy {

  type MethodValidator = Method => Boolean

  def apply(target: Any,
            methodsToHide: Set[String] = DefaultMethodsToHide,
            methodValidator: MethodValidator = publicMethodsNotOnObject,
            returnNotToProxy: Any => Boolean = _ => false): jsScalaHidingProxy = {
    val r = target match {
      case null | None => null
      case shp: jsScalaHidingProxy =>
        // Don't double proxy
        shp
      case x =>
        new jsScalaHidingProxy(x, methodsToHide, methodValidator, returnNotToProxy)
    }
    r
  }

  val DefaultMethodsToHide: Set[String] = Set("getClass")

  def publicMethodsNotOnObject(m: Method): Boolean =
    Modifier.isPublic(m.getModifiers) && m.getDeclaringClass != classOf[Object]

}
