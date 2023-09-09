libraryDependencies += "jakarta.servlet" % "jakarta.servlet-api" % "5.0.0" % "provided"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % "test"

enablePlugins(JettyPlugin)

containerLibs in Jetty := Seq(
  "org.eclipse.jetty" % "jetty-runner" % "11.0.15" intransitive ()
)
