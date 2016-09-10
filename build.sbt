

lazy val pcplod = project.settings(
  name := "pcplod",
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)

lazy val example = project.dependsOn(pcplod % "test").settings(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  scalacOptions in Test <++= (packageBin in Compile) map { jar =>
    // needs timestamp to force recompile
    Seq("-Xplugin:" + jar.getAbsolutePath, "-Jdummy=" + jar.lastModified)
  }
)
