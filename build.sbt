name := "metrics-annotaion-play"
organization := "de.khamrakulov"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.8","2.12.0")

scalacOptions := Seq("-deprecation", "-unchecked", "-feature")

val metricsVersion = "3.1.2"
val metricsPlayVersion = "2.4.0_0.4.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.5.3" % Provided,
  "io.dropwizard.metrics" % "metrics-annotation" % metricsVersion,
  "com.kenshoo" %% "metrics-play" % metricsPlayVersion,
  "com.google.code.findbugs" % "jsr305" % "3.0.1",

//test
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

enablePlugins(GitVersioning)
git.useGitDescribe := true