package ascii.controller

import ascii.model.{Pipeline, PipelineBuilder}
import ascii.model.convert.Converter
import ascii.model.filter.ImageFilter
import ascii.model.image.{AsciiImageData, GrayscaleImageData, RgbImageData, Pixel}
import ascii.model.load.ImageSource
import ascii.model.save.Saver
import ascii.view.View
import org.scalatest.funsuite.AnyFunSuite
import scala.collection.mutable.ListBuffer

class ControllerTest extends AnyFunSuite {

  // --- SPY VIEW ---
  class SpyView extends View {
    val events = ListBuffer[String]()
    var lastErrorMessage: Option[String] = None

    override def showHelp(): Unit = events += "help"
    override def showSuccess(): Unit = events += "success"

    override def showError(message: String): Unit = {
      events += "error"
      lastErrorMessage = Some(message)
    }
  }

  // --- SPY BUILDER ---
  class SpyPipelineBuilder extends PipelineBuilder {
    val events = ListBuffer[String]()

    override def withImageSource(src: ImageSource): PipelineBuilder = {
      events += "source"
      this
    }

    override def addFilter(f: ImageFilter[GrayscaleImageData, GrayscaleImageData]): PipelineBuilder = {
      events += "filter"
      this
    }

    override def withAsciiConverter(conv: Converter[GrayscaleImageData, AsciiImageData]): PipelineBuilder = {
      events += "converter"
      this
    }

    override def addSaver(s: Saver): PipelineBuilder = {
      events += "saver"
      this
    }

    override def build(): Pipeline = {
      events += "build"

      val stubSource = new ImageSource {
        override def load(): RgbImageData = RgbImageData(1, 1, Vector(Pixel(0,0,0)), "stub")
      }

      val stubGrayConverter = new Converter[RgbImageData, GrayscaleImageData] {
        override def convert(in: RgbImageData): GrayscaleImageData = GrayscaleImageData(1, 1, Vector(0.0))
      }

      val stubAsciiConverter = new Converter[GrayscaleImageData, AsciiImageData] {
        override def convert(in: GrayscaleImageData): AsciiImageData = AsciiImageData(1, 1, Vector(' '))
      }

      new Pipeline(stubSource, stubGrayConverter, Seq.empty, stubAsciiConverter, Seq.empty) {
        override def run(): Unit = {
        }
      }
    }
  }

  // --- TEST CASES ---

  test("Should show help when no arguments are provided") {
    val view = new SpyView()
    val controller = new Controller(view)
    controller.run(Array.empty)
    assert(view.events.contains("help"))
  }

  test("Should show error when arguments contain unknown commands") {
    val view = new SpyView()
    val controller = new Controller(view)
    val args = Array("--unknown-flag")

    controller.run(args)

    assert(view.events.contains("error"))
    assert(view.lastErrorMessage.exists(_.contains("Unknown")))
  }

  test("Should configure builder and attempt to run pipeline (Happy Path)") {
    // Arrange
    val view = new SpyView()
    val spyBuilder = new SpyPipelineBuilder()
    val controller = new Controller(view, spyBuilder)

    val args = Array(
      "--image-random",
      "--scale", "0.25",
      "--output-console"
    )

    // Act
    controller.run(args)

    // Assert
    assert(spyBuilder.events.contains("source"), "Should set image source")
    assert(spyBuilder.events.contains("filter"), "Should add filters")
    assert(spyBuilder.events.contains("saver"), "Should add saver")
    assert(spyBuilder.events.contains("build"), "Should call build()")

    if (view.events.contains("error")) {
      fail(s"Controller reported error: ${view.lastErrorMessage.getOrElse("Unknown")}")
    }

    assert(view.events.contains("success"), "Should report success to View")
  }

  test("Should show error if a handler fails (e.g. missing parameter)") {
    val view = new SpyView()
    val spyBuilder = new SpyPipelineBuilder()
    val controller = new Controller(view, spyBuilder)

    val args = Array("--scale")

    controller.run(args)

    assert(view.events.contains("error"))
    assert(view.lastErrorMessage.exists(msg => msg.contains("Invalid scale") || msg.contains("parameter")))
  }
}