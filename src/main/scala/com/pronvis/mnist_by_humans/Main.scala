package com.pronvis.mnist_by_humans

import java.awt.image.BufferedImage
import java.io.File

import com.pronvis.mnist_by_humans.db.Context
import com.pronvis.mnist_by_humans.mnist.MnistConverter
import com.pronvis.mnist_by_humans.db.Context.context._
import com.typesafe.scalalogging.Logger
import io.getquill._
import org.slf4j.LoggerFactory
import scala.concurrent.duration._

import scala.concurrent.Await

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

  def main(args: Array[String]) {

    case class Circle(radius: Float)

    val areas = quote {
      query[Circle].insert(lift(Circle(2.3f)))
    }

    val areas2 = quote {
      query[Circle].insert(lift(Circle(2.4f)))
    }
    val areas3 = quote {
      query[Circle].insert(lift(Circle(2.5f)))
    }
    val areas4 = quote {
      query[Circle].insert(lift(Circle(2.6f)))
    }

    Context.context.run(areas)
    Context.context.run(areas2)
    Context.context.run(areas3)
    Context.context.run(areas4)
    Thread.sleep(2000)

//    logger.debug(x.sum.toString)
  }
}