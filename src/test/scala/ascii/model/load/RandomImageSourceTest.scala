package ascii.model.load

import org.scalatest.funsuite.AnyFunSuite

class RandomImageSourceTest extends AnyFunSuite {

  test("Should generate identical images when the same seed is used") {
    val seed = 12345L
    val source1 = new RandomImageSource(Some(seed))
    val source2 = new RandomImageSource(Some(seed))

    val img1 = source1.load()
    val img2 = source2.load()

    assert(img1.width == img2.width)
    assert(img1.height == img2.height)

    assert(img1.pixel(0, 0) == img2.pixel(0, 0))
    val lastIdx = img1.getData.length - 1
    assert(img1.getData(lastIdx) == img2.getData(lastIdx))
  }

  test("Should generate different images for different seeds") {
    val source1 = new RandomImageSource(Some(1L))
    val source2 = new RandomImageSource(Some(2L))

    val img1 = source1.load()
    val img2 = source2.load()

    assert(img1.getData != img2.getData)
  }

  test("Should verify image dimensions are within expected bounds (20 to 200)") {
    val img = new RandomImageSource().load()

    assert(img.width >= 20 && img.width <= 200)
    assert(img.height >= 20 && img.height <= 200)
    assert(img.format == "random")
  }
}