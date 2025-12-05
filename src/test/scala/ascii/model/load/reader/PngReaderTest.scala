package ascii.model.load.reader

import org.scalatest.funsuite.AnyFunSuite
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import ascii.model.image.Pixel

class PngReaderTest extends AnyFunSuite {

  test("Should correctly read a PNG file and convert pixels") {
    val tempFile = File.createTempFile("test_image", ".png")

    try {
      val bufferedImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB)
      bufferedImage.setRGB(0, 0, 0xFF0000)
      bufferedImage.setRGB(1, 0, 0x00FF00)
      bufferedImage.setRGB(0, 1, 0x0000FF)
      bufferedImage.setRGB(1, 1, 0xFFFFFF)

      ImageIO.write(bufferedImage, "png", tempFile)

      val reader = new PngReader()

      val resultImage = reader.read(tempFile)

      assert(resultImage.width == 2)
      assert(resultImage.height == 2)
      assert(resultImage.format == "png")
      assert(resultImage.pixel(0, 0) == Pixel(255, 0, 0))
      assert(resultImage.pixel(1, 0) == Pixel(0, 255, 0))
      assert(resultImage.pixel(0, 1) == Pixel(0, 0, 255))
      assert(resultImage.pixel(1, 1) == Pixel(255, 255, 255))

    } finally {
      tempFile.delete()
    }
  }

  test("Should throw exception when file does not exist") {
    val reader = new PngReader()
    val nonExistentFile = new File("ghost.png")

    assertThrows[IllegalArgumentException] {
      reader.read(nonExistentFile)
    }
  }
}