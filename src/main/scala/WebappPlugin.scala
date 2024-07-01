package v5

import java.util.jar.Manifest

import sbt._
import sbt.Def.taskKey
import sbt.Def.settingKey
import sbt.Keys._
import sbt.FilesInfo.lastModified
import sbt.FilesInfo.exists
import sbt.FileFunction.cached

object V5WebappPlugin extends AutoPlugin {

  object autoImport {

    lazy val Webapp =
      config("webapp").hide

    lazy val assets =
      settingKey[Map[File, String]]("assets")

    lazy val classes =
      taskKey[Map[File, String]]("classes")

    lazy val lib =
      taskKey[Map[File, String]]("lib")
  }

  import autoImport._

  override def requires = plugins.JvmPlugin

  private def defaultAssets(srcDir: File): Map[File, String] =
    (srcDir ** "*").get
      .flatMap(source =>
        IO
          .relativize(srcDir, source)
          .map(dest => source -> dest)
      )
      .toMap

  private def defaultClasses(
      mappings: Seq[(File, String)]
  ): Map[File, String] =
    mappings
      .map({ case (source, dest) => source -> s"classes/${dest}" })
      .toMap

  private def defaultLib(
      classpath: Keys.Classpath
  ): Map[File, String] = {
    classpath
      .map(_.data)
      .filter(in => !in.isDirectory && in.getName.endsWith(".jar"))
      .map(source => source -> s"lib/${source.name}")
      .toMap
  }

  override def projectSettings: Seq[Setting[_]] =
    inConfig(Webapp)(
      Seq(
        assets := defaultAssets(
          (sourceDirectory in Compile).value / "webapp"
        ),
        classes := defaultClasses(
          (mappings in packageBin in Compile).value
        ),
        lib := defaultLib(
          (fullClasspath in Runtime).value
        )
      )
    )
}
