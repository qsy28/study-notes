import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window


object WindowsFunc extends App{

  val spark:SparkSession = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  import spark.implicits._

  val simpleData = Seq(("James", "Sales", 3000),
    ("Michael", "Sales", 4600),
    ("Robert", "Sales", 4100),
    ("Maria", "Finance", 3000),
    ("James", "Sales", 3000),
    ("Scott", "Finance", 3300),
    ("Jen", "Finance", 3900),
    ("Jeff", "Marketing", 3000),
    ("Kumar", "Marketing", 2000),
    ("Saif", "Sales", 4100)
  )
  val df = simpleData.toDF("employee_name", "department", "salary")
  df.show()

  // 排序
  // row_number() 1,2,3,4,5
  // rank() 1,1,3,4,4
  // dense_rank() 1,1,2,3,3
  // percent_rank()
  // ntile(n) 将分组数据按照顺序切分成n片
  // cume_dist() 窗口分区内值的累积分布：例如小于等于当前薪水的人数，所占总人数的比例
  val windowSpec  = Window.partitionBy("department").orderBy("salary")
  df.withColumn("rank1", row_number().over(windowSpec))
    .withColumn("rank2", rank().over(windowSpec))
    .withColumn("rank3", dense_rank().over(windowSpec))
    .withColumn("rank4",percent_rank().over(windowSpec))
    .withColumn("ntile", ntile(2).over(windowSpec))
    .withColumn("cume_dist", cume_dist().over(windowSpec))
    .show()

  // lag(col, n)
  // lead(col, n)
  df.withColumn("lag", lag("salary",2).over(windowSpec))
    .withColumn("lead",lead("salary",1).over(windowSpec))
    .show()

  // Window Aggregate Functions
  val windowSpecAgg  = Window.partitionBy("department")
  val aggdf = df.withColumn("row",row_number().over(windowSpec))
    .withColumn("avg", avg(col("salary")).over(windowSpecAgg))
    .withColumn("sum", sum(col("salary")).over(windowSpecAgg))
    .withColumn("min", min(col("salary")).over(windowSpecAgg))
    .withColumn("max", max(col("salary")).over(windowSpecAgg))

  aggdf.show()

  aggdf.where(col("row")===1).select("department","avg","sum","min","max")
    .show()





}
