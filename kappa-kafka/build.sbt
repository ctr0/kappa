name := "kappa-kafka"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.kafka" %% "kafka" % "0.10.1.0"

lazy val core = RootProject(file("../kappa-core"))
val main = Project(id = "kappa-kafka", base = file(".")).dependsOn(core)