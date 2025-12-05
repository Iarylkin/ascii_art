package ascii.model.load

import org.scalatest.funsuite.AnyFunSuite
import java.io.File

class FileImageSourceTest extends AnyFunSuite {

  test("Should throw IllegalArgumentException if file does not exist") {
    val source = new FileImageSource("non_existent_file_12345.jpg")

    val error = intercept[IllegalArgumentException] {
      source.load()
    }
    assert(error.getMessage.contains("File not found"))
  }

  test("Should throw IllegalArgumentException if extension is unsupported") {
    val tempFile = File.createTempFile("readme", ".txt")
    tempFile.deleteOnExit()

    val source = new FileImageSource(tempFile.getAbsolutePath)

    val error = intercept[IllegalArgumentException] {
      source.load()
    }
    assert(error.getMessage.contains("Unsupported extension"))
  }

}
