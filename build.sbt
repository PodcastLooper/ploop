// *****************************************************************************
// Projects
// *****************************************************************************

lazy val ploopserver =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .configs(IntegrationTest)
    .settings(
      name := "ploop-server"
    )
    .settings(settings)
    .settings(
      Defaults.itSettings,
      headerSettings(IntegrationTest),
      inConfig(IntegrationTest)(scalafmtSettings),
      IntegrationTest / console / scalacOptions --= Seq(
        "-Xfatal-warnings",
        "-Ywarn-unused-import",
        "-Ywarn-unused:implicits",
        "-Ywarn-unused:imports",
        "-Ywarn-unused:locals",
        "-Ywarn-unused:params",
        "-Ywarn-unused:patvars",
        "-Ywarn-unused:privates"
      ),
      IntegrationTest / parallelExecution := false
    )
    .settings(
      libraryDependencies ++= Seq(
        library.catsCore,
        library.circeCore,
        library.circeGeneric,
        library.circeRefined,
        library.circeParser,
        library.doobieCore,
        library.doobieHikari,
        library.doobiePostgres,
        library.doobieRefined,
        library.enumeratumCirce,
        library.flywayCore,
        library.http4sBlazeClient,
        library.http4sBlazeServer,
        library.http4sCirce,
        library.http4sDsl,
        library.logback,
        library.sqlite,
        library.slick,
        library.slf4jnop,
        library.slickhikari,
        library.pureConfig,
        library.refinedCats,
        library.refinedCore,
        library.refinedPureConfig,
        library.tapirCats,
        library.tapirCirce,
        library.tapirCore,
        library.tapirEnumeratum,
        library.tapirHttp4s,
        library.tapirOpenApiDocs,
        library.tapirOpenApiYaml,
        library.tapirRefined,
        library.tapirSwaggerUi,
        library.doobieScalaTest   % IntegrationTest,
        library.refinedScalaCheck % IntegrationTest,
        library.scalaCheck        % IntegrationTest,
        library.scalaTest         % IntegrationTest,
        library.scalaTestPlus     % IntegrationTest,
        library.doobieScalaTest   % Test,
        library.refinedScalaCheck % Test,
        library.scalaCheck        % Test,
        library.scalaTest         % Test,
        library.scalaTestPlus     % Test
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val cats          = "2.4.1"
      val circe         = "0.13.0"
      val doobie        = "0.10.0"
      val enumeratum    = "1.6.1"
      val flyway        = "7.5.3"
      val http4s        = "0.21.14"
      val logback       = "1.2.3"
      val sqlite        = "3.34.0"
      val slick         = "3.3.3"
      val slf4jnop      = "1.6.4"
      val pureConfig    = "0.14.0"
      val refined       = "0.9.20"
      val scalaCheck    = "1.15.2"
      val scalaTest     = "3.2.3"
      val scalaTestPlus = "3.2.3.0"
      val tapir         = "0.17.10"
    }
    val catsCore          = "org.typelevel"               %% "cats-core"                % Version.cats
    val circeCore         = "io.circe"                    %% "circe-core"               % Version.circe
    val circeGeneric      = "io.circe"                    %% "circe-generic"            % Version.circe
    val circeRefined      = "io.circe"                    %% "circe-refined"            % Version.circe
    val circeParser       = "io.circe"                    %% "circe-parser"             % Version.circe
    val doobieCore        = "org.tpolecat"                %% "doobie-core"              % Version.doobie
    val doobieHikari      = "org.tpolecat"                %% "doobie-hikari"            % Version.doobie
    val doobiePostgres    = "org.tpolecat"                %% "doobie-postgres"          % Version.doobie
    val doobieRefined     = "org.tpolecat"                %% "doobie-refined"           % Version.doobie
    val doobieScalaTest   = "org.tpolecat"                %% "doobie-scalatest"         % Version.doobie
    val enumeratumCirce   = "com.beachape"                %% "enumeratum-circe"         % Version.enumeratum
    val flywayCore        = "org.flywaydb"                %  "flyway-core"              % Version.flyway
    val http4sBlazeServer = "org.http4s"                  %% "http4s-blaze-server"      % Version.http4s
    val http4sBlazeClient = "org.http4s"                  %% "http4s-blaze-client"      % Version.http4s
    val http4sCirce       = "org.http4s"                  %% "http4s-circe"             % Version.http4s
    val http4sDsl         = "org.http4s"                  %% "http4s-dsl"               % Version.http4s
    val logback           = "ch.qos.logback"              %  "logback-classic"          % Version.logback
    val sqlite            = "org.xerial"                  %  "sqlite-jdbc"              % Version.sqlite
    val slick             = "com.typesafe.slick"          %  "slick_2.13"               % Version.slick
    val slf4jnop          = "org.slf4j"                   %  "slf4j-nop"                % Version.slf4jnop
    val slickhikari       = "com.typesafe.slick"          %  "slick-hikaricp_2.13"      % Version.slick
    val pureConfig        = "com.github.pureconfig"       %% "pureconfig"               % Version.pureConfig
    val refinedCore       = "eu.timepit"                  %% "refined"                  % Version.refined
    val refinedCats       = "eu.timepit"                  %% "refined-cats"             % Version.refined
    val refinedPureConfig = "eu.timepit"                  %% "refined-pureconfig"       % Version.refined
    val refinedScalaCheck = "eu.timepit"                  %% "refined-scalacheck"       % Version.refined
    val scalaCheck        = "org.scalacheck"              %% "scalacheck"               % Version.scalaCheck
    val scalaTest         = "org.scalatest"               %% "scalatest"                % Version.scalaTest
    val scalaTestPlus     = "org.scalatestplus"           %% "scalacheck-1-15"          % Version.scalaTestPlus
    val tapirCats         = "com.softwaremill.sttp.tapir" %% "tapir-cats"               % Version.tapir
    val tapirCirce        = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % Version.tapir
    val tapirCore         = "com.softwaremill.sttp.tapir" %% "tapir-core"               % Version.tapir
    val tapirEnumeratum   = "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"         % Version.tapir
    val tapirHttp4s       = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % Version.tapir
    val tapirOpenApiDocs  = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % Version.tapir
    val tapirOpenApiYaml  = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % Version.tapir
    val tapirRefined      = "com.softwaremill.sttp.tapir" %% "tapir-refined"            % Version.tapir
    val tapirSwaggerUi    = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s"  % Version.tapir
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  scalafmtSettings ++
  scoverageSettings

lazy val commonSettings =
  Seq(
    scalaVersion := "2.13.4",
    organization := "org.podcastlooper.ploop",
    organizationName := "PodcastLooper",
    startYear := Some(2020),
    licenses += ("MPL-2.0", url("https://www.mozilla.org/en-US/MPL/2.0/")),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    scalacOptions ++= Seq(
      "-deprecation",
      "-explaintypes",
      "-feature",
      "-language:higherKinds",
      "-unchecked",
      "-Xcheckinit",
      "-Xfatal-warnings",
      "-Xlint:adapted-args",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Ywarn-dead-code",
      "-Ywarn-extra-implicit",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates",
      "-Ywarn-value-discard",
      "-Ycache-plugin-class-loader:last-modified",
      "-Ycache-macro-class-loader:last-modified",
    ),
    Compile / console / scalacOptions --= Seq(
      "-Xfatal-warnings",
      "-Ywarn-unused-import",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates"
    ),
    Compile / compile / wartremoverWarnings ++= Warts.unsafe.filterNot(_ == Wart.Any), // Disable the "Any" wart due to too many false positives.
    Test / console / scalacOptions --= Seq(
      "-Xfatal-warnings",
      "-Ywarn-unused-import",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates"
    )
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
  )

lazy val scoverageSettings =
  Seq(
    coverageMinimum := 60,
    coverageFailOnMinimum := false,
    coverageHighlighting := true
  )

