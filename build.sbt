

lazy val commonSettings = Seq(
  organization := "org.ensime",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

val akkaVersion = "2.3.14"
val streamsVersion = "1.0"

lazy val logback = Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.slf4j" % "jul-to-slf4j" % "1.7.12",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.12"
)

////////////////////////////////////////////////
// utils
def testLibs(scalaV: String, config: String = "test") = Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % config,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % config,
  "org.scalacheck" %% "scalacheck" % "1.12.1" % config,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % config,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion % config
) ++ logback.map(_ % config)


lazy val root = (project in file(".")).settings(
  commonSettings: _*).settings(
  libraryDependencies ++= Seq(
    "org.ensime" %% "core" % "1.0.0",
    "commons-io" % "commons-io" % "2.4" % "test"
  ) ++ testLibs(scalaVersion.value)
  ).dependsOn(
    testingSimple % "test"
)

lazy val testingSimple = project.in(file("testing/simple")).settings(commonSettings: _*)



