//> using scala 2.12.15

/*
 * Program 22 - Case Classes (Week 13)
 * Case class Person(name, age, city): filter people older than 30, group by city,
 * describe each person as "Name is Age years old and lives in City", sort by age.
 */
case class Person(name: String, age: Int, city: String)

object PersonRecords {

  def above30(people: Seq[Person]): Seq[Person] = people.filter { case Person(_, age, _) => age > 30 }

  def byCity(people: Seq[Person]): Map[String, Seq[String]] =
    people.groupBy(_.city).map { case (city, group) => city -> group.map(_.name) }

  def sentence(p: Person): String = p match {
    case Person(name, age, city) => s"$name is $age years old and lives in $city"
  }

  def youngestFirst(people: Seq[Person]): Seq[Person] = people.sortWith(_.age < _.age)

  def main(args: Array[String]): Unit = {
    val people = Seq(
      Person("Kavya", 33, "Pune"),
      Person("Rohan", 26, "Mumbai"),
      Person("Anita", 41, "Delhi"),
      Person("Vikram", 29, "Pune"),
      Person("Meera", 37, "Mumbai")
    )
    println("All people")
    println(f"  ${"Name"}%-8s ${"Age"}%3s  ${"City"}")
    people.foreach(p => println(f"  ${p.name}%-8s ${p.age}%3d  ${p.city}"))

    println("\nOlder than 30")
    above30(people).foreach(p => println("  " + p))

    println("\nGrouped by city")
    byCity(people).toSeq.sortBy(_._1).foreach { case (c, names) => println(s"  $c: ${names.mkString(", ")}") }

    println("\nSentences")
    people.map(sentence).foreach(s => println("  " + s))

    println("\nSorted by age (ascending)")
    youngestFirst(people).zipWithIndex.foreach { case (p, i) => println(s"  ${i + 1}. ${p.name} - ${p.age}") }

    println("\nAverage age : " + people.map(_.age).sum.toDouble / people.size)
    val moved = people(1).copy(city = "Bengaluru")
    println("copy()      : " + moved + "  (original unchanged: " + people(1) + ")")
    println("hashCode eq : " + (Person("Kavya", 33, "Pune").hashCode == people.head.hashCode))
  }
}
