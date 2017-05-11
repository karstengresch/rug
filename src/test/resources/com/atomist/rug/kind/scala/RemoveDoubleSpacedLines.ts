import {Project,File} from '@atomist/rug/model/Core'
import {Editor} from '@atomist/rug/operations/Decorators'
import {PathExpression,TextTreeNode,TypeProvider} from '@atomist/rug/tree/PathExpression'
import {PathExpressionEngine} from '@atomist/rug/tree/PathExpression'

/**
 * Removes double spacing from Scala and Java files.
 */
@Editor("Remove double spacing")
class RemoveDoubleSpacedLines  {

    private targetExtensions = [ ".java", ".scala"]

    edit(project: Project) {
        let eng: PathExpressionEngine = project.context.pathExpressionEngine

        eng.with<File>(project, `/src//File()`, f => {
            if (this.targetExtensions.filter(suffix => f.name.indexOf(suffix) > -1).length) {
                // console.log(`The file is ${f}`)
                f.regexpReplace("(?m)\n$\n$", "\n")
            }
        })
    }
}

export let editor = new RemoveDoubleSpacedLines()
