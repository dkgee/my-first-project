package com.tank.ch14

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by JinHuaTao on 2017/5/14.
  */
object DataAnalytics {
  def main(args: Array[String]) {
    sample()
  }

  def sample(): Unit = {
    // 设置Spark集群主节点和Spark应用程序名称
    val config = new SparkConf().setMaster("spark://hadoop1:7077").setAppName("dataAanlytics")
    // 1，创建Spark的入口点 SparkContext
    val sc = new SparkContext(config)

    // 2，创建弹性分布式数据集RDD
    //2.1 parallelize
//    val xs = (1 to 10000).toList
//    val rdd1 = sc.parallelize(xs)

    //2.2 textFile
//    val rdd2 = sc.textFile("hdfs://hadoop1:9000/path/to/file-to-directory")
//    val rdd3 = sc.textFile("hdfs://hadoop1:9000/path/to/directory/*.gz")

    //2.3 wholeTextFiles   可以读取本地文件系统的目录
//    val rdd4 = sc.wholeTextFiles("path/to/my-data/*.txt")

    //2.4 sequenceFile
//    val rdd5 = sc.sequenceFile[String, String]("some-file")

    //3, RDD数据转换(transformations)操作
    //3.1 map
//    val lines = sc.textFile("...")
//    val lengths = lines map { l => l.length }

    //3.2 filter
//    val longLines = lines filter { l => l.length > 80 }

    //3.3 flatMap
//    val words = lines flatMap { l => l.split(" ") }

    //3.4 Partitions 级别函数
//    val lengths2 = lines mapPartitions { iter => iter.map { l => l.length } }

    //3.5 union函数
//    val linesFile1 = sc.textFile("...")
//    val linesFile2 = sc.textFile("...")
//    val linesFromBothFiles = linesFile1.union(linesFile2)

    //3.6 intersection
//    val linesPresentInBothFiles = linesFile1.intersection(linesFile2)

//    val mammals = sc.parallelize(List("Lion", "Dolphin", "Whale"))
//    val aquatics = sc.parallelize(List("Shark", "Dolphin", "Whale"))
//    val aquaticMammals = mammals.intersection(aquatics)

    //3.7 subtract
//    val linesInFile1Only = linesFile1.subtract(linesFile2)

//    val fishes = aquatics.subtract(mammals)

    //3.8 distinct
//    val numbers = sc.parallelize(List(1, 2, 3, 4, 5, 3, 2, 1))
//    val uniqueNumbers = numbers.distinct

    //3.9 cartesian
//    val numbers2 = sc.parallelize(List(1, 2, 3, 4))
//    val alphabets = sc.parallelize(List("a", "b", "c", "d"))
//    val cartesianProduct = numbers2.cartesian(alphabets)

    //3.10 zip
//    val zippedPairs = numbers.zip(alphabets)

    //3.11 zipWithIndex -----
    //val alphabetsWithIndex = alphabets.zip

    //3.12 groupBy
    //As next

    //3.13 keyBy
//    val sorted = numbers2.sortBy(x => x, true)

    //3.14 pipe

    //3.15 randomSplit
//    val numbers3 = sc.parallelize((1 to 100).toList)
//    val splits = numbers3.randomSplit(Array(0.6, 0.2, 0.2))

    //3.16 coalesce
//    val numbersWithOnePartition = numbers3.coalesce(1)

    //3.17 repartition
//    val numbersWithOnePartition2 = numbers3.repartition(4)

    //3.18 sample
//    val sampleNumbers = numbers3.sample(true, 0.2)

    //4,RDD的 key-value转换
    //4.1 keys
//    val kvRdd = sc.parallelize(List(("a", 1), ("b", 2), ("c", 3)))
//    val keysRdd = kvRdd.keys

    //4.2 values
//    val valuesRdd = kvRdd.values

    //4.3 mapValues
//    val valuesDoubled = kvRdd mapValues { x => 2 * x }

    //4.4 join
//    val pairRdd1 = sc.parallelize(List(("a", 1), ("b", 2), ("c", 3)))
//    val pairRdd2 = sc.parallelize(List(("b", "second"), ("c", "third"), ("d", "fourth")))
//    val joinRdd = pairRdd1.join(pairRdd2)

    //4.5 leftOuterJoin
//    val leftOuterJoinRdd = pairRdd1.leftOuterJoin(pairRdd2)

    //4.6 rightOuterJoin
//    val rightOuterJoinRdd = pairRdd1.rightOuterJoin(pairRdd2)

    //4.7 fullOuterJoin
//    val fullOuterJoinRdd = pairRdd1.fullOuterJoin(pairRdd2)

    //4.8 sampleByKey
//    val pairRdd4 = sc.parallelize(List(("a", 1), ("b", 2), ("a", 11), ("b", 22), ("a", 111), ("b", 222)))
//    val sampleRdd = pairRdd4.sampleByKey(true, Map("a" -> 0.1, "b" -> 0.2))

    //4.9 subtractByKey
//    val resultRdd = pairRdd1.subtractByKey(pairRdd2)

    //4.10 groupByKey
//    val pairRdd5 = sc.parallelize(List(("a", 1), ("b", 2), ("c", 3), ("a", 11), ("b", 22), ("a", 111)))
//    val groupRdd = pairRdd5.groupByKey()

    //4.11 reduceByKey
//    val sumByKeyRdd = pairRdd5.reduceByKey((x, y) => x + y)
//    val minByKeyRdd = pairRdd5.reduceByKey((x, y) => if (x < y) x else y)


    //5 Actions
    //5.1 collect
//    val rdd001 = sc.parallelize((1 to 10000).toList)
//    val filteredRdd = rdd001 filter { x => (x % 1000) == 0 }
//    val filterResult = filteredRdd.collect

    //5.2 count
//    val total = rdd001.count

    //5.3 countByValue
//    val rdd002 = sc.parallelize(List(1, 2, 3, 4, 1, 2, 3, 1, 2, 1))
//    val counts = rdd002.countByValue

    //5.4 first
//    val rdd003 = sc.parallelize(List(10, 5, 3, 1))
//    val firstElement = rdd003.first

    //5.5 max
//    val maxElement = rdd003.max

    //5.6 min
//    val minElement = rdd003.min

    //5.7 take
//    val smallest3 = rdd003.takeOrdered(3)

    //5.8 top
//    val largest3 = rdd003.top(3)

    //5.9 fold
//    val numbersRdd3 = sc.parallelize(List(2, 5, 3, 1))
//    val sum0 = numbersRdd3.fold(0)((partialSum, x) => partialSum + x)
//    val product0 = numbersRdd3.fold(1)((partialProduct, x) => partialProduct * x)

    //5.10 reduce
//    val sum1 = numbersRdd3.reduce((x, y) => x + y)
//    val product1 = numbersRdd3.reduce((x, y) => x * y)

    //6, Action 中key-value对
    //6.1 countByKey
//    val pairRdd6 = sc.parallelize(List(("a", 1), ("b", 2), ("c", 3), ("a", 11), ("b", 22), ("c", 11)))
//    val countOfEachKey = pairRdd6.countByKey()

    //6.2 lookup
//    val values = pairRdd6.lookup("a")

    //7, Actions中的数值类型
    //7.1 mean
//    val numbersRdd7 = sc.parallelize(List(2, 5, 3, 1))
//    val mean = numbersRdd7.mean
//
    //7.2 stdev
//    val stdev = numbersRdd7.stdev

    //7.3 sum
//    val sum3 = numbersRdd7.sum

    //7.4 variance
//    val variance = numbersRdd7.variance

    //7 Save RDD
    val numRdd = sc.parallelize((1 to 10000).toList)
    val filterRdd = numRdd filter { x => x % 1000 == 0}
    //7.1 保存为text文件
    filterRdd.saveAsTextFile("numbers-as-text")

    //7.2 保存为Object文件
    filterRdd.saveAsObjectFile("numbers-as-object")

    //7.3 保存为序列化文件
    val pairs = (1 to 10000).toList map { x => (x, x*2)}
    val pairsRdd = sc.parallelize(pairs)
    val filteredPairsRdd = pairsRdd filter { case(x, y) => x % 1000 == 0}
    filteredPairsRdd.saveAsSequenceFile("pairs-as-sequence")
    filteredPairsRdd.saveAsTextFile("pairs-as-text")




  }


}

case class Customer(name: String, age: Int, gender: String, zip: String){
  val sc = new SparkContext()
  val lines = sc.textFile("...")
  val customers = lines map {
      l => {
        val a = l.split(",")
        Customer(a(0), a(1).toInt, a(2), a(3))
      }
  }
  val groupByZip = customers.groupBy{ c => c.zip }
  val keyedByZip = customers.keyBy{ c => c.zip }
  val sortedByAge = customers.sortBy(c => c.age, true)

}