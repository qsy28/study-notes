import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object ArrayTypeFunctions extends App {

  val spark:SparkSession = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  // Creating Spark ArrayType Column on DataFrame
  val arrayStructureData = Seq(
    Row("James,,Smith",List("Java","Scala","C++"),List("Spark","Java"),"OH","CA"),
    Row("Michael,Rose,",List("Spark","Java","C++"),List("Spark","Java"),"NY","NJ"),
    Row("Robert,,Williams",List("CSharp","VB"),List("Spark","Python"),"UT","NV")
  )
  val arrayStructureSchema = new StructType()
    .add("name",StringType)
    .add("languagesAtSchool", ArrayType(StringType))
    .add("languagesAtWork", ArrayType(StringType))
    .add("currentState", StringType)
    .add("previousState", StringType)
  val df = spark.createDataFrame(
    spark.sparkContext.parallelize(arrayStructureData),arrayStructureSchema)
  df.show()

  // Spark ArrayType (Array) Functions
  // explode(): Use explode() function to create a new row for each element in the given array column.
  df.select(col("name"),explode(col("languagesAtSchool"))).show(false)
  // Split(): Splits the inputted column and returns an array type.
  df.select(split(col("name"),",").as("nameAsArray") ).show(false)
  // Creates a new array column.
  df.select(col("name"),array(col("currentState"),col("previousState")).as("States") ).show(false)
  // array_contains(): Returns null if the array is null, true if the array contains `value`, and false otherwise.
  df.select(col("name"),array_contains(col("languagesAtSchool"),"Java")
    .as("array_contains")).show(false)


}
