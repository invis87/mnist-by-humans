name := "Mnist by humans"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0")


fork in run := true
cancelable in Global := true
