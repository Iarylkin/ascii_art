package ascii.model.convert

import ascii.model.convert.tables.AsciiTable
import ascii.model.image.GrayscaleImageData
import org.scalatest.funsuite.AnyFunSuite

class AsciiConverterTest extends AnyFunSuite {

  // A simple stub table for testing purposes.
  // It maps 0.0 -> 'A' and anything else -> 'B'
  private val stubTable = new AsciiTable {
    override def map(gray: Double): Char = if (gray == 0.0) 'A' else 'B'
  }

  private val converter = new AsciiConverter(stubTable)

  test("Should convert grayscale values to characters using provided table") {
    // Arrange
    val width = 2
    val height = 2
    // Grid:
    // 0.0 (A)   128.0 (B)
    // 255.0 (B) 0.0 (A)
    val grayData = Vector(0.0, 128.0, 255.0, 0.0)
    val inputImage = GrayscaleImageData(width, height, grayData)

    // Act
    val result = converter.convert(inputImage)

    // Assert
    assert(result.width == width)
    assert(result.height == height)

    // Check individual mapping logic
    assert(result.pixel(0, 0) == 'A') // 0.0 -> A
    assert(result.pixel(1, 0) == 'B') // 128.0 -> B
    assert(result.pixel(0, 1) == 'B') // 255.0 -> B
    assert(result.pixel(1, 1) == 'A') // 0.0 -> A
  }

  test("Should preserve dimensions exactly") {
    // Arrange
    val inputImage = GrayscaleImageData(1, 1, Vector(100.0))

    // Act
    val result = converter.convert(inputImage)

    // Assert
    assert(result.width == 1)
    assert(result.height == 1)
  }
}