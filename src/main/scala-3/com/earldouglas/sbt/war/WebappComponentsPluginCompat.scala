package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys._
import sbt.*
import sbt.given
import scala.language.implicitConversions
import sbt.librarymanagement.Configuration
import sbt.SlashSyntax.RichConfiguration

object WebappComponentsPluginCompat:

  lazy val classpathFiles: Initialize[Task[Seq[File]]] =
      Def.task:
        (Runtime / fullClasspath).value
          .map(_.data)
          .map(fileConverter.value.toPath(_))
          .map(_.toFile())
