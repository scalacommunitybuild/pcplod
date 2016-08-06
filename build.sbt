import scalariform.formatter.preferences._

organization := "org.ensime"
name := "pcplod"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

Sensible.settings

SonatypeSupport.sonatype("ensime", "pcplod", SonatypeSupport.Apache2)
headers := Copyright.ApacheMap

scalariformPreferences := FormattingPreferences().setPreference(AlignSingleLineCaseStatements, true)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
) ++ Sensible.testLibs()
