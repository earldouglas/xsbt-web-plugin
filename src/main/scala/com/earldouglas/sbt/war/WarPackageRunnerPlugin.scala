package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys._
import sbt.Keys.{`package` => pkg}
import sbt._

import java.util.concurrent.atomic.AtomicReference
import scala.sys.process.{Process => ScalaProcess}

/** Launches the .war file managed by WarPackagePlugin. Uses a forked
  * JVM to run Tomcat via com.heroku:webapp-runner.
  */
object WarPackageRunnerPlugin extends AutoPlugin {

  object autoImport {
    lazy val War = config("war").hide
    lazy val warPort = settingKey[Int]("war container port")
    lazy val warStart = taskKey[Unit]("start war container")
    lazy val warJoin = taskKey[Unit]("join war container")
    lazy val warStop = taskKey[Unit]("stop war container")
    lazy val warForkOptions =
      taskKey[ForkOptions]("war container fork options")
  }

  import autoImport._
  import WebappRunnerPlugin.autoImport._

  override val requires: Plugins =
    WarPackagePlugin && WebappRunnerPlugin

  override val projectConfigurations: Seq[Configuration] =
    Seq(War)

  private lazy val containerInstance =
    new AtomicReference[Option[ScalaProcess]](None)

  override val projectSettings: Seq[Setting[_]] = {

    def stopContainerInstance(): Unit = {
      val oldProcess = containerInstance.getAndSet(None)
      oldProcess.foreach(_.destroy())
    }

    val startWar: Initialize[Task[Unit]] =
      Def.task {
        stopContainerInstance()

        val runnerJars: Seq[File] =
          WarPackageRunnerPluginCompat.runnerJars(War).value

        runnerJars match {
          case runner :: Nil =>
            streams.value.log.info("[sbt-war] Starting server")
            val process: ScalaProcess =
              Fork.java.fork(
                warForkOptions.value,
                Seq(
                  "-jar",
                  runner.getPath(),
                  "--port",
                  warPort.value.toString(),
                  WarPackageRunnerPluginCompat
                    .toFile(pkg)
                    .value
                    .getPath()
                )
              )
            containerInstance.set(Some(process))
          case _ :: _ =>
            streams.value.log.error(
              s"""[sbt-war] Expected one runner, but found ${runnerJars.length}: ${runnerJars
                  .mkString("\n  * ", "  * ", "")}"""
            )
          case _ =>
            streams.value.log.error(
              """[sbt-war] Expected a runner, but found none"""
            )
        }
      }

    val joinWar: Initialize[Task[Unit]] =
      Def.task(containerInstance.get.map(_.exitValue()))

    val stopWar: Initialize[Task[Unit]] =
      Def.task(stopContainerInstance())

    val onLoadSetting: Initialize[State => State] =
      Def.setting {
        Compat.Global_onLoad.value
          .compose(_.addExitHook(stopContainerInstance()))
      }

    val forkOptions: Initialize[Task[ForkOptions]] =
      Def.task {
        ForkOptions()
          .withOutputStrategy(Some(BufferedOutput(streams.value.log)))
      }

    val runnerLibrary: Initialize[ModuleID] =
      Def.setting {
        ModuleID(
          organization = "com.heroku",
          name = "webapp-runner",
          revision = webappRunnerVersion.value
        ).intransitive() % War
      }

    Seq(
      warPort := 8080,
      warStart := startWar.value,
      warJoin := joinWar.value,
      warStop := stopWar.value,
      warForkOptions := forkOptions.value,
      Compat.Global_onLoad := onLoadSetting.value,
      libraryDependencies += runnerLibrary.value
    )
  }
}
