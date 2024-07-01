TaskKey[Unit]("check-assets") := {

  def assertEquals(
    name: String,
    expected: Map[File, String],
    obtained: Map[File, String]
  ): Unit = {

    val sizesDoNotMatch = expected.size != obtained.size
    val mappingsDoNotMatch = expected != obtained

    if (sizesDoNotMatch || mappingsDoNotMatch) {
      sys.error(
        s"""|${name}:
            |  expected:
            |${expected.mkString("    - ", "\n    - ", "")}
            |  obtained:
            |${obtained.mkString("    - ", "\n    - ", "")}
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
}
