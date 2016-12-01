package com.atomist.tree.pathexpression

import com.atomist.tree.TreeNode
import com.atomist.tree.pathexpression.ExecutionResult._
import com.atomist.tree.pathexpression.{ExecutionResult => _}

object PathExpressionEngine {

  val DotSeparator = "."

  val SlashSeparator = "/"

  val AmongSeparator = "$"

  val SlashSlash = "//"

  val PredicateOpen = "["

  val PredicateClose = "]"

  val PathProperty = "[a-zA-Z][\\w]*".r
}

/**
  * Expression engine implementation for our path format
  */
class PathExpressionEngine extends ExpressionEngine {

  import ExpressionEngine.NodePreparer

  override def evaluate(node: TreeNode, expression: String, nodePreparer: Option[NodePreparer] = None): ExecutionResult = {
    val parsed = PathExpressionParser.parsePathExpression(expression)
    evaluateParsed(node, parsed, nodePreparer)
  }

  override def evaluateParsed(node: TreeNode, parsed: PathExpression, nodePreparer: Option[NodePreparer]): ExecutionResult = {
    var r: ExecutionResult = ExecutionResult(List(node))
    for (e <- parsed.elements) {
      r = r match {
        case Right(n :: Nil) =>
          val next = e.follow(n, nodePreparer.getOrElse(n => n))
          next
        case Right(Nil) =>
          Right(Nil)
        case Right(seq) =>
          val kids: List[TreeNode] = seq
            .flatMap(kid =>
              e.follow(kid, nodePreparer.getOrElse(n => n))
                .right.toOption)
            .flatten
          Right(kids.distinct)
        case failure@Left(msg) => failure
      }
    }
    r
  }

}

case class LocationStep(axis: AxisSpecifier, test: NodeTest, predicate: Option[Predicate] = None) {

  import ExpressionEngine.NodePreparer

  def follow(tn: TreeNode, nodePreparer: NodePreparer): ExecutionResult = test.follow(tn, axis) match {
    case Right(nodes) => Right(
      nodes
        .map(nodePreparer)
        .filter(tn => predicate.getOrElse(TruePredicate)(tn, nodes))
    )

    case failure => failure
  }
}

case class PathExpression(
                           elements: Seq[LocationStep]
                         ) {

  require(elements.nonEmpty, s"Must have path some elements in PathExpression")

}