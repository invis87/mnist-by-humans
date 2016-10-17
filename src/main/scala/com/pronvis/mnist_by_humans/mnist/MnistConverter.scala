package com.pronvis.mnist_by_humans.mnist

import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Files
import javax.imageio.ImageIO

import com.pronvis.mnist_by_humans.db.ImageLabel
import org.apache.commons.io.IOUtils
import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory

object MnistConverter {

  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  private final val ImagesBufferSize = 1000

  private def dimensionFromMnist(dimensionNumber: Int, mnistBytes: Array[Byte]): Int = {
    val slice = mnistBytes.slice(4 * (1 + dimensionNumber), (2 + dimensionNumber) * 4)
    logger.debug(s"slice dimension #$dimensionNumber; sliceBytes: ${slice.mkString(",")} ")
    ByteBuffer.wrap(slice).getInt
  }

  private def getData(startIndex: Int, dataLength: Int, mnistArray: Array[Byte], dataType: MnistDataType): Array[Byte] = {
    if (dataType != MnistDataType.UnsignedByte) {
      logger.error(s"Unsupported MnistDataType: MnistDataType.${dataType.getClass.getSimpleName}")
      throw new UnsupportedOperationException("Unsupported MnistDataType")
    }
    val dataArray = mnistArray.slice(startIndex, startIndex + dataLength)
    dataArray
  }

  def imagesFileToPng(inputFile: File, outputFile: File, bufferedImageType: Int) = {
    logger.info(s"Start converting Mnist Image File: ${inputFile.getAbsolutePath}; to '.png' files in: ${outputFile.getAbsolutePath}")
    val mnistBytes = IOUtils.toByteArray(inputFile.toURI)
    val magicNumberBytesCount = 4

    val magicNumber = mnistBytes.take(magicNumberBytesCount)

    val dataType = MnistDataType.mnistDataTypes(magicNumber(2))
    val dimensionsCount = magicNumber(3).toInt
    logger.debug(s"DataType: $dataType; Number of Dimensions: $dimensionsCount")

    if (dimensionsCount != 3) {
      logger.error(s"Unsupported size of dimensions: $dimensionsCount")
      throw new UnsupportedOperationException("Unsupported dimensions in MnistDataFile")
    }

    val dimensionSizes = Range(0, dimensionsCount).map(dmns => dimensionFromMnist(dmns, mnistBytes))

    val imagesCount = dimensionSizes(0)
    val firstDimensionSize = dimensionSizes(1)
    val secondDimensionSize = dimensionSizes(2)

    dimensionSizes.zip(Range(0, dimensionsCount)).foreach(size => logger.debug(s"Dimension ${size._2} size of ${size._1}"))

    Files.createDirectories(outputFile.toPath)

    val mnistImagesData = mnistBytes.drop(mnistBytesOffset(dimensionsCount))

    lazy val imagesStream = Range(0, imagesCount).toStream.map { imgIndex =>
      val data = getData(imgIndex * (firstDimensionSize * secondDimensionSize), firstDimensionSize * secondDimensionSize, mnistImagesData, dataType)
      val image = getImageFromFile(data, dataType, firstDimensionSize, secondDimensionSize, bufferedImageType)
      (imgIndex, image)
    }

    imagesStream.grouped(ImagesBufferSize).zip(Range(0, imagesCount).iterator).foreach { case (imagesSlice, index) => {
      imagesSlice.foreach { case (imgIndex, image) =>
        saveImage(outputFile, imgIndex, image)
      }
      logger.info(s"#${(index + 1) * ImagesBufferSize}/$imagesCount processed")
    }}

  }

  def labelsFileToArray(inputFile: File, bufferedImageType: Int): Array[ImageLabel] = {
    logger.info(s"Start converting Mnist Label File: ${inputFile.getAbsolutePath}; to Array[Byte]")
    val mnistBytes = IOUtils.toByteArray(inputFile.toURI)
    val magicNumberBytesCount = 4

    val magicNumber = mnistBytes.take(magicNumberBytesCount)

    val dataType = MnistDataType.mnistDataTypes(magicNumber(2))
    val dimensionsCount = magicNumber(3).toInt
    logger.debug(s"DataType: $dataType; Number of Dimensions: $dimensionsCount")

    if (dimensionsCount != 1) {
      logger.error(s"Unsupported size of dimensions: $dimensionsCount")
      throw new UnsupportedOperationException("Unsupported dimensions in MnistDataFile")
    }

    val dimensionSizes = Range(0, dimensionsCount).map(dmns => dimensionFromMnist(dmns, mnistBytes))

    val labelsCount = dimensionSizes(0)

    dimensionSizes.zip(Range(0, dimensionsCount)).foreach(size => logger.debug(s"Dimension ${size._2} size of ${size._1}"))

    val mnistLabelsData = mnistBytes.drop(mnistBytesOffset((dimensionsCount)))

    lazy val labelsStream = Range(0, labelsCount).toStream.map { labelIdx =>
      val data = getData(labelIdx, 4, mnistLabelsData, dataType)
      ImageLabel(data.head)
    }

    labelsStream.toArray
  }

  private def saveImage(outputFile: File, imgIndex: Int, image: BufferedImage) = {
    val imageFile = new File(outputFile, s"$imgIndex.png")
    logger.trace(s"Writing image to ${imageFile.getAbsolutePath}")
    try {
      logger.trace("writing image")
      ImageIO.write(image, "png", imageFile)
      logger.trace("finish writing")
    } catch {
      case e: Exception => logger.error("fail writing image", e)
    }
  }

  private def getImageFromFile(data: Array[Byte], dataType: MnistDataType, width: Int, height: Int, imageType: Int): BufferedImage = {
    val image = new BufferedImage(width, height, imageType)
    val imageRaster = Raster.createRaster(image.getSampleModel, new DataBufferByte(data, width * height), new java.awt.Point())
    image.setData(imageRaster)
    image
  }

  private def mnistBytesOffset(dimensionsCount: Int) = {
    4 + (4 * dimensionsCount)
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