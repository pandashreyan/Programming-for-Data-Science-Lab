//> using scala 2.12.15

/*
 * Program 18 - Arrays in Scala (Week 9)
 * Create an integer array (2, 5, 9, 14, 20) and a function returning the sum of its
 * elements, create an array of five fruit names and a function that joins them with
 * commas, then iterate over the integer array and print every element.
 */
object ArrayOperations {

  // sum of the elements using a while loop
  def arraySum(values: Array[Int]): Int = {
    var i = 0
    var total = 0
    while (i < values.length) {
      total = total + values(i)
      i += 1
    }
    total
  }

  // sum of the elements written recursively, used to cross-check arraySum
  def arraySumRecursive(values: Array[Int], index: Int = 0): Int =
    if (index == values.length) 0 else values(index) + arraySumRecursive(values, index + 1)

  // joins the fruit names separated by commas
  def joinWithCommas(names: Array[String]): String =
    names.reduce((left, right) => left + ", " + right)

  def main(args: Array[String]): Unit = {
    val marks = Array(2, 5, 9, 14, 20)
    println("=== Integer array ===")
    println(s"Elements           -> ${marks.mkString(" | ")}")
    println(s"Number of elements -> ${marks.size}")
    println(s"First / Last       -> ${marks.head} / ${marks.last}")
    println(s"Sum (while loop)   -> ${arraySum(marks)}")
    println(s"Sum (recursion)    -> ${arraySumRecursive(marks)}")
    println(s"Sum (foldLeft)     -> ${marks.foldLeft(0)(_ + _)}")

    val fruitBasket = Array("Papaya", "Guava", "Kiwi", "Pineapple", "Cherry")
    println("\n=== String array ===")
    println(s"Fruits             -> ${fruitBasket.mkString(" | ")}")
    println(s"Comma separated    -> ${joinWithCommas(fruitBasket)}")

    println("\n=== Iterating the integer array (while loop) ===")
    var pos = 0
    while (pos < marks.length) {
      println(s"Position $pos holds ${marks(pos)}")
      pos += 1
    }

    println("\n=== Iterating with zipWithIndex ===")
    for ((value, idx) <- marks.zipWithIndex) println(s"[$idx] = $value")
  }
}
