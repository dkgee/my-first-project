package com.tank.ch10

import  Element.elem

/**
  * Description: 布局元素，抽象类
  * User： JinHuaTao 
  * Date：2017/5/2
  * Time: 17:41
  */
abstract class Element {

  /**
    * 返回字符串数组
    * */
  def contents:Array[String]

  /**
    * 无参方法：返回内容高度
    * */
  def height: Int = contents.length

  /**
    * 无参方法：返回内容宽度，
    * */
  def width: Int = if (height == 0) 0 else contents(0).length

  def demo(): Unit ={
    println("Element's implementation invoked")
  }

  /*def above(that: Element): Element =
    new ArrayElement(this.contents ++ that.contents)*/

 /* def beside(that: Element): Element = {
    val contents = new Array[String](this.contents.length)
    for(i <- 0 until this.contents.length)
      contents(i) = this.contents(i) + that.contents(i)
    new ArrayElement(contents)
  }*/

  def above(that: Element): Element =
    elem(this.contents ++ that.contents)

  def beside(that: Element): Element = {
    val this1 = this.height
    elem(
      for(
        (line1, line2) <- this.contents zip that.contents
      )yield line1 + line2
    )
  }

  def widen(w: Int): Element =
    if(w <= width) this
    else{
      val left = elem(' ', (w - width)/2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    }

  def heighten(h: Int):Element =
    if(h <= height) this
    else{
      val top = elem(' ', width, (h - height) / 2)
      val bot = elem(' ', width, h - height - top.height)
      top above this above bot
    }

  def heighten(h: Char):Unit =
      println(h)


  override def toString: String = contents mkString "\n"
}

object Element{
  //伴生对象声明私有子类
  private class ArrayElement(val contents: Array[String]) extends Element

  private class LineElement(s: String) extends Element{
    val contents = Array(s)
    override def width: Int = s.length
    override def height: Int = 1
  }

  private class UniformElement(ch: Char,
        override val height: Int,
        override val width: Int) extends Element {
     private val line = ch.toString * width
     def contents  = Array.fill(height)(line)
  }

  def elem(contents: Array[String]): Element =
      new ArrayElement(contents)

  def elem(chr: Char, width: Int, height: Int): Element =
      new UniformElement(chr, width, height)

  def elem(line: String): Element =
      new LineElement(line)
}
