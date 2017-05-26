package com.tank.ch12.heap

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/3
  * Time: 16:38
  */
trait Doubling extends IntQueue{
  abstract override def put(x: Int): Unit ={
    super.put( 2 * x)
  }
}
