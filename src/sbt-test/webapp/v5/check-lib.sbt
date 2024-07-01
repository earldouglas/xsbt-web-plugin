TaskKey[Unit]("check-lib") := {

  def assertContains(
    name: String,
    expected: Set[String],
    obtained: Map[File, String]
  ): Unit = {

    val sizesDoNotMatch = expected.size != obtained.size
    val mappingsDoNotMatch = expected != obtained.values.toSet

    if (sizesDoNotMatch || mappingsDoNotMatch) {
      sys.error(
        s"""|${name}:
            |  expected: ${expected}
            |  obtained:
            |${obtained.mkString("    - ", "\n    - ", "")}
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
