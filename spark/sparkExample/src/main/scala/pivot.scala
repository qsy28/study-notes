import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions.{expr}
import org.apache.spark.sql.types.{ArrayType, StringType, StructType,StructField}
import org.apache.spark.sql.functions._

/**
 * pivot() to transpose one or multiple rows into columns
 * rotate column values into rows values
 */
object pivot extends App {

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  val data = Seq(("Banana",1000,"USA"), ("Carrots",1500,"USA"), ("Beans",1600,"USA"),
    ("Orange",2000,"USA"),("Orange",2000,"USA"),("Banana",400,"China"),
    ("Carrots",1200,"China"),("Beans",1500,"China"),("Orange",4000,"China"),
    ("Banana",2000,"Canada"),("Carrots",2000,"Canada"),("Beans",2000,"Mexico"))

  import spark.sqlContext.implicits._
  val df = data.toDF("Product","Amount","Country")
  df.show()

  // pivot()
  val pivotDF = df.groupBy("Product").pivot("Country").sum("Amount")
  pivotDF.show()
  // Below two examples returns the same output but with better performance.
  val countries = Seq("USA","China","Canada","Mexico")
  val pivotDF1 = df.groupBy("Product").pivot("Country", countries).sum("Amount")
  pivotDF1.show()
  val pivotDF2 = df.groupBy("Product","Country")
    .sum("Amount")
    .groupBy("Product")
    .pivot("Country")
    .sum("sum(Amount)")
  pivotDF2.show()

  //unpivot
  val unPivotDF = pivotDF.select($"Product",
    expr("stack(3, 'Canada', Canada, 'China', China, 'Mexico', Mexico) as (Country,Total)"))
    .where("Total is not null")
  unPivotDF.show()

  // 非聚合的行转列
  val df1 = Seq(
    ("col1", "val1"),
    ("col2", "val2"),
    ("col3", "val3"),
    ("col4", "val4"),
    ("col5", "val5")
  ).toDF("COLUMN_NAME", "VALUE")
  df1.show()

  df1.groupBy()
    .pivot("COLUMN_NAME")
    .agg(first("VALUE"))
    .show()



}
