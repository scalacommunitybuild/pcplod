import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

inThisBuild {
  Seq(
    scalaVersion := "2.11.8",
    organization := "org.ensime",
    sonatypeGithub := ("ensime", "pcplod"),
    licenses := Seq(Apache2),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )
}

val common = Seq(
  javaOptions in Test ++= Seq(
    s"""-Dpcplod.settings=${(scalacOptions in Test).value.mkString(",")}""",
    s"""-Dpcplod.classpath=${(fullClasspath in Test).value.map(_.data).mkString(",")}"""
  ),
  mimaPreviousArtifacts := Set(organization.value %% name.value % "1.1.0")
)

lazy val pcplod = project.settings(common)

lazy val example = project.dependsOn(pcplod % "test").settings(common).settings(
  // too awkward to remove the deprecated warning
  scalacOptions -= "-Xfatal-warnings",
  scalacOptions in Test ++= {
    val jar = (packageBin in Compile).value
    Seq(s"-Xplugin:${jar.getAbsolutePath}", s"-Jdummy=${jar.lastModified}") // ensures recompile
  }
)

