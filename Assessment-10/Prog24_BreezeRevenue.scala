//> using scala 2.12.15
//> using dep org.scalanlp::breeze:2.1.0
//> using dep org.plotly-scala::plotly-render:0.8.4

/*
 * Program 24 - Use Breeze library (Week 14)
 * Compute the total revenue (price x quantity) of each fruit type with Breeze vectors
 * and show the revenues in a bar chart created with Plotly.Scala.
 */
import breeze.linalg.{DenseVector, DenseMatrix, sum, argmax, argmin}
import plotly._
import plotly.element._
import plotly.layout._
import scala.io.Source

object BreezeRevenue {

  case class Sale(date: String, fruit: String, price: Double, qty: Double)

  def main(args: Array[String]): Unit = {
    val src = Source.fromFile(if (args.nonEmpty) args(0) else "fruit_market.csv")
    val sales = try src.getLines().drop(1).filter(_.trim.nonEmpty).map { line =>
      val c = line.trim.split(",")
      Sale(c(0), c(1), c(2).toDouble, c(3).toDouble)
    }.toVector finally src.close()
    println(s"Sales records read : ${sales.size}")

    val fruits = sales.map(_.fruit).distinct.sorted
    // one row per fruit, one column per day
    val days = sales.map(_.date).distinct.sorted
    val priceM = DenseMatrix.tabulate(fruits.size, days.size) { (r, c) =>
      sales.find(s => s.fruit == fruits(r) && s.date == days(c)).map(_.price).getOrElse(0.0) }
    val qtyM = DenseMatrix.tabulate(fruits.size, days.size) { (r, c) =>
      sales.find(s => s.fruit == fruits(r) && s.date == days(c)).map(_.qty).getOrElse(0.0) }
    println(s"Price matrix       : ${priceM.rows} fruits x ${priceM.cols} days")

    // element-wise product gives the daily revenue matrix
    val revenueM = priceM *:* qtyM
    println("Day-1 revenue      : " + fruits.indices.map(r => f"${fruits(r)}=${revenueM(r, 0)}%.2f").mkString(", "))

    val totals: DenseVector[Double] = DenseVector(fruits.indices.map(r => sum(revenueM(r, ::).t)).toArray)
    val grand = sum(totals)
    println("\nTotal revenue by fruit")
    println(f"  ${"Fruit"}%-10s ${"Revenue (Rs)"}%14s ${"Share"}%7s ${"Avg price"}%10s")
    fruits.indices.foreach { r =>
      val avgPrice = sum(priceM(r, ::).t) / days.size
      println(f"  ${fruits(r)}%-10s ${totals(r)}%,14.2f ${totals(r) / grand * 100}%6.1f%% ${avgPrice}%10.2f")
    }
    println(f"\nAll fruits         : Rs $grand%,.2f")
    println(f"Best seller        : ${fruits(argmax(totals))}")
    println(f"Lowest revenue     : ${fruits(argmin(totals))}")

    val chart = Bar(fruits, totals.toArray.toSeq)
      .withText(totals.toArray.toSeq.map(v => f"Rs $v%,.0f"))
      .withMarker(Marker().withColor(Seq(Color.RGB(239, 83, 80), Color.RGB(126, 87, 194),
                                         Color.RGB(255, 167, 38), Color.RGB(38, 166, 154))))
    val layout = Layout()
      .withTitle("Fruit-wise Total Revenue (12 days)")
      .withXaxis(Axis().withTitle("Fruit type"))
      .withYaxis(Axis().withTitle("Revenue in Rs"))
    Plotly.plot("revenue_bar_chart.html", Seq(chart), layout, openInBrowser = false, addSuffixIfExists = false)
    println("Plotly chart saved : revenue_bar_chart.html")
  }
}
