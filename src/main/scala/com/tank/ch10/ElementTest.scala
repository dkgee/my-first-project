package com.tank.ch10

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/2
  * Time: 18:07
  */
object ElementTest {

  def main(args: Array[String]) {
   /* val ae = new ArrayElement(Array("hello", "world"))
    print(ae.width)*/

    invokeDemo(new LineElement("eee"))
  }

  def invokeDemo(e: Element){
    e.demo()
  }

}
