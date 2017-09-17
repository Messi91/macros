import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object Welcome {
  def isEvenLog(number: Int): Unit = macro isEvenLogImplementation

  def isEvenLogImplementation(context: Context)(number: context.Tree): context.Tree = {
    import context.universe._

    val result = q"""
       val evaluatedNumber = $number
       if(evaluatedNumber%2 == 0) {
         println("\n" + evaluatedNumber.toString + " is even\n")
       } else {
         println("\n" + evaluatedNumber.toString + " is odd\n")
       }
     """
    println("\n" + showCode(result))
    result
  }
}
