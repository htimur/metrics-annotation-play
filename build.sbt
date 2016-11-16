name := "metrics-annotation-play"
organization := "de.khamrakulov"
scalaVersion := "2.11.8"
crossScalaVersions := Seq("2.11.8","2.12.0")
scalacOptions := Seq("-deprecation", "-unchecked", "-feature")

resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.jcenterRepo

val metricsVersion = "3.1.2"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.5.3" % Provided,
  "de.khamrakulov.metrics-reporter-play" %% "reporter-core" % "1.0.0" % Provided,
  "io.dropwizard.metrics" % "metrics-annotation" % metricsVersion,
  "com.google.code.findbugs" % "jsr305" % "3.0.1",

//test
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)
