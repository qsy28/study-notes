import org.apache.spark.sql.functions.{struct,col,when}
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.DataTypes


object dataType extends App{

  val arr = ArrayType(IntegerType,false)
  println("json() : "+arr.json)  // Represents json string of datatype
  println("prettyJson() : "+arr.prettyJson) // Gets json in pretty format
  println("simpleString() : "+arr.simpleString) // simple string
  println("sql() : "+arr.sql) // SQL format
  println("typeName() : "+arr.typeName) // type name
  println("catalogString() : "+arr.catalogString) // catalog string
  println("defaultSize() : "+arr.defaultSize) // default size

  val strType = DataTypes.StringType
  println("json : "+strType.json)
  println("prettyJson : "+strType.prettyJson)
  println("simpleString : "+strType.simpleString)
  println("sql : "+strType.sql)
  println("typeName : "+strType.typeName)
  println("catalogString : "+strType.catalogString)
  println("defaultSize : "+strType.defaultSize)


  // StructType
  import org.apache.spark.sql.{Row, SparkSession}
  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  val simpleData = Seq(Row("James ","","Smith","36636","M",3000),
    Row("Michael ","Rose","","40288","M",4000),
    Row("Robert ","","Williams","42114","M",4000),
    Row("Maria ","Anne","Jones","39192","F",4000),
    Row("Jen","Mary","Brown","","F",-1)
  )
  // StructType is a collection of StructField
  // StructField is used to define the column name, data type and a flag for nullable or not
  val simpleSchema = StructType(Array(
    StructField("firstname",StringType,true),
    StructField("middlename",StringType,true),
    StructField("lastname",StringType,true),
    StructField("id", StringType, true),
    StructField("gender", StringType, true),
    StructField("salary", IntegerType, true)
  ))

  val df = spark.createDataFrame(
    spark.sparkContext.parallelize(simpleData),simpleSchema)
  df.printSchema()
  df.show()

  // Defining nested StructType object struct
  // add()
  val structureData = Seq(
    Row(Row("James ","","Smith"),"36636","M",3100),
    Row(Row("Michael ","Rose",""),"40288","M",4300),
    Row(Row("Robert ","","Williams"),"42114","M",1400),
    Row(Row("Maria ","Anne","Jones"),"39192","F",5500),
    Row(Row("Jen","Mary","Brown"),"","F",-1)
  )

  // column "name" data type is StructType
  val structureSchema = new StructType()
    .add("name",new StructType()
      .add("firstname",StringType)
      .add("middlename",StringType)
      .add("lastname",StringType))
    .add("id",StringType)
    .add("gender",StringType)
    .add("salary",IntegerType)

  val df2 = spark.createDataFrame(
    spark.sparkContext.parallelize(structureData),structureSchema)
  df2.printSchema()
  df2.show()

  // struct()
  // we can change the struct of the existing DataFrame and add a new StructType to it
  val updatedDF = df2.withColumn("OtherInfo",
    struct(  col("id").as("identifier"),
      col("gender").as("gender"),
      col("salary").as("salary"),
      when(col("salary").cast(IntegerType) < 2000,"Low")
        .when(col("salary").cast(IntegerType) < 4000,"Medium")
        .otherwise("High").alias("Salary_Grade")
    )).drop("id","gender","salary")

  updatedDF.printSchema()
  updatedDF.show(false)

  val arrayStructureData = Seq(
    Row(Row("James ","","Smith"),List("Cricket","Movies"),Map("hair"->"black","eye"->"brown")),
    Row(Row("Michael ","Rose",""),List("Tennis"),Map("hair"->"brown","eye"->"black")),
    Row(Row("Robert ","","Williams"),List("Cooking","Football"),Map("hair"->"red","eye"->"gray")),
    Row(Row("Maria ","Anne","Jones"),null,Map("hair"->"blond","eye"->"red")),
    Row(Row("Jen","Mary","Brown"),List("Blogging"),Map("white"->"black","eye"->"black"))
  )
  // ArrayType and MapType for column
  val arrayStructureSchema = new StructType()
    .add("name",new StructType()
      .add("firstname",StringType)
      .add("middlename",StringType)
      .add("lastname",StringType))
    .add("hobbies", ArrayType(StringType))
    .add("properties", MapType(StringType,StringType))

  val df5 = spark.createDataFrame(
    spark.sparkContext.parallelize(arrayStructureData),arrayStructureSchema)
  df5.printSchema()
  df5.show()

}
