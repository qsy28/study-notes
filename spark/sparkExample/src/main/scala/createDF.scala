import com.databricks.spark.xml.XmlDataFrameReader
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.Row

import scala.collection.JavaConversions._
import scala.collection.immutable.Map

/**
 * https://sparkbyexamples.com/
 */
object createDF extends App {

    val spark = SparkSession.builder().master("local[1]").appName("SparkByExamples.com").getOrCreate()
    import spark.implicits._

    val columns = Seq("language","users_count")
    val data = Seq(("Java", "20000"), ("Python", "100000"), ("Scala", "3000"))

    println("1. rdd to df")
    // create RDD
    val rdd = spark.sparkContext.parallelize(data)
    rdd.collect().foreach {println}

    // RDD to DataFrame
    val dfFromRDD1 = rdd.toDF()
    // By default, it creates column names as “_1” and “_2” as we have two columns for each row.
    dfFromRDD1.printSchema()

    // give the column names
    val dfFromRDD2 = rdd.toDF("language","users_count")
    dfFromRDD2.printSchema()

    // crate df by createDataFrame()
    val dfFromRDD3 = spark.createDataFrame(rdd).toDF(columns:_*)
    dfFromRDD3.printSchema()

    // Using createDataFrame() with the Row type
    val schema = StructType( Array(
      StructField("language", StringType,true),
      StructField("language", StringType,true)
    ))
    val rowRDD = rdd.map(attributes => Row(attributes._1, attributes._2))
    rowRDD.collect().foreach {println}
    val dfFromRDD4 = spark.createDataFrame(rowRDD,schema)

    println("2. list or seq collection to df")
    // Using toDF()
    val dfFromData1 = data.toDF()
    // Using createDataFrame()
    var dfFromData2 = spark.createDataFrame(data).toDF(columns:_*)
    // Using createDataFrame() with the Row type: we need to import scala.collection.JavaConversions._
    val rowData= Seq(Row("Java", "20000"),
      Row("Python", "100000"),
      Row("Scala", "3000"))
    var dfFromData3 = spark.createDataFrame(rowData,schema)

    println("3. create df from csv/txt/json")
    val optionMap = Map[String, String](
      "header" -> "true",
      "delimiter" -> ",",
      "inferSchema" -> "true"
    )

    val df2 = spark.read
      .format("csv")
      .options(optionMap)
      .load("./target/classes/test_data.csv")
    df2.show()

    val df3 = spark.read
      .format("text")
      .load("./target/classes/test.txt")
    df3.show()

    val df3_ = spark.read.text("./target/classes/test.txt")
    df3_.show()
    val df4 = spark.read
      .format("json")
      .option("multiline","true")
      .load("./target/classes/test1.json")
    df4.printSchema()
    df4.show(false)

    println("4. create df from xml")
    // we should use DataSource "com.databricks.spark.xml" spark-xml api from Databricks.
    val df5 = spark.read
      .format("com.databricks.spark.xml")
      .option("rowTag", "person")
      .xml("./target/classes/test2.xml")
    df5.printSchema()
    df5.show()

    println("5. create df from tdw")
    // 参见weihua代码


}
