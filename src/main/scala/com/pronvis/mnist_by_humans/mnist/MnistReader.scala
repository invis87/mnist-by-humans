package com.pronvis.mnist_by_humans.mnist

import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO

import org.apache.commons.io.IOUtils

object MnistReader {
  private def dimensionFromMnist(dimensionNumber: Int, mnistBytes: Array[Byte]): Int = {
    val slice = mnistBytes.slice(4 * (1 + dimensionNumber), (2 + dimensionNumber) * 4)
    println(s"slice dimension #$dimensionNumber; slice: ${slice.mkString(",")} ")
    ByteBuffer.wrap(slice).getInt
  }

  private def getData[T](startIndex: Int, mnistArray: Array[Byte], dataType: MnistDataType): Array[Byte] = {
    val dataArray = mnistArray.drop(startIndex)
    dataArray
//    val groupdeData = dataArray.grouped(dataType.size)
//
//    val convertFunction = dataType match {
//      case MnistDataType.UnsignedByte => (bytes: Array[Byte]) => bytes.head
//      case MnistDataType.SignedByte => (bytes: Array[Byte]) => 5.toByte//ByteBuffer.wrap(bytes).getInt
//      case MnistDataType.Short => (bytes: Array[Byte]) => 5.toByte//ByteBuffer.wrap(bytes).getInt
//      case MnistDataType.Int => (bytes: Array[Byte]) => 5.toByte//ByteBuffer.wrap(bytes).getInt
//      case MnistDataType.Float => (bytes: Array[Byte]) => 5.toByte//ByteBuffer.wrap(bytes).getInt
//      case MnistDataType.Double => (bytes: Array[Byte]) => 5.toByte//ByteBuffer.wrap(bytes).getInt
//    }
//
//    println("print Chars ")
//    groupdeData.take(100).map(convertFunction).foreach(ch => println(ch.toChar))
//
//    groupdeData.map(convertFunction).toArray
  }

