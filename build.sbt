name := "akka_parallel_read_csv"

organization := "tsheppard01"

version := "0.1.0"

scalaVersion := "2.11.12"

//==================================================================================================
// DEPENDENCIES
//==================================================================================================

lazy val akkaVersion = "2.5.6"

lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion

lazy val commonsIo = "commons-io" % "commons-io" % "2.5"

val dependencies = Seq(
  akkaActor,
  commonsIo
)

libraryDependencies ++= dependencies