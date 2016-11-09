publishMavenStyle in bintray := false

bintrayRepository in bintray := "de.khamrakulov"

bintrayOrganization in bintray := Some("khamrakulov")

bintrayReleaseOnPublish in bintray := isSnapshot.value

licenses  += ("MIT", url("http://opensource.org/licenses/MIT"))