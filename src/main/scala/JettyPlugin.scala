package v5

import java.util.jar.Manifest

import sbt._
import sbt.Def.taskKey
import sbt.Def.settingKey
import sbt.Keys._
import sbt.FilesInfo.lastModified
import sbt.FilesInfo.exists
import sbt.FileFunction.cached

object V5JettyPlugin extends AutoPlugin {

  override def requires = V5WebappPlugin

  override lazy val projectSettings =
    V5WebappPlugin.projectSettings

}
