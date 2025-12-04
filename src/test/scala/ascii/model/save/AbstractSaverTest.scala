package ascii.model.save

import ascii.model.image.AsciiImageData
import org.scalatest.funsuite.AnyFunSuite
import scala.collection.mutable.ListBuffer

class AbstractSaverTest extends AnyFunSuite {

  class SpySaver extends AbstractSaver {
    val events = ListBuffer[String]()

    override protected def open(): Unit = events += "open"
    override protected def close(): Unit = events += "close"
    override protected def writeChar(c: Char): Unit = events += s"char:$c"
    override protected def writeNewLine(): Unit = events += "newline"
  }

  class BrokenSaver extends AbstractSaver {
    var isClosed = false

    override protected def writeChar(c: Char): Unit = throw new RuntimeException("Write error")
    override protected def writeNewLine(): Unit = {}

    override protected def close(): Unit = {
      isClosed = true
    }
  }

  test("Should execute open -> write -> newline -> close sequence correctly") {
    // Arrange
    val saver = new SpySaver()
    val data = AsciiImageData(2, 2, Vector('A', 'B', 'C', 'D'))

    // Act
    saver.save(data)

    // Assert
    val expectedEvents = List(
      "open",
      "char:A", "char:B", "newline",
      "char:C", "char:D", "newline",
      "close"
    )
    assert(saver.events.toList == expectedEvents)
  }

  test("Should call close() even if writing fails (Exception safety)") {
    // Arrange
    val saver = new BrokenSaver() // Use the named class
    val data = AsciiImageData(1, 1, Vector('A'))

    // Act
    assertThrows[RuntimeException] {
      saver.save(data)
    }

    // Assert
    assert(saver.isClosed, "close() must be called in finally block")
  }
}