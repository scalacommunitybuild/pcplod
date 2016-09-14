val common = Seq(
  javaOptions in Test ++= Seq(
    s"""-Dpcplod.settings=${(scalacOptions in Test).value.mkString(",")}""",
    s"""-Dpcplod.classpath=${(fullClasspath in Test).value.map(_.data).mkString(",")}"""
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)

lazy val pcplod = project.settings(common).settings(name := "pcplod")

lazy val example = project.dependsOn(pcplod % "test").settings(common).settings(
  scalacOptions in Test ++= {
    val jar = (packageBin in Compile).value
    Seq(s"-Xplugin:${jar.getAbsolutePath}", s"-Jdummy=${jar.lastModified}") // ensures recompile
  }
)
