package com.tank.ch10

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/3
  * Time: 9:46
  */
class UniformElement(
                    ch: Char,
                    override val width: Int,
                    override val height: Int
                    ) extends Element{
  private val line = ch.toString * width
  def contents  = Array.fill(height)(line)
}
