name := "metrics-annotation-play"
organization := "de.khamrakulov"
scalaVersion := "2.11.8"
crossScalaVersions := Seq("2.11.8")
scalacOptions := Seq("-deprecation", "-unchecked", "-feature")

resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.jcenterRepo

val metricsVersion = "3.1.2"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.5.10" % Provided,
  "io.dropwizard.metrics" % "metrics-core" % metricsVersion % Provided,
  "io.dropwizard.metrics" % "metrics-annotation" % metricsVersion,

//test
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)
