name := "MyNotes"

version := "1.0"

lazy val `mynotes` = (project in file(".")).enablePlugins(PlayScala, SwaggerPlugin)

swaggerDomainNameSpaces := Seq("models")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.9"

libraryDependencies ++= Seq(guice,
  "io.monix" %% "monix" % "3.3.0",
  "org.webjars" % "swagger-ui" % "3.45.0",
  "net.debasishg" %% "redisclient" % "3.30",
  "com.github.t3hnar" %% "scala-bcrypt" % "4.3.0",
  "org.reactivemongo" % "play2-reactivemongo_2.12" % "1.0.3-play28",

  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,

  "org.postgresql" % "postgresql" % "42.2.19",

  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",

  "org.flywaydb" % "flyway-core" % "7.7.0",
  "org.flywaydb" %% "flyway-play" % "7.7.0"
)

      