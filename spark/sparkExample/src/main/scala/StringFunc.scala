import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

/**
 * concat_ws()
 * locate()
 * split(col, "regex")
 * substring()
 * repeat()
 * regexp_extract()
 * regexp_replace()
 */
object StringFunc extends App {

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()


  val arrayData = Seq(
    Row("James","x",List("Java","Scala"),Map("hair"->"black","eye"->"brown")),
    Row("Michael","xx",List("Spark","Java",null),Map("hair"->"brown","eye"->null)),
    Row("Robert","xxx",List("CSharp",""),Map("hair"->"red","eye"->"")),
    Row("Washington","",null,null),
    Row("Jefferson","nnn",List(),Map())
  )

  val Schema = new StructType()
    .add("f_name", StringType)
    .add("l_name", StringType)
    .add("language", ArrayType(StringType))
    .add("Appearance", MapType(StringType,StringType))

  val df = spark.createDataFrame(spark.sparkContext.parallelize(arrayData),Schema)
  df.show()

  // concat_ws()
  // 利用 concat_ws() 将array col 转string col
  df.withColumn("language_str", concat_ws(",", col("language"))).show()
  // 利用 concat_ws() 将多列合并为一列
  df.withColumn("name",concat_ws(".",col("f_name"),col("l_name"))).show()

  // locate()
  // 返回 substr 第一次出现的位置
  df.withColumn("index", locate("e",col("f_name"))).show()
  // 返回 substr 在pos后(包括pos)第一次出现的位置
  df.withColumn("index", locate("e",col("f_name"),4)).show()

  // split(col, "regex")

  // substring(col, pos: Int, len: Int): Column 提取子字符串
  df.withColumn("sub", substring(col("f_name"), 0, 2)).show()
  // repeat(str: Column, n: Int): Column 将列内容重复n遍
  df.withColumn("repeat", repeat(col("l_name"), 3)).show()
  // regexp_extract(e: Column, exp: String, groupIdx: Int): Column

  // regexp_replace(e: Column, pattern: String, replacement: String): Column
}
