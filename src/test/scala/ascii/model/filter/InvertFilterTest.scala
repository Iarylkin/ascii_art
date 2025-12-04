package ascii.model.filter

import ascii.model.image.GrayscaleImageData
import org.scalatest.funsuite.AnyFunSuite

class InvertFilterTest extends AnyFunSuite {

  private val filter = new InvertFilter()

  test("Should invert grayscale values correctly") {
    // Arrange
    // 0 (Black) -> should become 255 (White)
    // 255 (White) -> should become 0 (Black)
    // 100 -> 155
    val data = Vector(0.0, 255.0, 100.0)
    val image = GrayscaleImageData(3, 1, data)

    // Act
    val result = filter.apply(image)

    // Assert
    assert(result.pixel(0, 0) == 255.0)
    assert(result.pixel(1, 0) == 0.0)
    assert(result.pixel(2, 0) == 155.0)
  }

  test("Should maintain image dimensions") {
    val image = GrayscaleImageData(1, 1, Vector(10.0))
    val result = filter.apply(image)
    assert(result.width == 1)
    assert(result.height == 1)
  }
}