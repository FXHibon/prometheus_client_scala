organization in Global := "org.lyranthe.prometheus"

val scala212 = "2.12.11"
val scala213 = "2.13.1"

version in ThisBuild := {
  git.gitCurrentTags.value.lastOption match {
    case Some(tag) => tag
    case None      => "%s-SNAPSHOT".format(git.gitCurrentBranch.value)
  }
}

scalacOptions ++= Seq("-groups",
                      "-implicits",
                      "-implicits-show-all",
                      "-diagrams",
                      "-deprecation")
scalaVersion in ThisBuild := scala212
crossScalaVersions in ThisBuild := Seq(scala212)

lazy val macros =
  project
    .in(file("macros"))
    .settings(
      libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      scalaVersion := scala213,
      crossScalaVersions := Seq(scala212, scala213),
      scalacOptions := scalacOptions.value
    )

lazy val client =
  project
    .in(file("client"))
    .enablePlugins(spray.boilerplate.BoilerplatePlugin)
    .settings(
      scalaVersion := scala213,
      crossScalaVersions := Seq(scala212, scala213),
      scalacOptions := scalacOptions.value
    )
    .dependsOn(macros)

lazy val play26 =
  project
    .in(file("play26"))
    .settings(
      scalaVersion := scala213,
      crossScalaVersions := Seq(scala212, scala213),
      libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play"       % "2.8.1" % Provided withSources (),
        "com.typesafe.play" %% "play-guice" % "2.8.1" % Provided withSources ()
      )
    )
    .dependsOn(client)
