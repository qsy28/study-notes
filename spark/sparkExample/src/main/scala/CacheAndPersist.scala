import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel

/**
 * Using cache() and persist() methods,
 * Spark provides an optimization mechanism to store the intermediate computation of a Spark DataFrame
 * so they can be reused in subsequent actions.
 *
 * The advantages of using Spark Cache and Persist methods.
 *
 * Cost efficient – Spark computations are very expensive hence reusing the computations are used to save cost.
 * Time efficient – Reusing the repeated computations saves lots of time.
 * Execution time – Saves execution time of the job and we can perform more jobs on the same cluster.
 */

object CacheAndPersist extends App{

  val spark:SparkSession = SparkSession.builder()
    .master("local[1]")
    .appName("SparkByExamples.com")
    .getOrCreate()

  //read csv with options
  val df = spark.read.options(Map("inferSchema"->"true","delimiter"->",","header"->"true"))
    .csv("src/main/resources/zipcodes.csv")

  // cache()
  val df2 = df.where(col("State") === "PR").cache()
  df2.show(false)
  println(df2.count())
  val df3 = df2.where(col("Zipcode") === 704)
  println(df2.count())

  // persist()
  val dfPersist = df.persist()
  dfPersist.show(false)
  val dfPersist1 = df.persist(StorageLevel.MEMORY_ONLY)
  dfPersist.show(false)

  // unpersist()
  // unpersist() marks the Dataset as non-persistent, and remove all blocks for it from memory and disk.
  val dfPersist2 = dfPersist.unpersist()

}

/**
 * Spark Persist storage levels
 * MEMORY_ONLY
 * MEMORY_ONLY_SER
 * MEMORY_ONLY_2
 * MEMORY_ONLY_SER_2
 * MEMORY_AND_DISK
 * MEMORY_AND_DISK_SER
 * MEMORY_AND_DISK_2
 * MEMORY_AND_DISK_SER_2
 * DISK_ONLY
 * DISK_ONLY_2
 */
