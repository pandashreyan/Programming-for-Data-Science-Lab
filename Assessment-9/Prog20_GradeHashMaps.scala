//> using scala 2.12.15

/*
 * Program 20 - Handling collisions and resolving conflicts in HashMaps (Week 11)
 * Store student grades in a HashMap (including a duplicate key), merge two maps keeping
 * the higher grade, and handle collisions by chaining values in lists.
 */
import scala.collection.mutable

object GradeHashMaps {

  // merge using foldLeft: on a conflict the higher grade wins
  def mergeKeepHigher(a: Map[String, Int], b: Map[String, Int]): Map[String, Int] =
    b.foldLeft(a) { case (acc, (student, grade)) =>
      acc + (student -> acc.get(student).map(_ max grade).getOrElse(grade))
    }

  // chaining: every key keeps a ListBuffer of all values inserted for it
  def addChained(store: mutable.Map[String, mutable.ListBuffer[Int]], key: String, grade: Int): Unit =
    store.getOrElseUpdate(key, mutable.ListBuffer[Int]()) += grade

  // a tiny hash table with 5 buckets; a bucket is a List of (key, values) pairs
  class BucketTable(size: Int) {
    val slots: Array[List[(String, List[Int])]] = Array.fill(size)(Nil)
    private def slotOf(key: String): Int = (key.hashCode & Int.MaxValue) % size

    def insert(key: String, grade: Int): Unit = {
      val s = slotOf(key)
      slots(s).find(_._1 == key) match {
        case Some(_) =>
          slots(s) = slots(s).map(e => if (e._1 == key) (key, e._2 :+ grade) else e)
        case None =>
          if (slots(s).nonEmpty) println(f"  collision -> $key%-6s goes to slot $s with ${slots(s).map(_._1).mkString(", ")}")
          slots(s) = slots(s) :+ (key -> List(grade))
      }
    }

    def lookup(key: String): List[Int] = slots(slotOf(key)).collectFirst { case (`key`, v) => v }.getOrElse(Nil)
  }

  def main(args: Array[String]): Unit = {
    val classA = mutable.HashMap("Rahul" -> 72, "Priya" -> 88, "Arjun" -> 64, "Sneha" -> 91)
    println("Class A grades           : " + classA.toSeq.sortBy(_._1).mkString(", "))
    classA.put("Arjun", 79) // duplicate key, different grade
    println("After re-inserting Arjun : " + classA.toSeq.sortBy(_._1).mkString(", "))
    println("Entries in map           : " + classA.size)

    val classB = Map("Priya" -> 84, "Arjun" -> 83, "Karan" -> 77, "Sneha" -> 95)
    val merged = mergeKeepHigher(classA.toMap, classB)
    println("\nClass B grades           : " + classB.toSeq.sortBy(_._1).mkString(", "))
    println("Merged (higher grade)    : " + merged.toSeq.sortBy(_._1).mkString(", "))

    val history = mutable.LinkedHashMap[String, mutable.ListBuffer[Int]]()
    Seq("Rahul" -> 72, "Priya" -> 88, "Rahul" -> 81, "Priya" -> 84, "Sneha" -> 91, "Rahul" -> 69)
      .foreach { case (k, g) => addChained(history, k, g) }
    println("\nGrade history (chained values per key):")
    history.foreach { case (k, gs) => println(f"  $k%-6s -> ${gs.mkString("[", ", ", "]")}") }

    println("\nInserting into a 5-slot hash table:")
    val table = new BucketTable(5)
    Seq("Rahul" -> 72, "Priya" -> 88, "Arjun" -> 79, "Sneha" -> 91, "Karan" -> 77,
        "Meera" -> 85, "Dev" -> 70, "Priya" -> 84).foreach { case (k, g) => table.insert(k, g) }
    println("\nSlots:")
    table.slots.zipWithIndex.foreach { case (chain, i) =>
      println(s"  slot $i : " + (if (chain.isEmpty) "-" else chain.map { case (k, v) => s"$k${v.mkString("(", ",", ")")}" }.mkString(" -> ")))
    }
    println("\nlookup(Priya) = " + table.lookup("Priya"))
    println("lookup(Dev)   = " + table.lookup("Dev"))
    println("lookup(Zara)  = " + table.lookup("Zara"))
  }
}
