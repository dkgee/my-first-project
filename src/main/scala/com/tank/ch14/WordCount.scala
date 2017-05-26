package com.tank.ch14

import org.apache.spark.SparkContext


/**
  * Created by JinHuaTao on 2017/5/15.
  */
object WordCount {
  def main(args: Array[String]) {
    val inputPath = args(0)
    val outputPath = args(1)
    val sc = new SparkContext()
    val lines = sc.textFile(inputPath)
    val wordCounts = lines.flatMap{ line => line.split(" ") }
      .map( word => (word, 1))
      .reduceByKey(_+_)
    wordCounts.saveAsTextFile(outputPath)
  }
}
