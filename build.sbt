name := "Hello-Scala"
organization := "it.sijmen"
version := "0.0.1"

scalaVersion := "2.12.1"

scalaSource in Compile := baseDirectory.value / "src/main/scala"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.12" % "2.4.17"
