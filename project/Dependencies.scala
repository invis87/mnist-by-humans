import sbt._

object Dependencies {
  private val scalaVersion = "2.11.8"
  private val apacheCommonsVersion = "2.4"
  private val logbackClassicVersion = "1.1.7"
  private val scalaLoggingVersion = "3.5.0"
  private val typesafeVersion = "1.3.0"
  private val postgresVersion = "9.4.1208"
  private val quillVersion = "0.10.0"
  private val http4sVersion = "0.14.10"

  val apacheCommonsIO = "commons-io" % "commons-io" % apacheCommonsVersion
  val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackClassicVersion
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion exclude("org.slf4j", "slf4j-api")
  val typesafeConfig = "com.typesafe" % "config" % typesafeVersion
  val postgresSql = "org.postgresql" % "postgresql" % postgresVersion
  val quillCore = "io.getquill" %% "quill-core" % quillVersion exclude("com.typesafe.scala-logging", "scala-logging_2.11")
  val quillAsyncPostgres = "io.getquill" %% "quill-async-postgres" % quillVersion exclude("org.slf4j", "slf4j-api")
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion

  object Scala {
    val scalaCompiler = "org.scala-lang" % "scala-compiler" % scalaVersion
    val scalaLibrary = "org.scala-lang" % "scala-library" % scalaVersion
    val scalaReflect = "org.scala-lang" % "scala-reflect" % scalaVersion
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.4"

    val all = Seq(scalaCompiler, scalaLibrary, scalaReflect, scalaXml)
  }

}