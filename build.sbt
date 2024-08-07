// General
Global / excludeLintKeys += crossSbtVersions // don't warn about unused setting
crossSbtVersions := Seq("0.13.18", "1.10.0")
enablePlugins(SbtPlugin)
name := "xsbt-web-plugin"
organization := "com.earldouglas"
sbtPlugin := true
scalacOptions ++= Seq("-feature", "-deprecation")
scalaVersion := "2.12.19"

// scripted-plugin
scriptedBufferLog := false
watchSources ++= { (sourceDirectory.value ** "*").get }

// AWS deployment support
val awsJavaSdkVersion = "1.12.765"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-elasticbeanstalk" % awsJavaSdkVersion
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % awsJavaSdkVersion

// Publish to Sonatype, https://www.scala-sbt.org/release/docs/Using-Sonatype.html
credentials := List(
  Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
)
description := "Servlet support for sbt"
developers := List(
  Developer(
    id = "earldouglas",
    name = "James Earl Douglas",
    email = "james@earldouglas.com",
    url = url("https://earldouglas.com/")
  )
)
homepage := Some(url("https://github.com/earldouglas/xsbt-web-plugin"))
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
    url("https://github.com/earldouglas/xsbt-web-plugin"),
    "scm:git@github.com:earldouglas/xsbt-web-plugin.git"
  )
)
ThisBuild / versionScheme := Some("semver-spec")
