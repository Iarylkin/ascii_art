package ascii.model.image

import org.scalatest.funsuite.AnyFunSuite

class RgbImageDataTest extends AnyFunSuite {

  test("Should create valid RGB image and store format") {
    // Arrange
    val width = 2
    val height = 1
    val pixels = Vector(
      Pixel(255, 0, 0),
      Pixel(0, 0, 255)
    )
    val format = "png"

    // Act
    val image = RgbImageData(width, height, pixels, format)

    // Assert
    assert(image.width == 2)
    assert(image.height == 1)
    assert(image.format == "png")
    assert(image.pixel(0, 0) == Pixel(255, 0, 0))
    assert(image.pixel(1, 0) == Pixel(0, 0, 255))
  }

  test("Should throw exception on data length mismatch (Inherited validation)") {
    val pixels = Vector(Pixel(0, 0, 0))

    assertThrows[IllegalArgumentException] {
      RgbImageData(2, 2, pixels, "jpg")
    }
  }
}