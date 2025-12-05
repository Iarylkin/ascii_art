package ascii.model.filter

import ascii.model.image.GrayscaleImageData
import org.scalatest.funsuite.AnyFunSuite

class FlipFilterTest extends AnyFunSuite {

  // Visualizing the 2x2 grid for tests:
  // (0,0)=A  (1,0)=B
  // (0,1)=C  (1,1)=D
  // Or in values:
  // 10  20
  // 30  40
  private val data = Vector(10.0, 20.0, 30.0, 40.0)
  private val image = GrayscaleImageData(2, 2, data)

  test("Should flip horizontally (Flip Y logic in code)") {
    // In your code: flipY = true changes the X coordinate.
    // Logic: Left becomes Right.
    // Expected:
    // 20 10
    // 40 30
    val filter = new FlipFilter(flipX = false, flipY = true)
    val result = filter.apply(image)

    assert(result.pixel(0, 0) == 20.0)
    assert(result.pixel(1, 0) == 10.0)
  }

  test("Should flip vertically (Flip X logic in code)") {
    // In your code: flipX = true changes the Y coordinate.
    // Logic: Top becomes Bottom.
    // Expected:
    // 30 40
    // 10 20
    val filter = new FlipFilter(flipX = true, flipY = false)
    val result = filter.apply(image)

    assert(result.pixel(0, 0) == 30.0)
    assert(result.pixel(0, 1) == 10.0)
  }

  test("Should flip both axes") {
    // Expected:
    // 40 30
    // 20 10
    val filter = new FlipFilter(flipX = true, flipY = true)
    val result = filter.apply(image)

    assert(result.pixel(0, 0) == 40.0)
    assert(result.pixel(1, 1) == 10.0)
  }

  test("Should do nothing if both flags are false") {
    val filter = new FlipFilter(flipX = false, flipY = false)
    val result = filter.apply(image)
    assert(result.getData == image.getData)
  }
}