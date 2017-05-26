package com.tank.ch12.heap

import scala.collection.mutable.ArrayBuffer

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/3
  * Time: 16:31
  */
class BasicIntQueue extends IntQueue{

  private val buf = new ArrayBuffer[Int]()

  override def get(): Int = buf.remove(0)

  override def put(x: Int): Unit ={
    buf += x
  }
}
