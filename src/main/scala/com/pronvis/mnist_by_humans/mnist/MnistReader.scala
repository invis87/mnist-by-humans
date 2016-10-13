package com.pronvis.mnist_by_humans.mnist

import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO

import org.apache.commons.io.IOUtils

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

object MnistReader {

  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  private def dimensionFromMnist(dimensionNumber: Int, mnistBytes: Array[Byte]): Int = {
    val slice = mnistBytes.slice(4 * (1 + dimensionNumber), (2 + dimensionNumber) * 4)
    logger.debug(s"slice dimension #$dimensionNumber; slice: ${slice.mkString(",")} ")
    ByteBuffer.wrap(slice).getInt
  }

  private def getData[T](startIndex: Int, mnistArray: Array[Byte], dataType: MnistDataType): Array[Byte] = {
    if (dataType != MnistDataType.UnsignedByte) {
      logger.error(s"Unsupported MnistDataType: MnistDataType.${dataType.getClass.getSimpleName}")
      throw new UnsupportedOperationException("Unsupported MnistDataType")
    }
    val dataArray = mnistArray.drop(startIndex)
    dataArray
  }

  def convertMnistImageToPng(inputFile: File, outputFile: File, bufferedImageType: Int) = {
    logger.debug(s"Start converting Mnist image: ${inputFile.getAbsolutePath}; to '.png' file: ${outputFile.getAbsolutePath}")
    val mnistBytes = IOUtils.toByteArray(inputFile.toURI)

    val magicNumber = mnistBytes.take(4)

    val dataType = MnistDataType.mnistDataTypes(magicNumber(2))
    val dimensionsCount = magicNumber(3).toInt
    logger.debug(s"DataType: $dataType; Number of Dimensions: $dimensionsCount")

    val dimensionSizes = Range(0, dimensionsCount).map(dmns => dimensionFromMnist(dmns, mnistBytes))

    dimensionSizes.zip(Range(0, dimensionsCount)).foreach(size => logger.debug(s"Dimension ${size._2} size of ${size._1}"))

    val data = getData(4 + (4 * dimensionsCount), mnistBytes, dataType)

    val image = new BufferedImage(28, 28, bufferedImageType)
    val imageRaster = Raster.createRaster(image.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point())
    image.setData(imageRaster)

    ImageIO.write(image, "png", outputFile)
  }
}


sealed abstract class MnistDataType (val value: Byte, val size: Int)
object MnistDataType {

  case object UnsignedByte extends MnistDataType(value = 8.toByte, 1)
  case object SignedByte extends MnistDataType(value = 9.toByte, 1)
  case object Short extends MnistDataType(value = 11.toByte, 2)
  case object Int extends MnistDataType(value = 12.toByte, 4)
  case object Float extends MnistDataType(value = 13.toByte, 4)
  case object Double extends MnistDataType(value = 14.toByte, 8)

  val mnistDataTypes = Map(
    UnsignedByte.value -> UnsignedByte,
      SignedByte.value -> SignedByte,
      Short.value -> Short,
      Int.value -> Int,
      Float.value -> Float,
      Double.value -> Double)
}