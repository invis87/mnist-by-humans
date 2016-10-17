name := "Mnist by humans"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  Dependencies.apacheCommonsIO,
  Dependencies.logbackClassic,
  Dependencies.scalaLogging,
  Dependencies.typesafeConfig,
  Dependencies.postgresSql,
  Dependencies.quillCore,
  Dependencies.quillAsyncPostgres
)

dependencyOverrides ++= Dependencies.Scala.all.toSet

unmanagedClasspath in Compile += baseDirectory.value / "src" / "main" / "resources"

fork in run := true
cancelable in Global := true


import com.github.sbtliquibase.SbtLiquibase

enablePlugins(SbtLiquibase)

liquibaseUsername  := "postgres"

liquibasePassword  := "postgres"

liquibaseDriver    := "org.postgresql.Driver"

liquibaseUrl       := "jdbc:postgresql://localhost:32769/postgres"

liquibaseDataDir   := baseDirectory.value / "src" / "main" / "liquibase"

liquibaseChangelog := liquibaseDataDir.value / "changelog.xml"