package com.atomist.rug.runtime

import com.atomist.project.ProjectOperationArguments
import com.atomist.project.archive.DefaultAtomistConfig
import com.atomist.project.common.InvalidParametersException
import com.atomist.project.generate.ProjectGenerator
import com.atomist.rug.kind.core.ProjectMutableView
import com.atomist.source.{ArtifactSource, EmptyArtifactSource}
import jdk.nashorn.api.scripting.ScriptObjectMirror

/**
  * ProjectEditor implementation that invokes a JavaScript function. This will probably be the result of
  * TypeScript compilation, but need not be. Attempts to source metadata from annotations.
  */
class JavaScriptInvokingProjectGenerator(
                                          jsc: JavaScriptContext,
                                          className: String,
                                          jsVar: ScriptObjectMirror,
                                          rugAs: ArtifactSource
                                        )
  extends JavaScriptInvokingProjectOperation(jsc, className, jsVar, rugAs)
    with ProjectGenerator {

  override val name: String =
    if (className.endsWith("Generator")) className.dropRight("Generator".length)
    else className

  @throws(classOf[InvalidParametersException])
  override def generate(poa: ProjectOperationArguments): ArtifactSource = {
    validateParameters(poa)
    //val tr = time {
    val newEmptyAs = EmptyArtifactSource(s"${getClass.getSimpleName}-new")
    val pmv = new ProjectMutableView(rugAs, newEmptyAs, atomistConfig = DefaultAtomistConfig, context)

    val result = invokeMember("populate", pmv, poa)

    //    logger.debug(s"$name modifyInternal took ${tr._2}ms")
    //    tr._1

    pmv.currentBackingObject
  }

}
