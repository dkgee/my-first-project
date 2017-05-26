package com.tank.ch07

import com.tank.ch04.ChecksumAccumulator

/**
  * Created by JinHuaTao on 2017/3/21.
  */
object ChecksumAccumulatorTest {
  def main(args: Array[String]) {
    val acc = new ChecksumAccumulator();
    println(acc.checksum());
  }
}
