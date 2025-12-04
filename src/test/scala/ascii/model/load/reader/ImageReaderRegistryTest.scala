package ascii.model.load.reader

import org.scalatest.funsuite.AnyFunSuite
import java.io.File

class ImageReaderRegistryTest extends AnyFunSuite {

  private val registry = ImageReaderRegistry.default

  test("Should pick PngReader for .png extension") {
    val file = new File("image.png")
    val readerOption = registry.pickFor(file)

    readerOption match {
      case Some(_: PngReader) => succeed
      case _ => fail("Expected PngReader")
    }
  }

  test("Should pick JpegReader for .jpg and .jpeg extensions") {
    registry.pickFor(new File("test.jpg")) match {
      case Some(_: JpegReader) => succeed
      case _ => fail("Expected JpegReader for .jpg")
    }

    registry.pickFor(new File("test.jpeg")) match {
      case Some(_: JpegReader) => succeed
      case _ => fail("Expected JpegReader for .jpeg")
    }
  }

  test("Should pick GifReader for .gif extension") {
    registry.pickFor(new File("animation.gif")) match {
      case Some(_: GifReader) => succeed
      case _ => fail("Expected GifReader")
    }
  }

  test("Should be case insensitive regarding extension") {
    val file = new File("IMAGE.PNG")
    registry.pickFor(file) match {
      case Some(_: PngReader) => succeed
      case _ => fail("Expected PngReader for uppercase extension")
    }
  }

  test("Should return None for unsupported extensions") {
    val file = new File("document.txt")
    assert(registry.pickFor(file).isEmpty)
  }

  test("Should return None for files without extension") {
    val file = new File("makefile")
    assert(registry.pickFor(file).isEmpty)
  }
}
