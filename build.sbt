name := "aws-iot-example"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers += "Eclipse Paho Repo" at "https://repo.eclipse.org/content/repositories/paho-releases/"

libraryDependencies ++= Seq(
  "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.0.2"
)
