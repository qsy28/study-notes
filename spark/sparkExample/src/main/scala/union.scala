import org.apache.spark.sql.{Row, SparkSession}

/**
 * union() 不会去重
 */
object union extends App{

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()
  import spark.implicits._

  val simpleData = Seq(("James","Sales","NY",90000,34,10000),
    ("Michael","Sales","NY",86000,56,20000),
    ("Robert","Sales","CA",81000,30,23000),
    ("Maria","Finance","CA",90000,24,23000)
  )
  val df = simpleData.toDF("employee_name","department","state","salary","age","bonus")

  val simpleData2 = Seq(("James","Sales","NY",90000,34,10000),
    ("Maria","Finance","CA",90000,24,23000),
    ("Jen","Finance","NY",79000,53,15000),
    ("Jeff","Marketing","CA",80000,25,18000),
    ("Kumar","Marketing","NY",91000,50,21000)
  )
  val df2 = simpleData2.toDF("employee_name","department","state","salary","age","bonus")
  // 不会去重
  val df3 = df.union(df2)
  df3.show(false)
  // 去重使用 distinct or dropDuplicates
  val df5 = df.union(df2).distinct()
  df5.show(false)

}
