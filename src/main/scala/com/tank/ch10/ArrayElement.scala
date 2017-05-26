package com.tank.ch10

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/2
  * Time: 18:02
  */
class ArrayElement(x123: Array[String]) extends Element{
  /**
    * 返回字符串数组
    **/
  override def contents: Array[String] = x123

  override def demo(){
      println("ArrayElement's implementation invoked")
  }


}
