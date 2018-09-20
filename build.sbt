val slf4jVersion = "1.7.25"

inThisBuild {
  Seq(
    scalaVersion := "2.12.6",
    organization := "org.ensime",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scalatest"  %% "scalatest" % "3.0.4" % Test,
      "ch.qos.logback" % "logback-classic"  % "1.2.3" % Test,
      "org.slf4j"      % "slf4j-api"        % slf4jVersion % Test,
      "org.slf4j"      % "jul-to-slf4j"     % slf4jVersion % Test,
      "org.slf4j"      % "jcl-over-slf4j"   % slf4jVersion % Test,
      "org.slf4j"      % "log4j-over-slf4j" % slf4jVersion % Test
    )
  )
}

val common = Seq(
  scalacOptions += "-language:implicitConversions",
  javaOptions in Test ++= {
    val jar = (packageBin in Compile).value
    Seq(
      s"""-Dpcplod.settings=-Xplugin:${jar.getAbsolutePath},-Jdummy=${jar.lastModified}""",
      s"""-Dpcplod.classpath=${(fullClasspath in Test).value.map(_.data).mkString(",")}"""
    )
  },
  mimaPreviousArtifacts := Set(organization.value %% name.value % "1.2.1")
)

lazy val pcplod = project.settings(common)

lazy val example = project.dependsOn(pcplod % "test").settings(
  // too awkward to remove the deprecated warning
  scalacOptions -= "-Xfatal-warnings",
  scalacOptions in Test ++= {
    val jar = (packageBin in Compile).value
    Seq(s"-Xplugin:${jar.getAbsolutePath}", s"-Jdummy=${jar.lastModified}") // ensures recompile
  }
).settings(common)
