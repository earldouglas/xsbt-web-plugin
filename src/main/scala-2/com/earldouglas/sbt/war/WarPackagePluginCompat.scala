package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt._

object WarPackagePluginCompat {

  // Flip webappContents around from (dst -> src) to (src -> dst)
  lazy val packageContents
      : Initialize[Task[Seq[(java.io.File, String)]]] =
    WebappComponentsPlugin.webappContents
      .map {
        _.map {
          _.swap
        }.toSeq
      }
}
