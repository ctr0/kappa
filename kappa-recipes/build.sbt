name := "kappa-recipes"

version := "1.0"

scalaVersion := "2.11.8"


lazy val kappa_core = RootProject(file("../kappa-core"))
lazy val kappa_coordinator = RootProject(file("../kappa-coordinator"))
lazy val kappa_kafka = RootProject(file("../kappa-kafka"))
lazy val kappa_spark = RootProject(file("../kappa-spark"))
val main = Project(
  id = "kappa-recipes", 
  base = file(".")
).dependsOn(
  kappa_core,
  kappa_coordinator,
  kappa_kafka,
  kappa_spark
)
