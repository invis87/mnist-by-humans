package com.pronvis.mnist_by_humans.db

import io.getquill.WrappedValue

case class ImageLabel(value: Byte) extends AnyVal with WrappedValue[Byte]

object Entities {

  case class ImageData(id: Long, imagePath: String, label: ImageLabel, trainData: Boolean)
  object ImageData {
    def apply(imagePath: String, label: ImageLabel, trainData: Boolean): ImageData =
      ImageData(0, imagePath, label, trainData)
  }

}
