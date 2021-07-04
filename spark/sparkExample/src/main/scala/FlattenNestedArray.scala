import org.apache.spark.sql.{Column, Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._


object FlattenNestedArray extends App{

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  val arrayArrayData = Seq(
    Row("James",List(List("Java","Scala","C++"),List("Spark","Java"))),
    Row("Michael",List(List("Spark","Java","C++"),List("Spark","Java"))),
    Row("Robert",List(List("CSharp","VB"),List("Spark","Python")))
  )

  val arrayArraySchema = new StructType().add("name",StringType)
    .add("subjects",ArrayType(ArrayType(StringType)))

  val df = spark.createDataFrame(
    spark.sparkContext.parallelize(arrayArrayData),arrayArraySchema)
  df.printSchema()
  df.show()
  // flatten 无法编译？
  // split 只能展平 list(), 不能展平list(list(),list())
  // df.select("name", flatten(col("subjects"))).show()
}
