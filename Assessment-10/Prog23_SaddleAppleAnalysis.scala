//> using scala 2.12.15
//> using dep io.github.pityka::saddle-core:3.3.0

/*
 * Program 23 - Use Saddle to load and manipulate a dataset (Week 13)
 * Load fruit_market.csv (Date, Fruit, Price, Quantity) with Saddle, keep only the apple
 * rows, compute the average apple price and plot the apple price trend over time.
 * Vegas (Scala 2.11 only) renders charts as Vega-Lite; the same Vega-Lite spec is
 * written here to apple_trend_chart.html.
 */
import org.saddle._
import org.saddle.csv.CsvParser
import scala.io.Source
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets

object SaddleAppleAnalysis {

  def loadFrame(file: String): Frame[Int, String, String] = {
    val clean = Source.fromFile(file).getLines().mkString("\n") + "\n"
    CsvParser.parseSourceWithHeader[String](Source.fromString(clean), recordSeparator = "\n")
      .fold(msg => throw new IllegalArgumentException(msg), identity)
  }

  def writeVegaLite(points: Seq[(String, Double)], average: Double, file: String): Unit = {
    val data = points.map { case (d, p) => s"""{"day":"$d","price":$p}""" }.mkString(",")
    val spec =
      s"""{"$$schema":"https://vega.github.io/schema/vega-lite/v5.json",
         |"title":"Apple Price Trend (Rs/kg)","width":620,"height":330,
         |"data":{"values":[$data]},
         |"layer":[
         | {"mark":{"type":"area","line":{"color":"#2e7d32"},"color":"#c8e6c9","opacity":0.6},
         |  "encoding":{"x":{"field":"day","type":"ordinal","title":"Day","axis":{"labelAngle":-40}},
         |              "y":{"field":"price","type":"quantitative","title":"Price","scale":{"zero":false}}}},
         | {"mark":{"type":"point","filled":true,"color":"#1b5e20"},
         |  "encoding":{"x":{"field":"day","type":"ordinal"},"y":{"field":"price","type":"quantitative"},
         |              "tooltip":[{"field":"day"},{"field":"price"}]}},
         | {"mark":{"type":"rule","color":"#e65100","strokeDash":[6,4]},
         |  "encoding":{"y":{"datum":$average}}}
         |]}""".stripMargin
    val html =
      s"""<!DOCTYPE html><html><head><meta charset="utf-8"><title>Apple Price Trend</title>
         |<script src="https://cdn.jsdelivr.net/npm/vega@5.25.0"></script>
         |<script src="https://cdn.jsdelivr.net/npm/vega-lite@5.16.3"></script>
         |<script src="https://cdn.jsdelivr.net/npm/vega-embed@6.22.2"></script></head>
         |<body><div id="chart"></div><script>vegaEmbed('#chart', $spec);</script></body></html>""".stripMargin
    Files.write(Paths.get(file), html.getBytes(StandardCharsets.UTF_8))
  }

  def main(args: Array[String]): Unit = {
    val market = loadFrame(if (args.nonEmpty) args(0) else "fruit_market.csv")
    println(s"Frame shape : ${market.numRows} rows, ${market.numCols} columns")
    println(s"Columns     : ${market.colIx.toSeq.mkString(" | ")}")
    println("Fruits      : " + market.firstCol("Fruit").toVec.toSeq.distinct.mkString(", "))
    println("\nSample (rows 0-5):")
    println(market.rowSlice(0, 6).stringify(6, 4))

    val apples = market.rfilter(_.first("Fruit").get == "Apple")
    val applePrice = apples.firstCol("Price").mapValues(_.toDouble)
    val appleQty = apples.firstCol("Quantity").mapValues(_.toInt)
    println(s"Apple rows  : ${apples.numRows}")
    println(apples.col("Date", "Price", "Quantity").stringify(12, 3))

    val avg = applePrice.toVec.mean
    println(f"Average apple price : Rs $avg%.2f per kg")
    println(f"Price range         : Rs ${applePrice.toVec.min2}%.2f - Rs ${applePrice.toVec.max2}%.2f")
    println(s"Apples sold (total) : ${appleQty.sum} kg")
    val days = apples.firstCol("Date").toVec.toSeq
    val above = applePrice.toVec.toSeq.count(_ > avg)
    println(s"Days above average  : $above of ${days.size}")

    writeVegaLite(days.map(_.substring(5)).zip(applePrice.toVec.toSeq), avg, "apple_trend_chart.html")
    println("\nVega-Lite chart written to apple_trend_chart.html")
  }
}
