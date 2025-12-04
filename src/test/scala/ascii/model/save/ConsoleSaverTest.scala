package ascii.model.save

import ascii.model.image.AsciiImageData
import org.scalatest.funsuite.AnyFunSuite
import java.io.ByteArrayOutputStream

class ConsoleSaverTest extends AnyFunSuite {

  test("Should print correctly to Standard Output") {
    // Arrange
    val outputStream = new ByteArrayOutputStream()
    val saver = new ConsoleSaver()
    val data = AsciiImageData(2, 1, Vector('H', 'i'))

    // Act
    Console.withOut(outputStream) {
      saver.save(data)
    }

    // Assert
    val output = outputStream.toString
    assert(output.trim == "Hi")
  }
}