package com.pronvis.mnist_by_humans

import java.awt.image.BufferedImage
import java.io.File

import com.pronvis.mnist_by_humans.db.Context.context._
import com.pronvis.mnist_by_humans.db.{Circle, Context}
import com.pronvis.mnist_by_humans.mnist.MnistConverter
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

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

    def insertCircle(circle: Circle) = quote {
      query[Circle].insert(lift(circle)).returning(_.id)
    }

    val x1 = Context.context.run(insertCircle(Circle(235f, 44)))
    val x1Res = Await.result(x1, 4 seconds)
    logger.debug(s"id of inserting new Circle is $x1Res")

    val q = quote(query[Circle])
    val insertResult = Context.context.run(q)

    val x: List[Circle] = Await.result(insertResult, 5 seconds)
    logger.debug(x.map(z => z.radius).mkString("; "))

  }
}