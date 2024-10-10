package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys._
import sbt._

object WebappComponentsPluginCompat {

  val classpathFiles: Initialize[Task[Seq[File]]] =
    (Runtime / fullClasspath)
      .map(_.files)
}
