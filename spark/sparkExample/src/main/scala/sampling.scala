import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}



object sampling extends App{

  val spark = SparkSession.builder() .master("local[1]").appName("SparkByExample")
    .getOrCreate();
  val df=spark.range(100)
  df.show()
  // sample()
  // 参数： withReplacement 放回抽样还是不放回抽样，true表示放回；fraction 采样比例；seed 随机种子
  println(df.sample(0.1).collect().mkString(","))

  println(df.sample(0.1,123).collect().mkString(","))
  println(df.sample(0.1,123).collect().mkString(","))
  println(df.sample(0.1,456).collect().mkString(","))

  println(df.sample(true,0.3,123).collect().mkString(",")) //with Duplicates
  println(df.sample(0.3,123).collect().mkString(",")) // No duplicates

  // sampleBy() 分层抽样
  println(df.stat.sampleBy("id", Map(""->0.1),123).collect().mkString(","))
}
