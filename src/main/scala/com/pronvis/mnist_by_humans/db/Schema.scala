package com.pronvis.mnist_by_humans.db

import com.pronvis.mnist_by_humans.db.Entities.ImageData
import com.typesafe.config.Config
import io.getquill.{PostgresAsyncContext, SnakeCase}

class Schema(dbConfig: Config) {

  val context = new PostgresAsyncContext[SnakeCase](dbConfig)
  import context._

  val imagesData = quote {
    query[ImageData]
  }

}