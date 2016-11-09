publishMavenStyle in bintray := false

bintrayRepository in bintray := "maven"

bintrayOrganization in bintray := Some("htimur")

bintrayReleaseOnPublish in bintray := isSnapshot.value

licenses  += ("MIT", url("http://opensource.org/licenses/MIT"))