name := "Semestral Project"

version := "1.0.0"

scalaVersion := "3.4.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19"
libraryDependencies += "org.scalatestplus" %% "mockito-5-12" % "3.2.19.0"

wartremoverWarnings ++= Seq(
  Wart.AsInstanceOf,
  Wart.EitherProjectionPartial,
  Wart.IsInstanceOf,
  Wart.IterableOps,
  Wart.Null,
  Wart.OptionPartial,
  Wart.PublicInference,
  Wart.StringPlusAny,
  Wart.TripleQuestionMark,
  Wart.TryPartial,
)
