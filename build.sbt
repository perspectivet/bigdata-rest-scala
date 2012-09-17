name := "bigdata-rest-scala"

organization := "com.github.perspectivet"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.1"

resolvers ++= Seq(
	  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
	  "repo.codahale.com" at "http://repo.codahale.com",
	  "bigdata.releases" at "http://www.systap.com/maven/releases",
	  "bigdata.snapshots" at "http://www.systap.com/maven/snapshots",
	  "nxparser-repo" at "http://nxparser.googlecode.com/svn/repository",
	  "openrdf.releases" at "http://repo.aduna-software.org/maven2/releases"
)

libraryDependencies := Seq(
  "org.specs2" %% "specs2" % "1.7.1" % "test",
  "net.databinder" %% "dispatch" % "0.8.8",
  "net.databinder" %% "dispatch-http" % "0.8.8",
  "net.databinder" %% "dispatch-core" % "0.8.8",
//  "org.scardf" % "scardf" % "0.6-SNAPSHOT",
  "com.codahale" %% "jerkson" % "0.5.0",
  "org.clapper" %% "grizzled-slf4j" % "0.6.9",
  "com.bigdata" % "bigdata" % "1.2.2-SNAPSHOT"
)

initialCommands := "import com.github.perspectivet.bigdata.rest._"
