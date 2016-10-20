
lazy val commonSettings = Seq(
  organization := "com.example",
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
    Dependencies.quillAsyncPostgres
  ),

  dependencyOverrides ++= Dependencies.Scala.all.toSet,
  unmanagedClasspath in Compile += baseDirectory.value / "src" / "main" / "resources",

  fork in run := true,
  cancelable in Global := true
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*)

lazy val mnistTask = taskKey[Unit]("Start processing mnist files from application.conf 'mnist' part.")
mnistTask := (runMain in Compile).toTask(" com.pronvis.mnist_by_humans.mnist.MnistToDB").value

import com.github.sbtliquibase.SbtLiquibase

enablePlugins(SbtLiquibase)

liquibaseUsername  := "postgres"

liquibasePassword  := "postgres"

liquibaseDriver    := "org.postgresql.Driver"

liquibaseUrl       := "jdbc:postgresql://localhost:32769/postgres"

liquibaseDataDir   := baseDirectory.value / "src" / "main" / "liquibase"

liquibaseChangelog := liquibaseDataDir.value / "changelog.xml"