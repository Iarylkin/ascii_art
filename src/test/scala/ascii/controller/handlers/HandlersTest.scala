package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.convert.Converter
import ascii.model.filter.{BrightnessFilter, FlipFilter, ImageFilter, ScaleFilter}
import ascii.model.image.{AsciiImageData, GrayscaleImageData, RgbImageData}
import ascii.model.load.ImageSource
import ascii.model.save.Saver
import org.scalatest.funsuite.AnyFunSuite
import scala.collection.mutable.ListBuffer

class HandlersTest extends AnyFunSuite {

  // --- SPY BUILDER ---
  class SpyPipelineBuilder extends PipelineBuilder {
    val events = ListBuffer[String]()

    override def withImageSource(src: ImageSource): PipelineBuilder = {
      events += s"source:${src.getClass.getSimpleName}"
      this
    }

    override def addFilter(f: ImageFilter[GrayscaleImageData, GrayscaleImageData]): PipelineBuilder = {
      f match {
        case _: ScaleFilter => events += "filter:Scale"
        case _: FlipFilter => events += "filter:Flip"
        case _: BrightnessFilter => events += "filter:Brightness"
        // InvertFilter isn't a case class usually, so we check by class name or type
        case other => events += s"filter:${other.getClass.getSimpleName}"
      }
      this
    }

    override def withAsciiConverter(conv: Converter[GrayscaleImageData, AsciiImageData]): PipelineBuilder = {
      events += "converter:Set"
      this
    }

    override def addSaver(s: Saver): PipelineBuilder = {
      events += s"saver:${s.getClass.getSimpleName}"
      this
    }
  }

  // --- TEST CASES ---

  test("ScaleFilterHandler should add correct filter") {
    val handler = new ScaleFilterHandler()
    val builder = new SpyPipelineBuilder()
    val cmd = CommandDTO("scale", List("0.25"))

    handler.handle(cmd, builder)

    assert(builder.events.contains("filter:Scale"))
  }

  test("ScaleFilterHandler should throw exception on invalid number") {
    val handler = new ScaleFilterHandler()
    val builder = new SpyPipelineBuilder()
    val cmd = CommandDTO("scale", List("not-a-number"))

    assertThrows[IllegalArgumentException] {
      handler.handle(cmd, builder)
    }
  }

  test("FlipFilterHandler should handle x and y params") {
    val handler = new FlipFilterHandler()
    val builder = new SpyPipelineBuilder()

    // Test Flip X
    handler.handle(CommandDTO("flip", List("x")), builder)
    assert(builder.events.last == "filter:Flip")
  }

  test("AsciiTableHandler should set converter for known table") {
    val handler = new AsciiTableHandler()
    val builder = new SpyPipelineBuilder()
    // Assuming "gamma" exists in your TableRegistry used by the handler
    val cmd = CommandDTO("table", List("gamma"))

    handler.handle(cmd, builder)

    assert(builder.events.contains("converter:Set"))
  }

  test("AsciiTableHandler should throw exception for unknown table") {
    val handler = new AsciiTableHandler()
    val builder = new SpyPipelineBuilder()
    val cmd = CommandDTO("table", List("non-existent-table"))

    val error = intercept[IllegalArgumentException] {
      handler.handle(cmd, builder)
    }
    assert(error.getMessage.contains("Unknown table"))
  }

  test("ImageSourceHandler should set file source") {
    val handler = new ImageSourceHandler()
    val builder = new SpyPipelineBuilder()
    val cmd = CommandDTO("image", List("test.jpg"))

    handler.handle(cmd, builder)

    assert(builder.events.contains("source:FileImageSource"))
  }

  test("RandomImageHandler should set random source") {
    val handler = new RandomImageHandler()
    val builder = new SpyPipelineBuilder()
    val cmd = CommandDTO("image-random", Nil)

    handler.handle(cmd, builder)

    assert(builder.events.contains("source:RandomImageSource"))
  }

  test("FileOutputHandler should add file saver") {
    val handler = new FileOutputHandler()
    val builder = new SpyPipelineBuilder()
    val cmd = CommandDTO("output-file", List("out.txt"))

    handler.handle(cmd, builder)

    assert(builder.events.contains("saver:FileSaver"))
  }
}