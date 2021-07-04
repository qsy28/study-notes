import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.DataTypes


object foreachPartitionVSforeach extends App {

  val spark = SparkSession.builder
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  val Data = Seq(("James","Sales","NY",0),
    ("Michael","Sales","NY",1),
    ("Robert","Sales","CA",2),
    ("Maria","Finance","CA",3)
  )

  val df = spark.createDataFrame(Data).toDF("name","ways","city", "num")
  df.show()

  df.foreachPartition(partition => {
    partition.foreach(println)
  })

  // longAccumulator 内置累加器
  val longAcc = spark.sparkContext.longAccumulator("SumAccumulator")
  df.foreach(f=> {
    longAcc.add(f.getInt(3))
  })
  println("Accumulator value:"+longAcc.value)

  val rdd = spark.sparkContext.parallelize(Seq(1,2,3,4,5,6,7,8,9))
  rdd.foreach(println)

}
