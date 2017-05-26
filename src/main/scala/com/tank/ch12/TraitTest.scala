package com.tank.ch12

import com.tank.ch12.heap._

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/3
  * Time: 15:05
  */
object TraitTest {

  def main(args: Array[String]) {
//    test03()
      test05()
  }

  def test01(): Unit ={
    val frog = new Frog
    frog.philosophize()
    val phil: Philosophical = frog
    phil.philosophize()
  }

  def test02(): Unit ={
    val queue = new BasicIntQueue
    queue.put(10)
    queue.put(20)
    println(queue.get())
    println(queue.get())
  }

  def test03(): Unit ={
    val queue = new MyQueue
    queue.put(10)
    println(queue.get())

    val queue2 = new BasicIntQueue with Doubling
    queue2.put(10)
    println(queue2.get())
  }

  def test04(): Unit ={
    //先调用Filtering，再调用Incrementing，调用次序从右向左
    val queue = (new BasicIntQueue with Incrementing with Filtering)
    queue.put(-1);queue.put(0);queue.put(1)
    println(queue.get())
    println(queue.get())
   // println(queue.get())
  }

  def test05(): Unit ={
    val queue = (new BasicIntQueue with Filtering with Incrementing)
    queue.put(-1);queue.put(0);queue.put(1)
    println(queue.get())
    println(queue.get())
    println(queue.get())
  }
}
