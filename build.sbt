name := "Semestral Project"

version := "1.0.0"

scalaVersion := "3.4.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-5-12" % "3.2.19.0" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.19.0" % "test"

wartremoverWarnings ++= Seq(
  Wart.AsInstanceOf,
  Wart.EitherProjectionPartial,
  Wart.IsInstanceOf,
  Wart.Null,
  Wart.OptionPartial,
  Wart.PublicInference,
  Wart.StringPlusAny,
  Wart.TripleQuestionMark,
  Wart.TryPartial,
)
