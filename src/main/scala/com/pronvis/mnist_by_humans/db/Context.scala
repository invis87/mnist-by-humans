package com.pronvis.mnist_by_humans.db

import com.typesafe.config.ConfigFactory
import io.getquill.{JdbcContext, PostgresDialect, SnakeCase}

object Context {
  private val config = ConfigFactory.load().getConfig("db.test")
  implicit val context = new JdbcContext[PostgresDialect, SnakeCase](config)

}
