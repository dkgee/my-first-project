package com.tank.ch14

import org.apache.spark.util.random
import org.apache.spark.{SparkContext, SparkConf}

import scala.util.Random

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/8
  * Time: 15:16
  */
object SparkApp {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark Pi")
    val spark = new SparkContext(conf)
    val random = new Random()
    val slices = if(args.length > 0) args(0).toInt else 2
    val n = 100000 * slices
    val count = spark.parallelize(1 to n, slices).map { i =>
      val x = random.nextInt(100000) * 2 - 1
      val y = random.nextInt(100000) * 2 - 1
      if(x * x + y * y < 1) 1 else 0
    }.reduce(_ + _)
    print("Pi is roughly " + 4.0 * count / n)
    spark.stop()
  }

}
