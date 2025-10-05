# BI-OOP Semestral Project

Select one of the projects bellow and implement it in this repository:

## Table Processor

The main goal of the project is to create an application to process tables with formulas. We should be able to load the table, evaluate all formulas found in the table, filter the rows by their values in specified columns, and finally print the result.

[Course Page](https://courses.fit.cvut.cz/BI-OOP/projects/table-processor.html)

## ASCIIArt

The idea of this project is to load images, translate them into ASCII ART images, optionally apply filters, and save them.

[Course Page](https://courses.fit.cvut.cz/BI-OOP/projects/ASCII-art.html)

## Project Structure

### `project/build.properties`
Specifies the SBT version

### `project/plugin.sbt`
Defines the SBT plugins, in our case:
- Test coverage (`sbt-coverage`)
- Linter (`Wartremover`)
- A JAR assembler (`sbt-assembly`, used for testing)
- A formatter (`Scalafmt`)

### `build.sbt`
Configuration file defining project setting, dependencies and build instructions

### `.gitignore`
Tells git which files to ignore

### `.gitlab-ci.yml`
Defines the pipeline for GitLab (see [here](https://courses.fit.cvut.cz/BI-OOP/projects/index.html#gitlab-pipeline))

### `.scalafmt.conf`
This defines a style for the formatter, unifying the style of all source files

This can be invoked with:
- `sbt scalafmtAll` from system shell,
- the command `scalafmtAll` inside the SBT shell,
- or directly inside of IntelliJ IDEA ([guide here](https://scalameta.org/scalafmt/docs/installation.html#intellij))


### `src/main/scala`
The Scala source files

### `src/test/scala`
The Scala tests

