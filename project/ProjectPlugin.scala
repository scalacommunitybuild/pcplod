import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._
import Sensible._

object ProjectPlugin extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  override def buildSettings = Seq(
    scalaVersion := "2.11.8",
    organization := "org.ensime",
    version := "1.1.1-SNAPSHOT"
  )

  override def projectSettings = Sensible.settings ++
    SonatypeSupport.sonatype("ensime", "pcplod", SonatypeSupport.Apache2) ++
    Seq(
      ScalariformKeys.preferences := FormattingPreferences().setPreference(AlignSingleLineCaseStatements, true),
      libraryDependencies ++= Sensible.testLibs(),
      javaOptions in Test ++= Seq(
        "-Dlogback.configurationFile=../logback-test.xml"
      )
    )

}
