name := "kappa-spark"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.0.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.0.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10-assembly" % "2.0.1"

lazy val core = RootProject(file("../kappa-core"))
val main = Project(id = "kappa-spark", base = file(".")).dependsOn(core)
