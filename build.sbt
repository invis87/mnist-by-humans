import sbt.Keys._
import sbt._

lazy val commonSettings = Seq(
  organization := "com.pronvis",
  version := "0.1",
  name := "Mnist by humans",
  scalaVersion := "2.11.8",

  libraryDependencies ++= Seq(
    Dependencies.apacheCommonsIO,
    Dependencies.logbackClassic,
    Dependencies.scalaLogging,
    Dependencies.typesafeConfig,
    Dependencies.postgresSql,
    Dependencies.quillCore,
    Dependencies.quillAsyncPostgres,

    Dependencies.http4sDsl,
    Dependencies.http4sBlazeServer
  ),

  dependencyOverrides ++= Dependencies.Scala.all.toSet,

  fork in Global := true,
  cancelable in Global := true
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*)

lazy val runMnist = TaskKey[Unit]("run-mnist", "Start processing mnist files from application.conf 'mnist' part.")
fullRunTask(runMnist, Runtime, "com.pronvis.mnist_by_humans.mnist.MnistToDB")

lazy val runServer = TaskKey[Unit]("run-server", "Start web server.")
fullRunTask(runServer, Runtime, "com.pronvis.mnist_by_humans.Main")


// ===================
// ====== MAGIC ======
// ===================

import com.typesafe.config._
lazy val configTask = settingKey[Config]("start liquibase plugin")
configTask in ThisBuild := {
  val resourceDir = (resourceDirectory in Compile).value
  val appConfig = ConfigFactory.parseFile(resourceDir / "application.conf")
  val config = ConfigFactory.load(appConfig)
  println(s"'application.conf' loaded from ${resourceDir.getPath}")
  config
}

import com.github.sbtliquibase.SbtLiquibase

enablePlugins(SbtLiquibase)

liquibaseUsername  := configTask.value.getString("global.db.user")

liquibasePassword  := configTask.value.getString("global.db.password")

liquibaseDriver    := configTask.value.getString("global.db.driver")

liquibaseUrl       := configTask.value.getString("global.db.url")

liquibaseDataDir   := baseDirectory.value / "src" / "main" / "liquibase"

liquibaseChangelog := liquibaseDataDir.value / "changelog.xml"