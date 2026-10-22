# Programming for Data Science Lab (MACSE502) – Scala

Shreyan Panda · 26MML0022 · VIT Vellore, Fall 2026

## Assessment 9 – Weeks 9 to 12

| Sl. No. | Week | Topic | File |
|--------:|-----:|-------|------|
| 18 | 9  | Arrays in Scala | [Prog18_ArrayOperations.scala](Assessment-9/Prog18_ArrayOperations.scala) |
| 19 | 10 | Lists, Sets and Tuples | [Prog19_CollectionsDemo.scala](Assessment-9/Prog19_CollectionsDemo.scala) |
| 20 | 11 | HashMap conflicts and collisions | [Prog20_GradeHashMaps.scala](Assessment-9/Prog20_GradeHashMaps.scala) |
| 21 | 12 | Functional combinators | [Prog21_CombinatorsLab.scala](Assessment-9/Prog21_CombinatorsLab.scala) |

## Assessment 10 – Weeks 13 and 14

| Sl. No. | Week | Topic | File |
|--------:|-----:|-------|------|
| 22 | 13 | Case classes | [Prog22_PersonRecords.scala](Assessment-10/Prog22_PersonRecords.scala) |
| 23 | 13 | Saddle + Vegas (Vega-Lite) | [Prog23_SaddleAppleAnalysis.scala](Assessment-10/Prog23_SaddleAppleAnalysis.scala) |
| 24 | 14 | Breeze + Plotly.Scala | [Prog24_BreezeRevenue.scala](Assessment-10/Prog24_BreezeRevenue.scala) |
| 25 | 14 | Spire + Saddle + Vegas (Vega-Lite) | [Prog25_SpirePriceStats.scala](Assessment-10/Prog25_SpirePriceStats.scala) |

Dataset: [`Assessment-10/fruit_market.csv`](Assessment-10/fruit_market.csv) – daily price and quantity of
Apple, Grapes, Papaya and Pineapple for 12 days (48 rows).

Console output and generated charts are in each folder's `output/` directory.

## Running

Requires [Scala CLI](https://scala-cli.virtuslab.org/); dependencies are declared in each file.

```bash
cd Assessment-10
scala-cli run Prog24_BreezeRevenue.scala
```

Vegas is published only for Scala 2.11, so programs 23 and 25 write the Vega-Lite specification
that Vegas would generate directly into an HTML page.
