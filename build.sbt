name := "demo-scala-akka-spray"

version := "0.1-SNAPSHOT"

organization := "demo"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val project1 = (project in file("demo-scala-akka-spray-basic"))
lazy val project2 = (project in file("demo-scala-akka-spray-router"))
lazy val project3 = (project in file("demo-scala-akka-spray-aggregator"))
lazy val project4 = (project in file("demo-scala-akka-spray-supervisor"))
