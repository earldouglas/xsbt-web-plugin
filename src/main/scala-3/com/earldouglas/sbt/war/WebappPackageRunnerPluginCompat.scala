package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys.*
import sbt.*
import sbt.given
import scala.language.implicitConversions
import xsbti.HashedVirtualFileRef

object WebappPackageRunnerPluginCompat:

    def runnerJars(config: Configuration): Initialize[Task[Seq[File]]] =
      Def.task:
          Classpaths
            .managedJars(config, classpathTypes.value, update.value, fileConverter.value)
            .map(_.data)
            .map(fileConverter.value.toPath(_))
            .map(_.toFile())
            .toList

    def toFilePath(file: HashedVirtualFileRef): Initialize[Task[String]] =
      Def.task:
        fileConverter.value.toPath(file).toString()
