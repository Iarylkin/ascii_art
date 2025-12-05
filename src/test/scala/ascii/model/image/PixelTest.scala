package ascii.model.image

import org.scalatest.funsuite.AnyFunSuite

class PixelTest extends AnyFunSuite {

  test("Should correctly store RGB values") {
    val p = Pixel(10, 20, 30)
    assert(p.r == 10)
    assert(p.g == 20)
    assert(p.b == 30)
  }

  test("Should be equal to another Pixel with same values (Equality)") {
    val p1 = Pixel(255, 0, 0)
    val p2 = Pixel(255, 0, 0)
    val p3 = Pixel(0, 255, 0)

    assert(p1 == p2)
    assert(p1 != p3)
  }

  test("Should accept valid boundary values (0 and 255)") {
    // Should not throw exceptions
    val black = Pixel(0, 0, 0)
    val white = Pixel(255, 255, 255)

    assert(black.r == 0)
    assert(white.r == 255)
  }

  test("Should throw IllegalArgumentException for invalid RGB values") {
    // Negative values
    assertThrows[IllegalArgumentException] { Pixel(-1, 10, 10) }
    assertThrows[IllegalArgumentException] { Pixel(10, -1, 10) }
    assertThrows[IllegalArgumentException] { Pixel(10, 10, -5) }

    // Values > 255
    assertThrows[IllegalArgumentException] { Pixel(256, 10, 10) }
    assertThrows[IllegalArgumentException] { Pixel(10, 300, 10) }
    assertThrows[IllegalArgumentException] { Pixel(10, 10, 999) }
  }
}