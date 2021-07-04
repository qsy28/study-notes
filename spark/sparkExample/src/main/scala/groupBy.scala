import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._

object groupBy extends App{

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  import spark.implicits._
  val simpleData = Seq(("James","Sales","NY",90000,34,10000),
    ("Michael","Sales","NY",86000,56,20000),
    ("Robert","Sales","CA",81000,30,23000),
    ("Maria","Finance","CA",90000,24,23000),
    ("Raman","Finance","CA",99000,40,24000),
    ("Scott","Finance","NY",83000,36,19000),
    ("Jen","Finance","NY",79000,53,15000),
    ("Jeff","Marketing","CA",80000,25,18000),
    ("Kumar","Marketing","NY",91000,50,21000)
  )
  val df = simpleData.toDF("employee_name","department","state","salary","age","bonus")
  df.show()

  // groupBy and aggregate on multiple DataFrame columns
  df.groupBy("department").sum("salary","bonus").show()

  // Using agg() aggregate function we can calculate many aggregations at a time
  df.groupBy("department")
    .agg(sum("salary").as("sum_salary"),
      avg("salary").as("avg_salary"),
      count("state").as("state_nums"))
    .show()

  // Similar to SQL “HAVING” clause, On Spark DataFrame we can use either where() or filter() function to filter the rows of aggregated data.
  df.groupBy("department")
    .agg(sum("salary").as("sum_salary"),
      avg("salary").as("avg_salary"),
      count("state").as("state_nums"))
    .where(col("state_nums") > 2)
    .show()

  // groupBy, rollup, cube, grouping sets
  // https://jaceklaskowski.gitbooks.io/mastering-spark-sql/content/spark-sql-multi-dimensional-aggregation.html
  // Spark SQL supports GROUPING SETS clause in SQL mode only

  df.groupBy("department","state").sum("salary").show()
  df.rollup("department","state").sum("salary").show()
  df.cube("department","state").sum("salary").show()

  // select ... from ... groupby A,B grouping sets((A,B),(A),()) = select ... from ... groupby A,B with rollup
  // select ... from ... groupby A,B grouping sets((A,B),(A),(B),()) = select ... from ... groupby A,B with cube

}
