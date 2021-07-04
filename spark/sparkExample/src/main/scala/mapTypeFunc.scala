import org.apache.spark.sql.{Row, SparkSession, Column}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

import scala.collection.mutable

object mapTypeFunc extends App {

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  // Creating MapType map column on Spark DataFrame
  val arrayStructureData = Seq(
    Row("James",List(Row("Newark","NY"),Row("Brooklyn","NY")),
      Map("hair"->"black","eye"->"brown"), Map("height"->"5.9")),
    Row("Michael",List(Row("SanJose","CA"),Row("Sandiago","CA")),
      Map("hair"->"brown","eye"->"black"),Map("height"->"6")),
    Row("Robert",List(Row("LasVegas","NV")),
      Map("hair"->"red","eye"->"gray"),Map("height"->"6.3")),
    Row("Maria",null,Map("hair"->"blond","eye"->"red"),
      Map("height"->"5.6")),
    Row("Jen",List(Row("LAX","CA"),Row("Orange","CA")),
      Map("white"->"black","eye"->"black"),Map("height"->"5.2"))
  )

  val mapType  = DataTypes.createMapType(StringType,StringType)

  val arrayStructureSchema = new StructType()
    .add("name",StringType)
    .add("addresses", ArrayType(new StructType()
      .add("city",StringType)
      .add("state",StringType)))
    // notice the two ways to create mapType
    .add("properties", mapType)
    .add("secondProp", MapType(StringType,StringType))

  val mapTypeDF = spark.createDataFrame(
    spark.sparkContext.parallelize(arrayStructureData),arrayStructureSchema)
  mapTypeDF.show()

  // map_keys(): to retrieve all keys from a Spark DataFrame MapType column.
  mapTypeDF.select(col("name"),map_keys(col("properties"))).show(false)
  // map_values()
  mapTypeDF.select(col("name"),map_values(col("properties"))).show(false)
  // 以下这几个函数 org.apache.spark.sql.functions package 中加载不出？？？
  // map_concat(): to merge keys and values from more than one map to a single map.
  // mapTypeDF.select(col("name"),map_concat(col("properties"),col("secondProp"))).show(false)
  // map_from_entries(): to convert array of StructType entries to map (MapType) on Spark DataFrame
  // map_entries(): to convert map of StructType to array of Structype (struct) column on DataFrame.

  val structureData = Seq(
    Row("36636","Finance",Row(3000,"USA")),
    Row("40288","Finance",Row(5000,"IND")),
    Row("42114","Sales",Row(3900,"USA")),
    Row("39192","Marketing",Row(2500,"CAN")),
    Row("34534","Sales",Row(6500,"USA"))
  )

  val structureSchema = new StructType()
    .add("id",StringType)
    .add("dept",StringType)
    .add("properties",new StructType()
      .add("salary",IntegerType)
      .add("location",StringType)
    )

  var df = spark.createDataFrame(
    spark.sparkContext.parallelize(structureData),structureSchema)
  df.show()

  val index = df.schema.fieldIndex("properties")
  println(index)
  println(df.schema(index))
  println(df.schema(index).dataType)
  // p.isInstanceOf[XX] 判断 p 是否为 XX 对象的实例；p.asInstanceOf[XX] 把 p 转换成 XX 对象的实例
  val propSchema = df.schema(index).dataType.asInstanceOf[StructType]
  println(propSchema)
  // set一般是无序的，但 LinkedHashSet 对插入的顺序有着严格的要求
  var columns = mutable.LinkedHashSet[Column]()
  println(columns)
  propSchema.fields.foreach(field =>{
    columns.add(lit(field.name))
    columns.add(col("properties." + field.name))

  })
  println(columns)
  println(map(columns.toSeq:_*))
  df = df.withColumn("propertiesMap",map(columns.toSeq:_*))
  df = df.drop("properties")
  df.show(false)
}