  def readMnistImage(inputFile: File, outputFile: File, bufferedImageType: Int) = {
// Вроде бы близко уже. Осталось проверить порядок элементов в массиве 28х28

    val mnistBytes = IOUtils.toByteArray(inputFile.toURI)

    val magicNumber = mnistBytes.take(4)

    val dataType = MnistDataType.mnistDataTypes(magicNumber(2))
    val dimensionsCount = magicNumber(3).toInt
    println(s"DataType: $dataType; Number of Dimensions: $dimensionsCount")

    val dimensionSizes = Range(0, dimensionsCount).map(dmns => dimensionFromMnist(dmns, mnistBytes))

    dimensionSizes.zip(Range(0, dimensionsCount)).foreach(size => println(s"Dimension ${size._2} size of ${size._1}"))

    val data = getData(4 + (4 * dimensionsCount), mnistBytes, dataType)

    val image = new BufferedImage(28, 28, bufferedImageType)
    image.setData(Raster.createRaster(image.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))

    ImageIO.write(image, "png", outputFile)

//    val TYPE_3BYTE_BGR = new BufferedImage(28, 28, BufferedImage.TYPE_3BYTE_BGR)
//    val TYPE_4BYTE_ABGR = new BufferedImage(28, 28, BufferedImage.TYPE_4BYTE_ABGR)
//    val TYPE_4BYTE_ABGR_PRE = new BufferedImage(28, 28, BufferedImage.TYPE_4BYTE_ABGR_PRE)
//    val TYPE_BYTE_BINARY = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_BINARY)
//    val TYPE_BYTE_GRAY = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY)
//    val TYPE_BYTE_INDEXED = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_INDEXED)
////    val TYPE_INT_ARGB = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB)
////    val TYPE_INT_BGR = new BufferedImage(28, 28, BufferedImage.TYPE_INT_BGR)
////    val TYPE_INT_RGB = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB)
////    val TYPE_USHORT_555_RGB = new BufferedImage(28, 28, BufferedImage.TYPE_USHORT_555_RGB)
////    val TYPE_USHORT_565_RGB = new BufferedImage(28, 28, BufferedImage.TYPE_USHORT_565_RGB)
////    val TYPE_USHORT_GRAY = new BufferedImage(28, 28, BufferedImage.TYPE_USHORT_GRAY)
//
//
//
//    TYPE_3BYTE_BGR.setData(Raster.createRaster(TYPE_3BYTE_BGR.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
//    TYPE_4BYTE_ABGR.setData(Raster.createRaster(TYPE_4BYTE_ABGR.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
//    TYPE_4BYTE_ABGR_PRE.setData(Raster.createRaster(TYPE_4BYTE_ABGR_PRE.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
//    TYPE_BYTE_BINARY.setData(Raster.createRaster(TYPE_BYTE_BINARY.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
//    TYPE_BYTE_GRAY.setData(Raster.createRaster(TYPE_BYTE_GRAY.getSampleModel(), new DataBufferByte(data.take(28*28), 28*28), new java.awt.Point()))
//    TYPE_BYTE_INDEXED.setData(Raster.createRaster(TYPE_BYTE_INDEXED.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
////    TYPE_INT_ARGB.setData(Raster.createRaster(TYPE_INT_ARGB.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
////    TYPE_INT_BGR.setData(Raster.createRaster(TYPE_INT_BGR.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
////    TYPE_INT_RGB.setData(Raster.createRaster(TYPE_INT_RGB.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
////    TYPE_USHORT_555_RGB.setData(Raster.createRaster(TYPE_USHORT_555_RGB.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
////    TYPE_USHORT_565_RGB.setData(Raster.createRaster(TYPE_USHORT_565_RGB.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
////    TYPE_USHORT_GRAY.setData(Raster.createRaster(TYPE_USHORT_GRAY.getSampleModel(), new DataBufferByte(data, 28*28), new java.awt.Point()))
//
//
//    ImageIO.write(TYPE_3BYTE_BGR, "png", new File("/Users/pronvis/Downloads/images/TYPE_3BYTE_BGR.png"))
//    ImageIO.write(TYPE_4BYTE_ABGR, "png", new File("/Users/pronvis/Downloads/images/TYPE_4BYTE_ABGR.png"))
//    ImageIO.write(TYPE_4BYTE_ABGR_PRE, "png", new File("/Users/pronvis/Downloads/images/TYPE_4BYTE_ABGR_PRE.png"))
//    ImageIO.write(TYPE_BYTE_BINARY, "png", new File("/Users/pronvis/Downloads/images/TYPE_BYTE_BINARY.png"))
//    ImageIO.write(TYPE_BYTE_GRAY, "png", new File("/Users/pronvis/Downloads/images/TYPE_BYTE_GRAY.png"))
//    ImageIO.write(TYPE_BYTE_INDEXED, "png", new File("/Users/pronvis/Downloads/images/TYPE_BYTE_INDEXED.png"))
//    ImageIO.write(TYPE_INT_ARGB, "png", new File("/Users/pronvis/Downloads/images/TYPE_INT_ARGB.png"))
//    ImageIO.write(TYPE_INT_BGR, "png", new File("/Users/pronvis/Downloads/images/TYPE_INT_BGR.png"))
//    ImageIO.write(TYPE_INT_RGB, "png", new File("/Users/pronvis/Downloads/images/TYPE_INT_RGB.png"))
//    ImageIO.write(TYPE_USHORT_555_RGB, "png", new File("/Users/pronvis/Downloads/images/TYPE_USHORT_555_RGB.png"))
//    ImageIO.write(TYPE_USHORT_565_RGB, "png", new File("/Users/pronvis/Downloads/images/TYPE_USHORT_565_RGB.png"))
//    ImageIO.write(TYPE_USHORT_GRAY, "png", new File("/Users/pronvis/Downloads/images/TYPE_USHORT_GRAY.png"))


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