package com.pronvis.mnist_by_humans

import java.awt.image.BufferedImage
import java.io.File

import com.pronvis.mnist_by_humans.mnist.MnistReader

object Main {

  def main(args: Array[String]) {




    val input = new File("/Users/pronvis/Downloads/train-images-idx3-ubyte")
    val output = new File("/Users/pronvis/Downloads/images/xx")
    MnistReader.readMnistImage(input, output, BufferedImage.TYPE_BYTE_GRAY)

    //to read labels
    //    MnistReader.readFile("/Users/pronvis/Downloads/train-labels-idx1-ubyte")
  }
}