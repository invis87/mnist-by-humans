package com.pronvis.mnist_by_humans.mnist

import java.awt.image.BufferedImage
import java.io.File
import java.util.{Timer, TimerTask}

import com.pronvis.mnist_by_humans.db.Entities.ImageData
import com.pronvis.mnist_by_humans.db.{ImageLabel, ImagesDataDao, Schema}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MnistToDB extends LazyLogging {

  def main(args: Array[String]) {
    val config = ConfigFactory.load().getConfig(s"mnist")

    val job = config.getString("whatToDo")
    job match {
      case "save labels in database"        => labelsRelated(config)
      case "parse images file to directory" => imagesRelated(config)
      case any                              => logger.error(s"Wrong job name: $any")
    }
  }

  private def imagesRelated(config: Config) = {
    val imgPath = config.getString("imagesParsing.imagesFilePath")
    val storeDir = config.getString("imagesParsing.storeDirectory")

    val imagesFile = new File(imgPath)
    val directoryForImages = new File(storeDir)
    MnistConverter.imagesFileToPng(imagesFile, directoryForImages, BufferedImage.TYPE_BYTE_GRAY)
  }

  private def labelsRelated(config: Config) = {
    val dbConfig = config.getConfig("db")
    val schema = new Schema(dbConfig)
    val imagesDataDao = new ImagesDataDao(schema)

    val isTrain = config.getBoolean("labelsParsing.isTrainData")
    val mnistLabelsFile = new File(config.getString("labelsParsing.labelsFilePath"))
    val labels: Array[ImageLabel] = MnistConverter.labelsFileToArray(mnistLabelsFile, BufferedImage.TYPE_BYTE_GRAY)

    val imagesData = labels.zipWithIndex.map(x => ImageData(s"$mnistLabelsFile/${ x._2 }.png", x._1, isTrain))
    val insertFuture = imagesDataDao.insertImages(imagesData.toList)


    val timer = new Timer("loggerTimer", false)
    timer.schedule(new TimerTask {
      def run() = logger.debug(".")
    }, 1000)
    Await.result(insertFuture, Duration.Inf)
  }

}
