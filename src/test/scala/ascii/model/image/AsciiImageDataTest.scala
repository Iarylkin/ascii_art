package ascii.model.image

import org.scalatest.funsuite.AnyFunSuite

class AsciiImageDataTest extends AnyFunSuite {

  // --- Success Cases ---

  test("Should create a valid AsciiImageData and retrieve pixels correctly") {
    // Arrange: 2x2 Grid
    // A B
    // C D
    val width = 2
    val height = 2
    val data = Vector('A', 'B', 'C', 'D')

    // Act
    val image = AsciiImageData(width, height, data)

    // Assert
    assert(image.width == 2)
    assert(image.height == 2)

    // Validate coordinate mapping: index = y * width + x
    assert(image.pixel(0, 0) == 'A') // 0 * 2 + 0 = 0
    assert(image.pixel(1, 0) == 'B') // 0 * 2 + 1 = 1
    assert(image.pixel(0, 1) == 'C') // 1 * 2 + 0 = 2
    assert(image.pixel(1, 1) == 'D') // 1 * 2 + 1 = 3
  }

  test("Should allow 1x1 image") {
    val image = AsciiImageData(1, 1, Vector('#'))
    assert(image.pixel(0, 0) == '#')
  }

  // --- Failure Cases (Validation Logic) ---

  test("Should throw IllegalArgumentException when width is zero or negative") {
    assertThrows[IllegalArgumentException] {
      AsciiImageData(0, 10, Vector())
    }
    assertThrows[IllegalArgumentException] {
      AsciiImageData(-5, 10, Vector())
    }
  }

  test("Should throw IllegalArgumentException when height is zero or negative") {
    assertThrows[IllegalArgumentException] {
      AsciiImageData(10, 0, Vector())
    }
  }

  test("Should throw IllegalArgumentException when data length does not match dimensions") {
    // We declare 2x2 (4 pixels needed), but provide only 3
    val data = Vector('A', 'B', 'C')

    val error = intercept[IllegalArgumentException] {
      AsciiImageData(2, 2, data)
    }
    assert(error.getMessage.contains("does not match dimensions"))
  }

  test("Should throw IndexOutOfBoundsException when accessing invalid coordinates") {
    val image = AsciiImageData(2, 2, Vector('A', 'B', 'C', 'D'))

    // Accessing (2, 2) is out of bounds for a 2x2 image (max index is 1,1)
    assertThrows[IndexOutOfBoundsException] {
      image.pixel(2, 2)
    }
  }
}