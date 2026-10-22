//> using scala 2.12.15

/*
 * Program 19 - Lists, Sets and Tuples in Scala (Week 10)
 * Square the first five primes, build a set of unique characters from "hello world",
 * intersect two sets and access the elements of an (Int, String, Boolean) tuple.
 */
object CollectionsDemo {

  // returns a new list with every element squared (for-comprehension)
  def squares(xs: List[Int]): List[Int] = for (x <- xs) yield x * x

  // returns the elements present in both sets
  def commonElements[T](a: Set[T], b: Set[T]): Set[T] = a.filter(b.contains)

  def main(args: Array[String]): Unit = {
    val firstPrimes = 2 :: 3 :: 5 :: 7 :: 11 :: Nil
    println("--- LIST ---")
    println("Primes            : " + firstPrimes.mkString("[", ", ", "]"))
    println("Squares           : " + squares(firstPrimes).mkString("[", ", ", "]"))
    println("Sum of squares    : " + squares(firstPrimes).sum)

    val phrase = "hello world"
    val letters: Set[Char] = phrase.replace(" ", "").toSet
    println("\n--- SET ---")
    println("Characters in \"" + phrase + "\" : " + phrase.length + " (with space)")
    println("Unique letters    : " + letters.toList.sorted.mkString("{", ", ", "}"))
    println("Unique count      : " + letters.size)

    val other: Set[Char] = "good morning".replace(" ", "").toSet
    println("\"good morning\" set : " + other.toList.sorted.mkString("{", ", ", "}"))
    println("Common letters    : " + commonElements(letters, other).toList.sorted.mkString("{", ", ", "}"))
    val evens = Set(2, 4, 6, 8, 10)
    val squaresSet = squares(firstPrimes).toSet ++ Set(16, 36, 64)
    println(s"Common numbers    : ${commonElements(evens, squaresSet).toList.sorted}")

    val profile: (Int, String, Boolean) = (22, "Shreyan", false)
    println("\n--- TUPLE ---")
    println("Tuple             : " + profile)
    println("Integer  (_1)     : " + profile._1)
    println("String   (_2)     : " + profile._2)
    println("Boolean  (_3)     : " + profile._3)
    println("Arity             : " + profile.productArity)
    profile.productIterator.zipWithIndex.foreach { case (v, i) => println(s"  element ${i + 1} -> $v") }
  }
}
