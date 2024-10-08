// General
enablePlugins(SbtPlugin)
name := "sbt-war"
organization := "com.earldouglas"
sbtPlugin := true
scalacOptions ++= Seq("-feature", "-deprecation")
scalaVersion := "2.12.18" // https://scalameta.org/metals/blog/2023/07/19/silver#support-for-scala-21218

// scripted-plugin
scriptedBufferLog := false
watchSources ++= { (sourceDirectory.value ** "*").get }
scriptedLaunchOpts +=
  "-DtemplateDirectory=" + (sourceDirectory.value / "template")
scriptedBatchExecution := true
scriptedParallelInstances := 8

// Scalafix
semanticdbEnabled := true
semanticdbVersion := scalafixSemanticdb.revision
scalacOptions += "-Ywarn-unused-import"
scalacOptions += s"-P:semanticdb:sourceroot:${baseDirectory.value}"

// webapp-runner
lazy val webappRunnerVersion =
  settingKey[String]("webapp-runner version")
webappRunnerVersion := "10.1.28.0"
libraryDependencies += "com.heroku" % "webapp-runner" % webappRunnerVersion.value % Provided

// webapp-components-runner
lazy val webappComponentsRunnerVersion =
  settingKey[String]("webapp-components-runner version")
webappComponentsRunnerVersion := "10.1.28.0.0-M1"
libraryDependencies += "com.earldouglas" % "webapp-components-runner" % webappComponentsRunnerVersion.value % Provided

// sbt-buildinfo
enablePlugins(BuildInfoPlugin)
buildInfoKeys := Seq[BuildInfoKey](
  webappRunnerVersion,
  webappComponentsRunnerVersion
)
buildInfoPackage := "com.earldouglas.sbt.war"

// Testing
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
Test / fork := true

// Publish to Sonatype, https://www.scala-sbt.org/release/docs/Using-Sonatype.html
credentials := List(
  Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
)
description := "Package and run .war files with sbt"
developers := List(
  Developer(
    id = "earldouglas",
    name = "James Earl Douglas",
    email = "james@earldouglas.com",
    url = url("https://earldouglas.com/")
  )
)
homepage := Some(url("https://github.com/earldouglas/sbt-war"))
licenses := List(
  "BSD New" -> url("https://opensource.org/licenses/BSD-3-Clause")
)
organizationHomepage := Some(url("https://earldouglas.com/"))
organizationName := "James Earl Douglas"
pomIncludeRepository := { _ => false }
publishMavenStyle := true
publishTo := Some(
  "releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
)
scmInfo := Some(
  ScmInfo(
    url("https://github.com/earldouglas/sbt-war"),
    "scm:git@github.com:earldouglas/sbt-war.git"
  )
)
ThisBuild / versionScheme := Some("semver-spec")
