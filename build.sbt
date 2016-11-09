name := "kappa"

version := "1.0"

scalaVersion := "2.11.8"


lazy val kappa_core = RootProject(file("./kappa-core"))
lazy val kappa_coordinator = RootProject(file("./kappa-coordinator"))
lazy val kappa_kafka = RootProject(file("./kappa-kafka"))
lazy val kappa_spark = RootProject(file("./kappa-spark"))
lazy val kappa_recipes = RootProject(file("./kappa-recipes"))

val kappa = Project(
  id = "kappa", 
  base = file(".")
).dependsOn(
  kappa_core,
  kappa_coordinator,
  kappa_kafka,
  kappa_spark,
  kappa_recipes
)
