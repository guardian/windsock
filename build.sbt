import AssemblyKeys._
import com.gu.SbtDistPlugin

organization  := "com.gu.windsock"

version       := "0.1"

scalaVersion  := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.2.4"
  val sprayV = "1.2.1"
  Seq(
    "com.amazonaws"       %   "aws-java-sdk"     % "1.7.13",
    "org.json4s"          %%  "json4s-jackson"   % "3.2.6",
    "com.github.nscala-time" %% "nscala-time"    % "1.2.0",
    "io.spray"            %   "spray-can"        % sprayV,
    "io.spray"            %%  "spray-json"       % "1.2.6",
    "io.spray"            %   "spray-routing"    % sprayV,
    "io.spray"            %   "spray-testkit"    % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"       % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"     % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"      % "2.3.7" % "test"
  )
}

Revolver.settings

// assembly to generate self-contained JAR
assemblySettings

jarName in assembly := "windsock.jar"

test in assembly := {}


// sbt-dist-plugin to generate TeamCity artifact
seq(SbtDistPlugin.distSettings :_*)

// include the jar itself (this is the location you want the jar to save to)
distFiles <+= (assembly in Compile) map { _ -> "packages/windsock/windsock.jar" }

// and include custom scripts in src/main/deploy
distFiles <++= (sourceDirectory in Compile) map { src => (src / "deploy" ***) x rebase(src / "deploy", "") }
