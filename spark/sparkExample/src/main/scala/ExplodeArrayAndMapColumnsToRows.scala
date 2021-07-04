import org.apache.spark.sql.{Column, Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object ExplodeArrayAndMapColumnsToRows extends App{

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()


  val arrayData = Seq(
    Row("James",List("Java","Scala"),Map("hair"->"black","eye"->"brown")),
    Row("Michael",List("Spark","Java",null),Map("hair"->"brown","eye"->null)),
    Row("Robert",List("CSharp",""),Map("hair"->"red","eye"->"")),
    Row("Washington",null,null),
    Row("Jefferson",List(),Map())
  )

  val arraySchema = new StructType()
    .add("name",StringType)
    .add("knownLanguages", ArrayType(StringType))
    .add("properties", MapType(StringType,StringType))

  val df = spark.createDataFrame(spark.sparkContext.parallelize(arrayData),arraySchema)
  df.show(false)

  // explode()
  // explode – array column
  df.select(col("name"),explode(col("knownLanguages")))
    .show(false)
  // explode – map column example
  df.select(col("name"),explode(col("properties")))
    .show(false)

  // explode_outer()
  // if the array or map is null or empty, explode_outer returns null.
  df.select(col("name"),explode_outer(col("knownLanguages")))
    .show(false)
  df.select(col("name"),explode_outer(col("properties")))
    .show(false)

  // posexplode()
  //  creates two columns “pos’ to hold the position of the array element and the ‘col’ to hold the actual array value.
  df.select(col("name"),posexplode(col("knownLanguages")))
    .show(false)
  df.select(col("name"),posexplode(col("properties")))
    .show(false)

  // posexplode_outer() same as explode_outer()
  df.select(col("name"),posexplode_outer(col("knownLanguages")))
    .show(false)
  df.select(col("name"),posexplode_outer(col("properties")))
    .show(false)

}
