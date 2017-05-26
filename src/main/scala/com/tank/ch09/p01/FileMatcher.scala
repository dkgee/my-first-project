package com.tank.ch09.p01

import java.io.{PrintWriter, File}
import java.util.Date

/**
  * Created by JinHuaTao on 2017/3/22.
  */
object FileMatcher {
  private def filesHere = (new File(".")).listFiles()

 /* def filesEnding(query: String) =
      for(file <- filesHere; if file.getName.endsWith(query))
        yield file    //返回文件*/

  /*def filesContaining(query: String) =
    for(file <- filesHere; if file.getName.contains(query))
      yield file*/

 /* def filesRegex(query: String) =
    for(file <- filesHere; if file.getName.matches(query))
      yield file*/

  def filesMatching(matcher: String => Boolean ) =
    for(file <- filesHere; if matcher(file.getName))
      yield file

  def filesEnding(query: String) =
    filesMatching(_.endsWith(query))

  def filesContaining(query: String) =
    filesMatching( _.contains(query))

  def filesRegex(query: String) =
    filesMatching( _.matches(query))

  def numberNeg(myList: List[Int]): Boolean ={
    var exist = false
    for(num <- myList)
      if(num < 0)
        exist = true
    exist
  }

  def containOdd(nums: List[Int]): Boolean = nums.exists(_ % 2 == 1)

  def plainOldSum(x: Int, y: Int) = x + y;

  def curriedSum(x: Int)(y: Int) = x + y;

  def first(x: Int) = (y: Int) => x + y;

  //调用第一个函数并传入1，会产生第二个函数
  def second = first(1)

  //重复一个操作两次，返回结果
  def twice(op: Double => Double, x: Double) = op(op(x))

  def withPrintWriter(file: File)(op: PrintWriter => Unit): Unit ={
    val writer = new PrintWriter(file)
    try {
        op(writer)
    }finally {
      writer.close()
    }
  }

  def test(): Unit ={
    /*withPrintWriter(
      new File("date.txt"),
      writer => writer.println(new Date())
    )*/
    val file = new File("date2.txt")
    withPrintWriter(file){
      writer => writer.println(new Date())
    }
  }


  def main(args: Array[String]) {
    test()
  }
}
