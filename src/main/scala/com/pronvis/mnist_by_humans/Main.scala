package com.pronvis.mnist_by_humans

import java.awt.image.BufferedImage
import java.io.File

import com.pronvis.mnist_by_humans.mnist.MnistConverter

object Main {

  def main(args: Array[String]) {

//
//    val mnistImagesFile = new File("/Users/pronvis/Downloads/train-images-idx3-ubyte")
//    val directoryForImages = new File("/Users/pronvis/scala/mnist_by_humans/mnist_files/train-images")
//    MnistConverter.imagesFileToPng(mnistImagesFile, directoryForImages, BufferedImage.TYPE_BYTE_GRAY)

    val mnistLabelsFile = new File("/Users/pronvis/Downloads/train-labels-idx1-ubyte")
    MnistConverter.labelsFileToArray(mnistLabelsFile, BufferedImage.TYPE_BYTE_GRAY)

    //to read labels
    //    MnistReader.readFile("/Users/pronvis/Downloads/train-labels-idx1-ubyte")
  }
}