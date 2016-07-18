
name := "akka-http-logging-starter-kit"

version := "1.0"

scalaVersion := "2.11.7"

organization := "com.knoldus"

val akkaV = "2.4.5"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
   "pl.project13.scala" %% "rainbow" % "0.2"
)

ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}
fork in run := true
