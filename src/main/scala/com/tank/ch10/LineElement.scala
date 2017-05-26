package com.tank.ch10

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/2
  * Time: 18:28
  */
class LineElement(s: String) extends ArrayElement(Array(s)){
  override def height: Int = 1
  override def width: Int = s.length

  override def demo(): Unit ={
    println("LineElement's implementation invoked")
  }



}
