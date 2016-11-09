name := "kappa-coordinator"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.curator" % "curator-recipes" % "2.11.0"

lazy val core = RootProject(file("../kappa-core"))
val main = Project(id = "kappa-coordinator", base = file(".")).dependsOn(core)