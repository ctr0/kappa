package spark.mock

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Driver {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
      .setAppName("SparkMock")

    val ssc = new StreamingContext(
      sparkConf,
      Seconds(2)
    )

    val lines = ssc.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" "))

    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    // Print the first ten elements of each RDD generated in this DStream to the console
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()

  }
}