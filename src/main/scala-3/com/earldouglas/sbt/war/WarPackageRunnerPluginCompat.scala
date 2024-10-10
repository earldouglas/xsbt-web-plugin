package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys._
import sbt.*
import sbt.given
import scala.language.implicitConversions
import sbt.internal.util.Attributed
import sbt.librarymanagement.Configuration
import sbt.SlashSyntax.RichConfiguration
import xsbti.HashedVirtualFileRef

object WarPackageRunnerPluginCompat:

    def runnerJars(config: Configuration): Initialize[Task[Seq[File]]] =
      Def.task:
          Classpaths
            .managedJars(config, classpathTypes.value, update.value, fileConverter.value)
            .map(_.data)
            .map(fileConverter.value.toPath(_))
            .map(_.toFile())
            .toList

    def toFile(file: TaskKey[HashedVirtualFileRef]): Initialize[Task[File]] =
      Def.task:
        fileConverter.value
          .toPath(file.value)
          .toFile()
