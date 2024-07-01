package v5

import java.util.jar.Manifest

import sbt._
import sbt.Def.taskKey
import sbt.Def.settingKey
import sbt.Keys._
import sbt.FilesInfo.lastModified
import sbt.FilesInfo.exists
import sbt.FileFunction.cached

case class WarFile(source: File, warPath: String)

object V5WebappPlugin extends AutoPlugin {

  object autoImport {

    lazy val Webapp =
      config("webapp").hide

    lazy val assets =
      settingKey[Seq[WarFile]]("assets")

    lazy val classes =
      taskKey[Seq[WarFile]]("classes")

    lazy val lib =
      taskKey[Seq[WarFile]]("lib")
  }

  import autoImport._

  override def requires = plugins.JvmPlugin

  private def defaultAssets(srcDir: File): Seq[WarFile] = {
    (srcDir ** "*")
      .get
      .flatMap(f => {
        IO.relativize(srcDir, f)
          .map(warPath => {
            WarFile(
              source = f,
              warPath = warPath
            )
          })
      })
  }

  private def defaultClasses(mappings: Seq[(File, String)]): Seq[WarFile] = {
    mappings.map({ case (file, string) =>
      WarFile(source = file, warPath = s"classes/${string}")
    })
  }

  private def defaultLib(classpath: Keys.Classpath): Seq[WarFile] = {
    classpath
      .map(_.data)
      .filter(in => !in.isDirectory && in.getName.endsWith(".jar"))
      .map(f => {
        WarFile(
          source = f,
          warPath = s"lib/${f.name}"
        )
      })
  }

  override def projectSettings: Seq[Setting[_]] =
    inConfig(Webapp)(
      Seq(
        assets := defaultAssets((Compile / sourceDirectory).value / "webapp"),
        classes := defaultClasses((Compile / packageBin / mappings).value),
        lib := defaultLib((Runtime / fullClasspath).value)
      )
    )

}
