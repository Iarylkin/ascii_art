package ascii.model.save

import ascii.model.image.AsciiImageData
import org.scalatest.funsuite.AnyFunSuite
import java.io.File
import scala.io.Source

class FileSaverTest extends AnyFunSuite {

  test("Should save ASCII content to a real file") {
    // Arrange
    val tempFile = File.createTempFile("ascii_output", ".txt")
    tempFile.deleteOnExit()

    val saver = new FileSaver(tempFile.getAbsolutePath)
    val data = AsciiImageData(2, 2, Vector('X', 'Y', 'Z', 'Q'))

    // Act
    saver.save(data)

    // Assert
    val source = Source.fromFile(tempFile)
    val content = try source.mkString finally source.close()

    // We expect:
    // XY
    // ZQ
    // We check if it contains the characters.
    assert(content.contains("XY"))
    assert(content.contains("ZQ"))
    // Ensure content length is reasonable (4 chars + 2 newlines)
    assert(content.length >= 6)
  }
}