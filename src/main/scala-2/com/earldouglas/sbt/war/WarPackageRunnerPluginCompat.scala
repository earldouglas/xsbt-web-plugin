package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys._
import sbt._

object WarPackageRunnerPluginCompat {

  def runnerJars(config: Configuration): Initialize[Task[Seq[File]]] =
    Def.task {
      Classpaths
        .managedJars(config, classpathTypes.value, update.value)
        .map(_.data)
        .toSeq
    }

  def toFile(file: TaskKey[File]): Initialize[Task[File]] =
    file
}
