package com.tank.ch04

/**
  * Created by JinHuaTao on 2017/3/21.
  */
class ChecksumAccumulator {
    private var sum = 0;

    def add(b: Byte) { sum += b }
    def checksum():Int = ~(sum & 0xFF) + 1
}

//单例对象 Singleton
object ChecksumAccumulator{
  private val cache = Map[String, Int]()

  def calculate(s: String):Int =
    if (cache.contains(s))
        cache(s)
    else {
      val acc = new ChecksumAccumulator
      for (c <- s)
          acc.add(c.toByte)
      val cs = acc.checksum()
      //cache += (s -> cs)
      cs
    }

  def main(args: Array[String]) {
    ChecksumAccumulator.calculate("Every value is an object.");
  }
}

