name := "demo-scala-akka-spray-supervisor"

version := "0.1-SNAPSHOT"

organization := "demo"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Spray Repository"    at "http://repo.spray.io"
)

libraryDependencies ++= {
  val akkaVersion  = "2.4.3"
  val sprayVersion = "1.3.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "io.spray"          %% "spray-can"       % sprayVersion,
    "io.spray"          %% "spray-routing"   % sprayVersion,
    "io.spray"          %% "spray-client"    % sprayVersion,
    "io.spray"          %% "spray-json"      % sprayVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.1.2",
    "io.spray"          %% "spray-testkit"   % sprayVersion % "test",
    "org.scalatest"     %% "scalatest"       % "2.2.6"      % "test"
  )
}
