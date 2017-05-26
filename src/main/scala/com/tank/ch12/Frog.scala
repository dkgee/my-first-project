package com.tank.ch12

/**
  * Description:
  * User： JinHuaTao 
  * Date：2017/5/3
  * Time: 15:03
  */
class Frog extends Animal with Philosophical with HasLegs{
  override def toString: String = "green"

  override def philosophize(): Unit ={
    println("It ain't easy being " + toString + "!")
  }
}

class Animal

trait HasLegs
