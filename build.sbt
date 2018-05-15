lazy val commonSettings = Seq(
  organization := "com.synapse",
  scalaVersion := sys.env.getOrElse("SCALA_VERSION", "2.12.6")
)

lazy val api = project
  .settings(commonSettings)

lazy val manager = project
  .settings(commonSettings)
  .dependsOn(api)

lazy val core = project
  .settings(commonSettings)
  .dependsOn(api)

lazy val server = project
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "de.heikoseeberger" %% "akka-http-argonaut" % "1.20.1",
      "com.typesafe.slick" %% "slick" % "3.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",

      "com.h2database" % "h2" % "1.4.197"
    )
  ).dependsOn(serverApi)

lazy val serverApi = project
  .in(file("server-api"))
  .settings(commonSettings)
