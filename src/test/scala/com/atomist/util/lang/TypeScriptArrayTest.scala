package com.atomist.util.lang

import java.util

import com.atomist.project.SimpleProjectOperationArguments
import com.atomist.project.edit.NoModificationNeeded
import com.atomist.rug.runtime.js.interop.UserModelContext
import com.atomist.rug.runtime.js.{JavaScriptInvokingProjectEditor, JavaScriptOperationFinder}
import com.atomist.source.{FileArtifact, SimpleFileBasedArtifactSource, StringFileArtifact}
import org.scalatest.{FlatSpec, Matchers}

class TypeScriptArrayTest extends FlatSpec with Matchers {

  val EditorWithFancyListArray =
    """import {Project} from 'user-model/model/Core'
      |import {ParametersSupport} from 'user-model/operations/Parameters'
      |import {ProjectEditor} from 'user-model/operations/ProjectEditor'
      |import {Parameters} from 'user-model/operations/Parameters'
      |import {File} from 'user-model/model/Core'
      |import {Result,Status} from 'user-model/operations/Result'
      |
      |import {parameter} from 'user-model/support/Metadata'
      |import {inject} from 'user-model/support/Metadata'
      |import {parameters} from 'user-model/support/Metadata'
      |import {tag} from 'user-model/support/Metadata'
      |import {editor} from 'user-model/support/Metadata'
      |
      |class JavaInfo extends ParametersSupport {
      |  @parameter({description: "The Java package name", displayName: "Java Package", pattern: ".*", maxLength: 100})
      |  packageName: string = null
      |}
      |
      |@editor("A nice little editor")
      |@tag("java")
      |@tag("maven")
      |class ConstructedEditor implements ProjectEditor<Parameters> {
      |
      |    private lyst: string[];
      |
      |    constructor(@inject("FancyList") _lyst: string[] ){
      |      this.lyst = _lyst;
      |    }
      |    edit(project: Project, @parameters("JavaInfo") ji: JavaInfo) {
      |       this.lyst[0].toString()
      |
      |       //ensure we return another TypeArray
      |       project.files().sort().sort()
      |
      |       this.lyst.filter(t => true)
      |       if(this.lyst.length != 1){
      |          throw new Error("Array length should be 1 after filtering none");
      |       }
      |
      |       this.lyst.filter(t => false)
      |       if(this.lyst.length != 0){
      |          throw new Error("Array length should be 0 after filtering all");
      |       }
      |       this.lyst.push("another")
      |       if(this.lyst.length != 1){
      |          throw new Error("Array length should be 1 after push");
      |       }
      |       this.lyst.push("another2")
      |
      |       let s: string = this.lyst.pop()
      |       if(s != "another2") {
      |          throw new Error("Was expecting 'another2' as result of pop")
      |       }
      |
      |       let strs: string[] = this.lyst.concat(["1"], ["2", "3"])
      |       if(strs.length != 4){
      |         throw new Error("Array length should be 4 after concat");
      |       }
      |       this.lyst.push("thing")
      |       let str: string = this.lyst.join(".")
      |
      |       if(str != "another.thing"){
      |           throw new Error("Join should not be: "+ str)
      |       }
      |
      |       this.lyst.reverse()
      |
      |       if(this.lyst[0] != "thing"){
      |          throw new Error("Expecting 'thing' in first element after reverse")
      |       }
      |
      |       let first: string = this.lyst.shift()
      |       if(first != "thing" || this.lyst.length != 1){
      |         throw new Error("First element should be 'thing' and legnth should be 1 after shift")
      |       }
      |       this.lyst.push("is")
      |       this.lyst.push("a")
      |       this.lyst.push("good")
      |       this.lyst.push("movie")
      |       let sliced: string[] = this.lyst.slice(1)
      |       if(sliced[0] != "is" || sliced[3] != "movie") {
      |          throw new Error("Simple slice failed")
      |       }
      |
      |       let sliced2: string[] = this.lyst.slice(-1)
      |       if(sliced2[0] != "movie") {
      |          throw new Error("Simple negative slice failed")
      |       }
      |
      |       let sliced3: string[] = this.lyst.slice(1,4)
      |       if(sliced3[0] != "is" || sliced3[2] != "good") {
      |          console.log(sliced3.toString())
      |          throw new Error("End slice failed")
      |       }
      |
      |       let sliced4: string[] = this.lyst.slice(1,-1)
      |       if(sliced4[0] != "is" || sliced4[2] != "good") {
      |          throw new Error("Negative end slice failed")
      |       }
      |       let sorted: string[] = this.lyst.sort()
      |
      |       if(sorted[0] != "a" || sorted[4] != "movie"){
      |          console.log(sorted.toString())
      |          throw new Error("Lexical sorting failed")
      |       }
      |
      |       let sorted2: string[]  = this.lyst.sort ((s1, s2) => s1.localeCompare(s1))
      |
      |       if(sorted2[0] != "a" || sorted2[4] != "movie"){
      |          throw new Error("Lexical sorting failed")
      |       }
      |
      |       let spliced: string[] = this.lyst.splice(1)
      |       if(this.lyst[0] != "a" || spliced[0] != "another" || spliced[3] != "movie"){
      |          console.log(this.lyst.toString())
      |          console.log(spliced.toString())
      |          throw new Error("1 arg splice should remove (length - start) from begining")
      |       }
      |
      |       this.lyst.push("lazy")
      |       this.lyst.push("fox")
      |       this.lyst.push("jumps")
      |       let spliced2: string[] = this.lyst.splice(-1)
      |       if(spliced2.length != 2 || this.lyst.length != 2 || spliced2[0] != "fox" || spliced2[1] != "jumps"){
      |          console.log(spliced2.toString())
      |          console.log(this.lyst.toString())
      |          throw new Error("-1 should start from the end -1 and always be 0 length")
      |       }
      |       this.lyst.pop()
      |       this.lyst.pop()
      |
      |       this.lyst.push("the")
      |       this.lyst.push("lazy")
      |       this.lyst.push("fox")
      |       this.lyst.push("jumps")
      |
      |       let spliced3: string[] = this.lyst.splice(1,2)
      |       if(spliced3.length != 2 || spliced3[0] != "lazy"  || spliced3[1] != "fox"){
      |          console.log(this.lyst.toString())
      |          console.log(spliced3.toString())
      |          throw new Error("Should only remove 2 of the right things")
      |       }
      |       this.lyst.pop()
      |       this.lyst.pop()
      |
      |       this.lyst.push("the")
      |       this.lyst.push("quick")
      |       this.lyst.push("brown")
      |       this.lyst.push("fox")
      |       let spliced4: string[] = this.lyst.splice(1,2, "jumps", "over", "the", "lazy", "dog")
      |       if(spliced4.length != 2 || this.lyst[0] != "the"  || this.lyst[1] != "fox" || this.lyst[6] != "dog"){
      |          console.log(this.lyst.toString())
      |          console.log(spliced4.toString())
      |          throw new Error("The quick brown fox didn't jump over the lazy dog")
      |       }
      |
      |       let total = this.lyst.unshift("why", "does")
      |       if(total != 9 || this.lyst[0] != "why") {
      |          console.log(this.lyst.toString())
      |          throw new Error("Shifting should insert things at the beginning of the array")
      |       }
      |
      |       if(this.lyst.indexOf("does") != 1){
      |          console.log(this.lyst.toString())
      |          throw new Error("indexOf 'does' should be 1")
      |       }
      |       this.lyst.push("the")
      |
      |       if(this.lyst.indexOf("the", 3) != 6){
      |          console.log(this.lyst.toString())
      |          throw new Error("indexOf 'the' should be 6")
      |       }
      |       if(this.lyst.lastIndexOf("the") != 9){
      |          console.log(this.lyst.toString())
      |          throw new Error("indexOf 'the' should be 9")
      |       }
      |
      |
      |       if(this.lyst.every(t => true) != true){
      |          throw new Error("Every should be true");
      |       }
      |
      |       if(this.lyst.every(t => false) != false){
      |          throw new Error("Every should be false");
      |       }
      |
      |       if(this.lyst.some(t => t == "the") != true){
      |          throw new Error("Some should be true");
      |       }
      |
      |       if(this.lyst.some(t => t == "doggy") != false){
      |          throw new Error("Some should be false");
      |       }
      |
      |       this.lyst.forEach(t => console.log(t))
      |
      |       let another: number[] = this.lyst.map(t => t.length)
      |       if(another.length != this.lyst.length){
      |           throw new Error("Length of array after map should be the same")
      |       }
      |
      |       let reduced: string = this.lyst.reduce((acc, cur) => acc + cur)
      |       if(reduced != "whydoesthefoxjumpsoverthelazydogthe"){
      |          throw new Error("Reduced should be: whydoesthefoxjumpsoverthelazydogthe, was: " + reduced)
      |       }
      |
      |       let reduced2: string = this.lyst.reduce((acc, cur) => acc + cur, "phrase:")
      |       if(reduced2 != "phrase:whydoesthefoxjumpsoverthelazydogthe"){
      |          throw new Error("Reduced should be: phrase:whydoesthefoxjumpsoverthelazydogthe, was: " + reduced2)
      |       }
      |
      |       let reduced3: string = this.lyst.reduceRight((acc, cur) => acc + cur)
      |       if(reduced3 != "thedoglazytheoverjumpsfoxthedoeswhy"){
      |          throw new Error("Reduced right should be: phrase:thedoglazytheoverjumpsfoxthedoeswhy, was: " + reduced3)
      |       }
      |       return new Result(Status.Success, "")
      |    }
      |  }
      | """.stripMargin

  it should "behave just like a normal JS/TS array, but also support some cool Java stuff!" in {
    invokeAndVerifyConstructed(StringFileArtifact(s".atomist/ConstructedEditor.ts",
      EditorWithFancyListArray))
  }

  private def invokeAndVerifyConstructed(tsf: FileArtifact): JavaScriptInvokingProjectEditor = {
    val as = SimpleFileBasedArtifactSource(tsf)
    val jsed = JavaScriptOperationFinder.fromTypeScriptArchive(as, TemporaryRegistry).head.asInstanceOf[JavaScriptInvokingProjectEditor]
    jsed.name should be("Constructed")

    val target = SimpleFileBasedArtifactSource(StringFileArtifact("pom.xml", "nasty stuff"))

    jsed.modify(target, SimpleProjectOperationArguments("", Map("packageName" -> "com.atomist.crushed"))) match {
      case sm: NoModificationNeeded =>
      sm.comment.contains("OK") should be(true)
    }
    jsed
  }

  object TemporaryRegistry extends UserModelContext{

    val lyst = new util.ArrayList[String]()
    lyst.add("blah")
    override val registry = Map(
      "FancyList" -> new TypeScriptArray[String](lyst)
    )
  }
}
