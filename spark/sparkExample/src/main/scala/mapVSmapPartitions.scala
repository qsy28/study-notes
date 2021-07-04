import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

/***
 * Spark map() and mapPartitions() transformations apply the function on each element/record/row of the DataFrame/Dataset and returns the new DataFrame/Dataset
 * flatMap()
 */
object mapVSmapPartitions extends App {

  class Util extends Serializable {
    def combine(fname:String,mname:String,lname:String):String = {
      fname+","+mname+","+lname
    }
  }

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

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

  var df = spark.createDataFrame(spark.sparkContext.parallelize(structureData),structureSchema)
  import spark.implicits._
  val util = new Util()
  val df1 = df.map(row=>{
    val fullName = util.combine(row.getString(0),row.getString(1),row.getString(2))
    (fullName, row.getString(3),row.getInt(5))
  })
  val df1Map =  df1.toDF("fullName","id","salary")
  df1Map.show(false)

  val df2 = df.mapPartitions(iterator => {
    // Do the heavy initialization here
    // Like database connections e.t.c
    // 实例化util是放在mapPartitions内部
    val util = new Util()
    val res = iterator.map(row=>{
      val fullName = util.combine(row.getString(0),row.getString(1),row.getString(2))
      (fullName, row.getString(3),row.getInt(5))
    })
    res
  })
  val df4part = df2.toDF("fullName","id","salary")
  df4part.printSchema()
  df4part.show(false)

  // difference between map() and flatMap()
  val data = Seq("Project Gutenberg’s",
    "Alice’s Adventures in Wonderland",
    "Project Gutenberg’s",
    "Adventures in Wonderland",
    "Project Gutenberg’s")

  val datardd = spark.sparkContext.parallelize(data)
  import spark.implicits._

  val df3 = datardd.toDF("data")

  //Map Transformation
  val mapDF=df3.map(fun=> {
    fun.getString(0).split(" ")
  })
  mapDF.show()

  //Flat Map Transformation
  // flatMap() converted the array into a row
  val flatMapDF=df.flatMap(fun=>
  {
    fun.getString(0).split(" ")
  })
  flatMapDF.show(false)

  val arrayStructureData = Seq(
    Row("James,,Smith",List("Java","Scala","C++"),"CA"),
    Row("Michael,Rose,",List("Spark","Java","C++"),"NJ"),
    Row("Robert,,Williams",List("CSharp","VB","R"),"NV")
  )

  val arrayStructureSchema = new StructType()
    .add("name",StringType)
    .add("languagesAtSchool", ArrayType(StringType))
    .add("currentState", StringType)

  val df4 = spark.createDataFrame(
    spark.sparkContext.parallelize(arrayStructureData),arrayStructureSchema)
  import spark.implicits._

  //flatMap() Usage
  val df5=df4.flatMap(f => {
    val lang=f.getSeq[String](1)
    println(lang)
    val x = lang.map((f.getString(0),_,f.getString(2)))
    println(x)
    x
  })
  println(df5)
  df5.show()

  val df6=df5.toDF("Name","language","State")
  df6.show(false)

  val df7=df4.flatMap(f => {
    val lang=f.getSeq[String](1)
    lang
  })
  println(df7)
  df7.show(100)


}
