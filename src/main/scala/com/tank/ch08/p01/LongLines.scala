package com.tank.ch08.p01

import scala.io.Source

/**
  * Created by JinHuaTao on 2017/3/22.
  */
object LongLines {

  //定义处理文件的函数
  def processFile(filename: String, width: Int): Unit ={
    //定义打印大于指定宽度的行的函数（限定在本函数内使用）
    def processLine(line: String): Unit ={
      if(line.length > width)
        println(filename + ":" + line)
    }

    val source = Source.fromFile(filename);
    for(line <- source.getLines())
       processLine(line)
  }

  def boom(x: Int): Int =
      if(x == 0) throw new Exception("boom!")
      else boom(x - 1)
}
