package ascii.model.convert

import ascii.model.image.{Pixel, RgbImageData}
import org.scalatest.funsuite.AnyFunSuite

class GrayScaleConverterTest extends AnyFunSuite {

  private val converter = new GrayScaleConverter()

  test("Should convert pure colors using correct luminance formula") {
    // Arrange
    // We create a 3x1 image with Red, Green, Blue
    val pixels = Vector(
      Pixel(255, 0, 0), // Red
      Pixel(0, 255, 0), // Green
      Pixel(0, 0, 255)  // Blue
    )
    val inputImage = RgbImageData(3, 1, pixels, "test")

    // Act
    val result = converter.convert(inputImage)

    // Assert
    assert(result.width == 3)
    assert(result.height == 1)

    // Check Red: 255 * 0.3 = 76.5
    assert(Math.abs(result.pixel(0, 0) - 76.5) < 0.001)

    // Check Green: 255 * 0.59 = 150.45
    assert(Math.abs(result.pixel(1, 0) - 150.45) < 0.001)

    // Check Blue: 255 * 0.11 = 28.05
    assert(Math.abs(result.pixel(2, 0) - 28.05) < 0.001)
  }

  test("Should convert white and black correctly") {
    // Arrange
    val pixels = Vector(
      Pixel(0, 0, 0),       // Black
      Pixel(255, 255, 255)  // White
    )
    val inputImage = RgbImageData(2, 1, pixels, "test")

    // Act
    val result = converter.convert(inputImage)

    // Assert
    assert(Math.abs(result.pixel(0, 0) - 0.0) < 0.001, "Black should be 0.0")
    assert(Math.abs(result.pixel(1, 0) - 255.0) < 0.001, "White should be 255.0")
  }
}