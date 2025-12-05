package ascii.model.filter

import ascii.model.image.GrayscaleImageData
import org.scalatest.funsuite.AnyFunSuite

class ScaleFilterTest extends AnyFunSuite {

  test("Should return original image for scale 1.0") {
    val image = GrayscaleImageData(1, 1, Vector(10.0))
    val filter = new ScaleFilter(1.0)
    val result = filter.apply(image)

    assert(result eq image, "Should return the same object instance for optimization")
  }

  test("Should scale up (scale 4.0 -> double dimensions)") {
    // Input 1x1: [100]
    // Expected 2x2:
    // 100 100
    // 100 100
    val image = GrayscaleImageData(1, 1, Vector(100.0))
    val filter = new ScaleFilter(4.0)

    // Act
    val result = filter.apply(image)

    // Assert
    assert(result.width == 2)
    assert(result.height == 2)
    assert(result.getData.forall(_ == 100.0))
  }

  test("Should scale down (scale 0.25 -> halve dimensions)") {
    // Input 2x2:
    // 10  10
    // 10  10
    // Average should be (10+10+10+10)/4 = 10
    val image = GrayscaleImageData(2, 2, Vector(10.0, 10.0, 10.0, 10.0))
    val filter = new ScaleFilter(0.25)

    // Act
    val result = filter.apply(image)

    // Assert
    assert(result.width == 1)
    assert(result.height == 1)
    assert(result.pixel(0, 0) == 10.0)
  }

  test("Should calculate average correctly when scaling down") {
    // Input 2x2:
    // 0   100
    // 100 200
    // Sum = 400, Count = 4, Avg = 100
    val image = GrayscaleImageData(2, 2, Vector(0.0, 100.0, 100.0, 200.0))
    val filter = new ScaleFilter(0.25)

    val result = filter.apply(image)
    assert(result.pixel(0, 0) == 100.0)
  }

  test("Should handle edge case: Scale down small image to minimum 1x1") {
    // If we have 1x1 image and try to scale down, it shouldn't become 0x0
    val image = GrayscaleImageData(1, 1, Vector(50.0))
    val filter = new ScaleFilter(0.25)

    val result = filter.apply(image)

    assert(result.width == 1)
    assert(result.height == 1)
  }

  test("Should throw exception for unsupported scales") {
    assertThrows[IllegalArgumentException] {
      new ScaleFilter(2.0).apply(GrayscaleImageData(1, 1, Vector(0.0)))
    }
  }
}