organization := "com.mak"
name := "WeatherSimulator"
version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  	Seq(
  	    "org.specs2" %% "specs2-core" % "3.8.5" % "test",
  	    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  	)
}

mainClass := Some("com.mak.weather.station.Simulator")

resolvers ++= Seq(
			"snapshots"     			
						at "http://oss.sonatype.org/content/repositories/snapshots",
            "releases"  
              			at "http://oss.sonatype.org/content/repositories/releases",
			"Artima Maven Repository" 	
						at "http://repo.artima.com/releases"
)
 
scalacOptions ++= Seq("-unchecked", "-deprecation")