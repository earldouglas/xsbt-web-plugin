package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys._
import sbt._

object WebappPackageRunnerPluginCompat {

  def runnerJars(config: Configuration): Initialize[Task[Seq[File]]] =
    Def.task {
      Classpaths
        .managedJars(config, classpathTypes.value, update.value)
        .map(_.data)
        .toSeq
    }
}
