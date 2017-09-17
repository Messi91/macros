import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

class Benchmark extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro Benchmark.impl
}

object Benchmark {
  def impl(context: Context)(annottees: context.Expr[Any]*): context.Expr[Any] = {
    import context.universe._

    val result = annottees.map(_.tree).toList match {
      case q"$mods def $methodName[..$tpes](...$args): $returnType = { ..$body }" :: Nil =>
        q"""
           $mods def $methodName[..$tpes](...$args): $returnType = {
             val start = System.nanoTime()
             val result = { ..$body }
             val end = System.nanoTime()
             println("\n$methodName elapsed time: " + (end - start) + "ns\n")
             result
           }
         """
      case _ => context.abort(context.enclosingPosition, "Annotation @Benchmark can be used only with methods")
    }
    context.Expr[Any](result)
  }
}
