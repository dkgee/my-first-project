package com.tank.ch12.heap

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/3
  * Time: 16:50
  */
trait Filtering extends IntQueue{
  abstract override def put(x: Int): Unit = {
    if( x >= 0) super.put(x)
  }
}
