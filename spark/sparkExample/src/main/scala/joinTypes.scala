import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._
/**
 * joinType: inner, full, left, right, corss, leftsemi, leftanti
 */
object joinTypes extends App{

  val spark = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()


  val emp = Seq((1,"Smith",-1,"2018","10","M",3000),
    (2,"Rose",1,"2010","20","M",4000),
    (3,"Williams",1,"2010","10","M",1000),
    (4,"Jones",2,"2005","10","F",2000),
    (5,"Brown",2,"2010","40","",-1),
    (6,"Brown",2,"2010","50","",-1)
  )
  val empColumns = Seq("emp_id","name","superior_emp_id","year_joined",
    "emp_dept_id","gender","salary")
  import spark.sqlContext.implicits._
  val empDF = emp.toDF(empColumns:_*)
  empDF.show(false)

  val dept = Seq(("Finance",10),
    ("Marketing",20),
    ("Sales",30),
    ("IT",40)
  )

  val deptColumns = Seq("dept_name","dept_id")
  val deptDF = dept.toDF(deptColumns:_*)
  deptDF.show(false)

  empDF.join(deptDF,empDF("emp_dept_id") ===  deptDF("dept_id"),"inner")
    .show(false)

  // Spark Left Semi join is similar to inner join difference being leftsemi join returns all columns from the left DataFrame/Dataset and ignores all columns from the right dataset.
  // 左右并集，但只返回做表得列
  empDF.join(deptDF,empDF("emp_dept_id") ===  deptDF("dept_id"),"leftsemi")
    .show(false)
  //leftanti join returns only columns from the left DataFrame/Dataset for non-matched records.
  // 左边没有匹配上右边的行，列为左表的
  empDF.join(deptDF,empDF("emp_dept_id") ===  deptDF("dept_id"),"leftanti")
    .show(false)

  // self join
  empDF.as("emp1").join(empDF.as("emp2"),
    col("emp1.superior_emp_id") === col("emp2.emp_id"),"inner")
    .select(col("emp1.emp_id"),col("emp1.name"),
      col("emp2.emp_id").as("superior_emp_id"),
      col("emp2.name").as("superior_emp_name"))
    .show(false)

  // also can use SQL expression
  empDF.createOrReplaceTempView("EMP")
  deptDF.createOrReplaceTempView("DEPT")
  val joinDF = spark.sql("select * from EMP e INNER JOIN DEPT d ON e.emp_dept_id == d.dept_id")
  joinDF.show(false)

}
