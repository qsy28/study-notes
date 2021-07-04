import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions.{when, _}

object whenOtherise extends App{

  val spark: SparkSession = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  import spark.sqlContext.implicits._
  val data = List(("James","","Smith","36636","M",60000),
    ("Michael","Rose","","40288","M",70000),
    ("Robert","","Williams","42114","",400000),
    ("Maria","Anne","Jones","39192","F",500000),
    ("Jen","Mary","Brown","","F",0))

  val cols = Seq("first_name","middle_name","last_name","dob","gender","salary")
  val df = spark.createDataFrame(data).toDF(cols:_*)

  val df2 = df.withColumn("new_gender", when(col("gender")==="M",0)
    .when(col("gender")==="F",1)
    .otherwise(null))
  df2.show()

  val df3 = df.select(col("*"), when(col("gender") === "M",0)
    .when(col("gender") === "F",1)
    .otherwise(null).alias("new_gender"))
  df3.show()

  // using "case when"
  val df4 = df.withColumn("new_gender",
    expr("case when gender = 'M' then 0 " +
      "when gender = 'F' then 1 " +
      "else null end"))
  df4.show()


}
