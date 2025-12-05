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
}