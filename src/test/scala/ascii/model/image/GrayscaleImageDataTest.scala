package ascii.model.image

import org.scalatest.funsuite.AnyFunSuite

class GrayscaleImageDataTest extends AnyFunSuite {

  test("Should create valid Grayscale image with Double values") {
    // Arrange
    val width = 2
    val height = 2
    val data = Vector(0.0, 100.5, 200.0, 255.0)

    // Act
    val image = GrayscaleImageData(width, height, data)

    // Assert
    assert(image.width == 2)
    assert(image.height == 2)
    assert(image.pixel(0, 0) == 0.0)
    assert(image.pixel(1, 0) == 100.5)
    assert(image.pixel(1, 1) == 255.0)
  }

  test("Should throw exception for negative dimensions (Inherited validation)") {
    assertThrows[IllegalArgumentException] {
      GrayscaleImageData(-5, 5, Vector())
    }
  }
}
