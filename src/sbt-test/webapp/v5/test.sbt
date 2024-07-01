TaskKey[Unit]("check") := {

  import v5.WarFile

  def toMap(xs: Seq[WarFile]): Map[File, String] =
    xs
      .map({ case WarFile(source, warPath) => (source -> warPath) })
      .toMap

  def assertEquals(
    name: String,
    expected: Map[File, String],
    obtained: Seq[WarFile]
  ): Unit = {

    def sizesDoNotMatch = expected.size != obtained.size

    val obtainedMap = toMap(obtained)

    def mappingsDoNotMatch = expected != obtainedMap

    if (sizesDoNotMatch || mappingsDoNotMatch) {
      sys.error(
        s"""|${name}:
            |  expected:
            |${expected.mkString("    - ", "\n    - ", "")}
            |  obtained:
            |${obtainedMap.mkString("    - ", "\n    - ", "")}
            |""".stripMargin
      )
    }
  }

  assertEquals(
    name = "assets",
    expected = {
      val root: File = (Compile / sourceDirectory).value
      Map(
          root / "webapp" -> "",
          root / "webapp" / "WEB-INF" -> "WEB-INF",
          root / "webapp" / "WEB-INF" / "web.xml" -> "WEB-INF/web.xml",
          root / "webapp" / "index.html" -> "index.html",
      )
    },
    obtained = (Webapp / assets).value
  )

  assertEquals(
    name = "classes",
    expected = {
      val root: File = (Compile / classDirectory).value
      Map(
        root / "effects" -> "classes/effects",
        root / "adder" -> "classes/adder",
        root / "servlets/QueryServlet.class" -> "classes/servlets/QueryServlet.class",
        root / "effects/Service.class" -> "classes/effects/Service.class",
        root / "effects/package.class" -> "classes/effects/package.class",
        root / "servlets/UpdateServlet$$anon$1.class" -> "classes/servlets/UpdateServlet$$anon$1.class",
        root / "servlets/DatabaseServlet.class" -> "classes/servlets/DatabaseServlet.class",
        root / "servlets" -> "classes/servlets",
        root / "servlets/UpdateServlet.class" -> "classes/servlets/UpdateServlet.class",
        root / "effects/package$.class" -> "classes/effects/package$.class",
        root / "servlets/CommandServlet.class" -> "classes/servlets/CommandServlet.class",
        root / "servlets/JdbcServlet.class" -> "classes/servlets/JdbcServlet.class",
        root / "effects/Service$.class" -> "classes/effects/Service$.class",
        root / "adder/Database$.class" -> "classes/adder/Database$.class",
        root / "adder/Database.class" -> "classes/adder/Database.class",
      )
    },
    obtained = (Webapp / classes).value
  )

  def assertContains(
    name: String,
    expected: Set[String],
    obtained: Seq[WarFile]
  ): Unit = {

    def sizesDoNotMatch = expected.size != obtained.size

    val obtainedMap = toMap(obtained)

    def mappingsDoNotMatch = expected != obtainedMap.values.toSet

    if (sizesDoNotMatch || mappingsDoNotMatch) {
      sys.error(
        s"""|${name}:
            |  expected: ${expected}
            |  obtained:
            |${obtainedMap.mkString("    - ", "\n    - ", "")}
            |""".stripMargin
      )
    }
  }

  assertContains(
    name = "lib",
    expected =
      Set(
        "lib/scala-library.jar",
        "lib/h2-2.2.224.jar",
      ),
    obtained = (Webapp / lib).value
  )

}
