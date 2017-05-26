package com.tank.ch12.heap

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/3
  * Time: 16:49
  */
trait Incrementing extends IntQueue{
  abstract override def put(x: Int): Unit = {
      super.put(x + 1)
  }
}
