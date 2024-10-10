package com.earldouglas.sbt.war

import sbt.Def.Initialize
import sbt.Keys.artifact
import sbt.Keys.fileConverter
import sbt.Keys.moduleName
import sbt.Keys.{`package` => pkg}
import sbt.*
import sbt.given
import scala.language.implicitConversions
import java.nio.file.{ Path => NioPath }
import xsbti.{ FileConverter, HashedVirtualFileRef, VirtualFile }
import sbt.librarymanagement.Configuration
import sbt.SlashSyntax.RichConfiguration

object WarPackagePluginCompat:

  // Flip webappContents around from (dst -> src) to (src -> dst)
  def packageContents: Initialize[Task[Seq[(HashedVirtualFileRef, String)]]] =
    Def.task:
      val conv: FileConverter = fileConverter.value
      WebappComponentsPlugin.webappContents.value
        .map:
            case (dst, src) => (conv.toVirtualFile(src.toPath()), dst)
        .toSeq

