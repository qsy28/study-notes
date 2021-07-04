import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

/**
 * Spark map() is a transformation operation that is used to apply the transformation on every element of RDD,
 * DataFrame, and Dataset and finally returns a new RDD/Dataset respectively
 */
object mapFunction extends App {

  val spark = SparkSession.builder().master("local[1]").appName("SparkByExamples.com").getOrCreate()

  // usage on rdd
  val data = Seq("Project",
    "Gutenberg’s",
    "Alice’s",
    "Adventures",
    "in",
    "Wonderland",
    "Project",
    "Gutenberg’s",
    "Adventures",
    "in",
    "Wonderland",
    "Project",
    "Gutenberg’s")

  val rdd=spark.sparkContext.parallelize(data)
  val rdd2=rdd.map(f=> (f,1))
  rdd2.foreach(println)

  // usage on df
  val structureData = Seq(
    Row("James","","Smith","36636","NewYork",3100),
    Row("Michael","Rose","","40288","California",4300),
    Row("Robert","","Williams","42114","Florida",1400),
    Row("Maria","Anne","Jones","39192","Florida",5500),
    Row("Jen","Mary","Brown","34561","NewYork",3000)
  )

  val structureSchema = new StructType()
    .add("firstname",StringType)
    .add("middlename",StringType)
    .add("lastname",StringType)
    .add("id",StringType)
    .add("location",StringType)
    .add("salary",IntegerType)

  val df2 = spark.createDataFrame(spark.sparkContext.parallelize(structureData),structureSchema)
  df2.printSchema()
  df2.show(false)

  import spark.implicits._

  val df3 = df2.map(row=>{
    val fullName = row.getString(0) +row.getString(1) +row.getString(2)
    (fullName, row.getString(3),row.getInt(5))
  })
  // One key point to remember is these both transformations returns the Dataset[U] but not the DataFrame
  val df3Map =  df3.toDF("fullName","id","salary")

  df3Map.printSchema()
  df3Map.show(false)



}
