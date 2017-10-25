scalaVersion := "2.12.3"

name         := "influx-vs-postgres-logs"
organization := "zensoft.net"
version      := "0.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-actor"            % "2.5.4",
  "com.typesafe.akka"  %% "akka-testkit"          % "2.5.4" % Test,
  "org.scalatest"      %% "scalatest"             % "3.0.1" % Test,
  "com.paulgoldbaum"   %% "scala-influxdb-client" % "0.5.2",
  "com.typesafe.slick" %% "slick"                 % "3.2.1",
  "org.slf4j"          % "slf4j-nop"              % "1.6.4",
  "org.postgresql"     % "postgresql"             % "42.1.4",
  "org.scalikejdbc"    %% "scalikejdbc"           % "3.1.0",
  "com.h2database"     %  "h2"                    % "1.4.196",
  "ch.qos.logback"     %  "logback-classic"       % "1.2.3"
)



