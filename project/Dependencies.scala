import sbt._

object Dependencies {
  val scalaVersion = "2.11.8"
  val apacheCommonsVersion = "2.4"
  val logbackClassicVersion = "1.1.7"
  val scalaLoggingVersion = "3.5.0"
  val typesafeVersion = "1.3.0"
  val postgresVersion = "9.4.1208"
  val quillVersion = "0.10.0"

  val apacheCommonsIO = "commons-io" % "commons-io" % apacheCommonsVersion
  val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackClassicVersion
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion exclude("org.slf4j", "slf4j-api")
  val typesafeConfig = "com.typesafe" % "config" % typesafeVersion
  val postgresSql = "org.postgresql" % "postgresql" % postgresVersion
  val quillCore = "io.getquill" %% "quill-core" % quillVersion exclude("com.typesafe.scala-logging", "scala-logging_2.11")
  val quillAsyncPostgres = "io.getquill" %% "quill-async-postgres" % quillVersion exclude("org.slf4j", "slf4j-api")

  object Scala {
    val scalaCompiler = "org.scala-lang" % "scala-compiler" % scalaVersion
    val scalaLibrary = "org.scala-lang" % "scala-library" % scalaVersion
    val scalaReflect = "org.scala-lang" % "scala-reflect" % scalaVersion
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.4"

    val all = Seq(scalaCompiler, scalaLibrary, scalaReflect, scalaXml)
  }

}