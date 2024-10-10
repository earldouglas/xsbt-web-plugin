package com.earldouglas.sbt.war

import sbt.Keys.artifact
import sbt.Keys.moduleName
import sbt.Keys.{`package` => pkg}
import sbt.{given, _}

/** Identifies the files that compose the .war file (resources, .class
  * files in the classes/ directory, and .jar files in the lib/
  * directory).
  *
  * Also configures the .war file as an sbt package artifact.
  *
  * This is also used by other user-facing plugins (WebappRunnerPlugin).
  */
object WarPackagePlugin extends AutoPlugin {


  override def requires = WebappComponentsPlugin

  override lazy val projectSettings: Seq[Setting[_]] = {

    val packageTaskSettings: Seq[Setting[_]] =
      Defaults.packageTaskSettings(
        pkg,
        WarPackagePluginCompat.packageContents
      )

    val packageArtifactSetting: Setting[_] =
      pkg / artifact := Artifact(moduleName.value, "war", "war")

    val artifactSettings: Seq[Setting[_]] =
      addArtifact(Compile / pkg / artifact, pkg)

    Seq(
      packageTaskSettings,
      Seq(packageArtifactSetting),
      artifactSettings
    ).flatten
  }
}
