name := "auth-db-scala-slick"

version := "1.0"

scalaVersion := "2.11.2"

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.github.t3hnar" %% "scala-bcrypt" % "2.4",
  "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test",
  "org.mockito" % "mockito-core" % "1.10.8" % "test",
  "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.1.4" % "test",
  "org.hamcrest" % "hamcrest-library" % "1.3" % "test"
)