package ascii.model.filter

import ascii.model.image.GrayscaleImageData
import org.scalatest.funsuite.AnyFunSuite

class BrightnessFilterTest extends AnyFunSuite {

  test("Should increase brightness correctly") {
    // Arrange
    val data = Vector(10.0, 50.0)
    val image = GrayscaleImageData(2, 1, data)
    val filter = new BrightnessFilter(10)

    // Act
    val result = filter.apply(image)

    // Assert
    assert(result.pixel(0, 0) == 20.0)
    assert(result.pixel(1, 0) == 60.0)
  }

  test("Should decrease brightness correctly") {
    // Arrange
    val data = Vector(50.0)
    val image = GrayscaleImageData(1, 1, data)
    val filter = new BrightnessFilter(-20)

    // Act
    val result = filter.apply(image)

    // Assert
    assert(result.pixel(0, 0) == 30.0)
  }

  test("Should clamp values to max 255 (Upper bound)") {
    // Arrange
    val data = Vector(200.0, 250.0)
    val image = GrayscaleImageData(2, 1, data)
    val filter = new BrightnessFilter(100) // Huge increase

    // Act
    val result = filter.apply(image)

    // Assert
    assert(result.pixel(0, 0) == 255.0, "Should be capped at 255")
    assert(result.pixel(1, 0) == 255.0, "Should be capped at 255")
  }

  test("Should clamp values to min 0 (Lower bound)") {
    // Arrange
    val data = Vector(10.0, 50.0)
    val image = GrayscaleImageData(2, 1, data)
    val filter = new BrightnessFilter(-100) // Huge decrease

    // Act
    val result = filter.apply(image)

    // Assert
    assert(result.pixel(0, 0) == 0.0, "Should be limited to 0")
    assert(result.pixel(1, 0) == 0.0, "Should be limited to 0")
  }
}