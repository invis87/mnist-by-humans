package com.pronvis.mnist_by_humans.db

case class Circle(id: Long, radius: Float, color: Int)
object Circle {
  def apply(radius: Float, color: Int): Circle = Circle(0, radius, color)
}
