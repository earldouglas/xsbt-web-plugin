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

  override def projectSettings: Seq[Setting[_]] =
    inConfig(Webapp)(
      Seq(
        assets := {
          val srcDir: File =
            (sourceDirectory in Compile).value / "webapp"
          (srcDir ** "*")
            .get
            .flatMap(src =>
              IO
                .relativize(srcDir, src)
                .map(dst => src -> dst)
            )
            .toMap
        },
        classes := {
          val classesMappings: Seq[(File, String)] =
            (mappings in packageBin in Compile).value
          classesMappings
            .map({ case (src, dst) => src -> s"classes/${dst}" })
            .toMap
        },
        lib := {
          val cp: Keys.Classpath =
            (fullClasspath in Runtime).value
          cp
            .map(_.data)
            .filter(f => f.isFile())
            .filter(f => f.getName().endsWith(".jar"))
            .map(src => src -> s"lib/${src.name}")
            .toMap
        }
      )
    )
}
