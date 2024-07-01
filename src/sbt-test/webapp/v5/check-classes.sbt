TaskKey[Unit]("check-classes") := {

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
}
