import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._

object ProjectPlugin extends SensiblePlugin {

  override def projectSettings = Seq(
    scalaVersion := "2.11.8",
    organization := "org.ensime",
    version := "1.0.0-SNAPSHOT",
    ScalariformKeys.preferences := FormattingPreferences().setPreference(AlignSingleLineCaseStatements, true),
    libraryDependencies ++= Sensible.testLibs(),
    javaOptions in Test ++= Seq(
      "-Dlogback.configurationFile=../logback-test.xml"
    )
  ) ++ Sensible.settings ++ SonatypeSupport.sonatype("ensime", "pcplod", SonatypeSupport.Apache2)

}
