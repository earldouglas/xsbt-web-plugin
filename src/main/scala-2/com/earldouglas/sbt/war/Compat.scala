package com.earldouglas.sbt.war

import sbt.Keys._
import sbt.Keys.{`package` => pkg}
import sbt._

object Compat {
  val Compile_pkg_artifact = Compile / pkg / artifact
  val Compile_sourceDirectory = Compile / sourceDirectory
  val Compile_target = Compile / target
  val Global_onLoad = Global / onLoad
  val pkg_artifact = pkg / artifact
}
