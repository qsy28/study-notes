import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.functions._

/** content
 *  1. withcolumn:To create a new column
 *  2. cast: Change Column Data Type
 *  3. createOrReplaceTempView() & spark.sql
 *  4. withColumnRenamed
 *  5. drop()
 *  6. Split Column into Multiple Columns
 */
object columnsOp extends App {

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  val data = Seq(Row(Row("James;","","Smith"),"36636","M","3000"),
    Row(Row("Michael","Rose",""),"40288","M","4000"),
    Row(Row("Robert","","Williams"),"42114","M","4000"),
    Row(Row("Maria","Anne","Jones"),"39192","F","4000"),
    Row(Row("Jen","Mary","Brown"),"","F","-1")
  )

  val schema = new StructType()
    .add("name",new StructType()
      .add("firstname",StringType)
      .add("middlename",StringType)
      .add("lastname",StringType))
    .add("dob",StringType)
    .add("gender",StringType)
    .add("salary",StringType)

  val df = spark.createDataFrame(spark.sparkContext.parallelize(data),schema)

  // 1. withcolumn
  // To create a new column, pass your desired column name to the first argument of withColumn() transformation function.
  // if it presents it updates the value of the column.

  import org.apache.spark.sql.functions.lit
  //  lit() function is used to add a constant value
  //
  val df1 = df.withColumn("Country", lit("China"))
    .withColumn("City", lit("BeiJing"))
  df1.show()

  // Derive New Column From an Existing Column
  import org.apache.spark.sql.functions.col
  val df2 = df.withColumn("CopiedColumn",col("salary")* -1)
  df2.show()


  // 2. cast: Change Column Data Type
  val df3 = df.withColumn("salary",col("salary").cast("Integer"))
  df3.printSchema()
  df3.show()


  // 3. Add, Replace, or Update multiple Columns
  // When you wanted to add, replace or update multiple columns in Spark DataFrame, it is not suggestible to chain withColumn() function as it leads into performance issue and recommends to use select() after creating a temporary view on DataFrame
  df.createOrReplaceTempView("PERSON")
  val df4 = spark.sql("SELECT salary*100 as salary, salary*-1 as CopiedColumn, 'USA' as country FROM PERSON")
  df4.show()


  // 4. Rename Column Name: withColumnRenamed
  val df5 = df.withColumnRenamed("gender","sex")
    .withColumnRenamed("dob","DateOfBirth")
  df5.printSchema()

  // To rename a nested column
  // by using Spark StructType
  val schema2 = new StructType()
    .add("fname",StringType)
    .add("middlename",StringType)
    .add("lname",StringType)
  df.select(col("name").cast(schema2),
    col("dob"),
    col("gender"),
    col("salary"))
    .printSchema()
  // by using select , you can also use withColumn
  val df_ = df.select(col("name.firstname").as("fname"),
    col("name.middlename").as("mname"),
    col("name.lastname").as("lname"),
    col("dob"),col("gender"),col("salary"))
  df_.printSchema()
  // To change all columns name
  // Another way to change all column names on Dataframe is to use col() function.
  val old_columns = Seq("dob","gender","salary","fname","mname","lname")
  val new_columns = Seq("DateOfBirth","Sex","salary","firstName","middleName","lastName")
  val columnsList = old_columns.zip(new_columns).map(f=>{col(f._1).as(f._2)})
  println(columnsList)
  val newdf = df_.select(columnsList:_*)
  newdf.printSchema()
  // When we have data in a flat structure (without nested) , use toDF() with a new schema to change all column names.
  val newColumns = Seq("firstName","middleName","lastName","DateOfBirth","Sex","salary")
  val df_1 = df_.toDF(newColumns:_*)
  df_1.printSchema()


  // 5. Drop Columns
  val df6 = df2.drop("CopiedColumn")
  // df2.drop(col("CopiedColumn"))
  df6.show()
  val df7 = df2.drop("CopiedColumn","salary","dob")
  df7.show()
  val dropcolname = Seq("CopiedColumn","salary","dob")
  df2.drop(dropcolname:_*).show()


  // 6. Split Column into Multiple Columns
  import spark.implicits._
  val columns = Seq("name","address")
  val data1 = Seq(("Robert, Smith", "1 Main st, Newark, NJ, 92537"),
    ("Maria, Garcia","3456 Walnut st, Newark, NJ, 94732"))

  var dfFromData = spark.createDataFrame(data1).toDF(columns:_*)
  dfFromData.printSchema()
  // map + spilt 方式
  // getAs method which can use both names and indices:
  val newDF = dfFromData.map(f=>{
    val nameSplit = f.getAs[String](0).split(",")
    val addSplit = f.getAs[String]("address").split(",")
    (nameSplit(0),nameSplit(1),addSplit(0),addSplit(1),addSplit(2),addSplit(3))
  })
  val finalDF = newDF.toDF("First Name","Last Name", "Address Line1","City","State","zipCode")
  finalDF.printSchema()
  finalDF.show(false)

  // select + split 方式
  val newDF2 = dfFromData.select(split(col("name"),",").getItem(0).as("FirstName"),
    split(col("name"),",").getItem(1).as("LastName"),
    split(col("address"),",").getItem(0).as("Address Line1"),
    split(col("address"),",").getItem(1).as("City"),
    split(col("address"),",").getItem(2).as("State"),
    split(col("address"),",").getItem(3).as("zipCode"))
    .drop("name")
    .drop("address")
  newDF2.show()

  // withColumn + split 方式
  val newDF3 = dfFromData.withColumn("FirstName",split(col("name"),",").getItem(0))
    .withColumn("LastName", split(col("name"),",").getItem(1))
    .withColumn("Address Line1", split(col("address"),",").getItem(0))
    .withColumn("City", split(col("address"),",").getItem(1))
    .withColumn("State", split(col("address"),",").getItem(2))
    .withColumn("zipCode", split(col("address"),",").getItem(3) )
    .drop("name")
    .drop("address")
  newDF3.show()

  // using raw Spark SQL
  dfFromData.createOrReplaceTempView("PERSON_2")
  val newDF4 = spark.sql("select SPLIT(name,',') as NameArray , SPLIT(address, ',') as AddressArray from PERSON_2")
    .show(false)



}
