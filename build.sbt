organization := "com.mak"
name := "WeatherSimulator"
version := "0.1-SNAPSHOT-R2"

scalaVersion := "2.11.8"

parallelExecution in Test := false

libraryDependencies ++= {
  	Seq(
  	    "org.specs2" %% "specs2-core" % "3.8.5" % "test",
  	    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  	    "org.jliszka" %% "probability-monad" % "1.0.1",
  	    "org.apache.spark" % "spark-core_2.11" % "2.0.0",
  		  "org.apache.spark" % "spark-mllib_2.11" % "2.0.0"
  	)
}

resolvers ++= Seq(
			"Sonatype snapshots"
						at "http://oss.sonatype.org/content/repositories/snapshots",
            "Sonatype Releases"
              			at "http://oss.sonatype.org/content/repositories/releases",
			"Artima Maven Repository"
						at "http://repo.artima.com/releases"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")
