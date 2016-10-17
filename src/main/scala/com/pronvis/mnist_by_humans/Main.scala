package com.pronvis.mnist_by_humans

import java.awt.image.BufferedImage
import java.io.File

import com.pronvis.mnist_by_humans.db.{ImageLabel, ImagesDataDao, Schema}
import com.typesafe.config.ConfigFactory

//import com.pronvis.mnist_by_humans.db.Context.context._
import com.pronvis.mnist_by_humans.db.Entities.ImageData
import com.pronvis.mnist_by_humans.mnist.MnistConverter
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object Main {

  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  def processMnistFiles() {
//
//    val mnistImagesFile = new File("/Users/pronvis/Downloads/train-images-idx3-ubyte")
//    val directoryForImages = new File("/Users/pronvis/scala/mnist_by_humans/mnist_files/train-images")
//    MnistConverter.imagesFileToPng(mnistImagesFile, directoryForImages, BufferedImage.TYPE_BYTE_GRAY)

    val mnistLabelsFile = new File("/Users/pronvis/Downloads/train-labels-idx1-ubyte")
    MnistConverter.labelsFileToArray(mnistLabelsFile, BufferedImage.TYPE_BYTE_GRAY)

    //to read labels
    //    MnistReader.readFile("/Users/pronvis/Downloads/train-labels-idx1-ubyte")
  }

  def main_imageStoring(args: Array[String]) {
    val mnistImagesFile = new File("/Users/pronvis/Downloads/t10k-images-idx3-ubyte")
    val directoryForImages = new File("/Users/pronvis/scala/mnist_by_humans/mnist_files/test-images")
    MnistConverter.imagesFileToPng(mnistImagesFile, directoryForImages, BufferedImage.TYPE_BYTE_GRAY)
  }

  def main(args: Array[String]) {
    val dbConfig = ConfigFactory.load().getConfig(s"db.test")
    val schema = new Schema(dbConfig)
    val imagesDataDao = new ImagesDataDao(schema)


    val mnistLabelsFile = new File("/Users/pronvis/Downloads/t10k-labels-idx1-ubyte")
    val labels: Array[ImageLabel] = MnistConverter.labelsFileToArray(mnistLabelsFile, BufferedImage.TYPE_BYTE_GRAY)

    val imagesData = labels.zipWithIndex.map(x => ImageData(s"mnist_files/test-images/${x._2}.png", x._1, false))
    val x1 = imagesDataDao.insertImages(imagesData.toList)


    val x1Res = Await.result(x1, Duration.Inf)
  }
}