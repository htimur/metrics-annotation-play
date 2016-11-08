publishMavenStyle := false

bintrayRepository := "metrics-annotaion-play"

bintrayOrganization in bintray := Some("de.khamrakulov")

bintrayReleaseOnPublish := isSnapshot.value

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))