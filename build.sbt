

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
  scalacOptions in Test ++= {
    val jar = (packageBin in Compile).value
    Seq(s"-Xplugin:${jar.getAbsolutePath}", s"-Jdummy=${jar.lastModified}") // ensures recompile
  },
  javaOptions in Test += s"-Dpcplod.plugin=${(packageBin in Compile).value.getAbsolutePath}"
)
