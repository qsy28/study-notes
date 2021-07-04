import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._


object DateAndTimestampFunc extends App {

  val spark:SparkSession = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()
  import spark.implicits._

  // Date Functions
  val data = Seq( ("2019-01-23"),("2021-01-01"))
  val rdd = spark.sparkContext.parallelize(data)
  val df = rdd.toDF("Input")
  df.show()

  // current_date() 返回当前日期
  // date_format(col, format) 将日期按format格式化
  df.select(
      current_date()as("current_date"),
      col("Input"),
      date_format(col("Input"), "MM-dd-yyyy").as("format")
    ).show()

  // to_date(col, fmt) 将具有fmt格式的col列转换成date类型
  val data1 = Seq( (20210601,"2021-06-01", "20210601"),(20210630,"2021-06-30","20210630"))
  val rdd1 = spark.sparkContext.parallelize(data1)
  val df1 = rdd1.toDF("date_int","date_str","date_str_no_")
  df1.show()
  df1.printSchema()

  val df2 = df1.select(to_date(col("date_int").cast(StringType), "yyyyMMdd").as("format1"),
   to_date(col("date_str"),"yyyy-MM-dd" ).as("format2"))
  df2.show()
  df2.printSchema()

  // 日期的运算，会在函数内部先进行转时间戳的计算，所以输入的数据格式可以为："yyyy-MM-dd"的字符串以及date类型
  // trunc() 返回某年的第一天或者某月的第一天
  df1.select(col("date_str"),
    trunc(col("date_str"),"Month").as("Month_trunc"),
    trunc(col("date_str"),"Year").as("Month_trunc"))
    .show()

  // 日期的一些基本运算
  df1.select(current_date().as("current_date"), col("date_str"),
    datediff(current_date(), col("date_str")).as("diff"),
    months_between(current_date(),col("date_str")).as("months_between"),
    add_months(col("date_str"),-3).as("add_months"),
    date_add(col("date_str"),-4).as("date_add"),
    year(col("date_str")).as("year"),
    month(col("date_str")).as("month"),
    dayofweek(col("date_str")).as("dayofweek"),
    dayofmonth(col("date_str")).as("dayofmonth"),
    dayofyear(col("date_str")).as("dayofyear"),
    next_day(col("date_str"),"Sunday").as("next_day"),
    weekofyear(col("date_str")).as("weekofyear") ).show()

  // unix_timestamp(col, fmt) 将某种格式的日期转换成unix时间戳，col可以为字符串以及date类型
  val df3 = df1.select(col("date_int"), col("date_str"), col("date_str_no_"),
    unix_timestamp(col("date_str"),"yyyy-MM-dd").as("timestamp"),
    unix_timestamp(col("date_str_no_"),"yyyyMMdd"))
  df3.show()
  df3.printSchema()
  // from_unixtime(col,fmt) 将unix时间戳转换成指定格式的日期
  df3.select(col("timestamp"),
    from_unixtime(col("timestamp")).as("date1"),
    from_unixtime(col("timestamp"),"yyyyMMdd").as("date1"),
    from_unixtime(col("timestamp"),"yyyy-MM-dd").as("date1")).show()

  // Timestamp Functions
  // Timestamp 时间格式： 2019-11-16 21:00:55.349；unix_timestamp 是一段数字
  // current_timestamp()
  // to_timestamp()
  // hour(), Minute() and second()

}
