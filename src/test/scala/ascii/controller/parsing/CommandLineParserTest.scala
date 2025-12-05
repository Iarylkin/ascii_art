package ascii.controller.parsing

import ascii.controller.parsing.{CommandDTO, CommandLineParser}
import org.scalatest.funsuite.AnyFunSuite

class CommandLineParserTest extends AnyFunSuite {

  private val parser = new CommandLineParser()

  // --- Success Cases ---

  test("Should return empty list when no arguments provided") {
    val result = parser.parse(List.empty)
    assert(result.isEmpty)
  }

  test("Should parse a single flag with no parameters (e.g. --image-random)") {
    // Arrange
    val args = List("--image-random")

    // Act
    val result = parser.parse(args)

    // Assert
    assert(result.length == 1)
    assert(result.head.name == "image-random")
    assert(result.head.params.isEmpty)
  }

  test("Should parse a single flag with one parameter (e.g. --image path)") {
    // Arrange
    val args = List("--image", "test.jpg")

    // Act
    val result = parser.parse(args)

    // Assert
    assert(result.length == 1)
    val cmd = result.head
    assert(cmd.name == "image")
    assert(cmd.params == List("test.jpg"))
  }

  test("Should parse multiple mixed commands correctly") {
    // Arrange
    // Command: --image test.jpg --rotate 90 --invert --output-console
    val args = List(
      "--image", "test.jpg",
      "--rotate", "90",
      "--invert",
      "--output-console"
    )

    // Act
    val result = parser.parse(args)

    // Assert
    assert(result.length == 4)

    // Check individual commands maintain order
    assert(result(0) == CommandDTO("image", List("test.jpg")))
    assert(result(1) == CommandDTO("rotate", List("90")))
    assert(result(2) == CommandDTO("invert", Nil))
    assert(result(3) == CommandDTO("output-console", Nil))
  }

  // --- Failure Cases ---

  test("Should throw exception for unknown argument") {
    val args = List("--unknown-flag")

    val error = intercept[IllegalArgumentException] {
      parser.parse(args)
    }
    assert(error.getMessage.contains("Unknown argument"))
  }

  test("Should throw exception when required parameter is missing at the end") {
    // --scale requires 1 param, but list ends
    val args = List("--scale")

    val error = intercept[IllegalArgumentException] {
      parser.parse(args)
    }
    assert(error.getMessage.contains("requires 1 parameter"))
  }

  test("Should throw exception when parameter looks like another flag (Ambiguity check)") {
    // User forgot the value for image and went straight to rotate
    val args = List("--image", "--rotate")

    val error = intercept[IllegalArgumentException] {
      parser.parse(args)
    }
    assert(error.getMessage.contains("cannot be another flag"))
  }
}