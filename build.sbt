lazy val commonSettings = Seq(
  organization := "com.synapse",
  scalaVersion := sys.env.getOrElse("SCALA_VERSION", "2.11.12")
)

lazy val api = project
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % "2.3.0",
      "org.apache.spark" %% "spark-streaming" % "2.3.0"
    )
  )

lazy val manager = project
  .settings(commonSettings)
  .dependsOn(api)

lazy val server = project
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "de.heikoseeberger" %% "akka-http-argonaut" % "1.20.1"
    )
  ).dependsOn(manager, serverApi)

lazy val serverApi = project
  .in(file("server-api"))
  .settings(commonSettings)
