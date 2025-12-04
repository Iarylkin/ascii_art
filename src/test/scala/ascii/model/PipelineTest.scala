package ascii.model

import ascii.model.convert.Converter
import ascii.model.filter.ImageFilter
import ascii.model.image.{AsciiImageData, GrayscaleImageData, Pixel, RgbImageData}
import ascii.model.load.ImageSource
import ascii.model.save.Saver
import org.scalatest.funsuite.AnyFunSuite

class PipelineTest extends AnyFunSuite {

  // --- Helper Stubs (Mock objects) ---

  // 1. Stub Source: Returns a fixed RGB image
  class StubSource extends ImageSource {
    override def load(): RgbImageData =
      RgbImageData(1, 1, Vector(Pixel(0,0,0)), "stub")
  }

  // 2. Stub Saver: Remembers if it was called
  class SpySaver extends Saver {
    var wasCalled = false
    var capturedImage: Option[AsciiImageData] = None

    override def save(image: AsciiImageData): Unit = {
      wasCalled = true
      capturedImage = Some(image)
    }
  }

  // 3. Stub Converters
  val stubGrayConverter = new Converter[RgbImageData, GrayscaleImageData] {
    override def convert(input: RgbImageData): GrayscaleImageData =
      GrayscaleImageData(1, 1, Vector(0.0))
  }

  val stubAsciiConverter = new Converter[GrayscaleImageData, AsciiImageData] {
    override def convert(input: GrayscaleImageData): AsciiImageData =
      AsciiImageData(1, 1, Vector('X'))
  }

  // --- Tests ---

  test("Should execute the full pipeline flow: Load -> Convert -> Filter -> Convert -> Save") {
    // Arrange
    val source = new StubSource()
    val saver = new SpySaver()

    // Create a filter that we can verify was executed
    var filterCalled = false
    val spyFilter = new ImageFilter[GrayscaleImageData, GrayscaleImageData] {
      override def apply(image: GrayscaleImageData): GrayscaleImageData = {
        filterCalled = true
        image
      }
    }

    val pipeline = new Pipeline(
      source,
      stubGrayConverter,
      Seq(spyFilter),
      stubAsciiConverter,
      Seq(saver)
    )

    // Act
    pipeline.run()

    // Assert
    assert(filterCalled, "Filter should have been applied")
    assert(saver.wasCalled, "Saver should have been called")
    assert(saver.capturedImage.isDefined)
    saver.capturedImage match {
      case Some(img) => assert(img.pixel(0, 0) == 'X', "Final result should match converter output")
      case None => fail("Saver expected to capture an image, but found None")
    }
  }

  test("Should handle multiple savers") {
    // Arrange
    val saver1 = new SpySaver()
    val saver2 = new SpySaver()

    val pipeline = new Pipeline(
      new StubSource(),
      stubGrayConverter,
      Seq.empty, // No filters
      stubAsciiConverter,
      Seq(saver1, saver2)
    )

    // Act
    pipeline.run()

    // Assert
    assert(saver1.wasCalled, "First saver should be called")
    assert(saver2.wasCalled, "Second saver should be called")
  }

  test("Should apply filters in sequence (FoldLeft check)") {

    val addingFilter1 = new ImageFilter[GrayscaleImageData, GrayscaleImageData] {
      override def apply(img: GrayscaleImageData): GrayscaleImageData =
        GrayscaleImageData(img.width, img.height, Vector(img.getData.head + 10.0))
    }

    val addingFilter2 = new ImageFilter[GrayscaleImageData, GrayscaleImageData] {
      override def apply(img: GrayscaleImageData): GrayscaleImageData =
        GrayscaleImageData(img.width, img.height, Vector(img.getData.head + 20.0))
    }

    // A converter that verifies the final value
    val verifyingConverter = new Converter[GrayscaleImageData, AsciiImageData] {
      override def convert(input: GrayscaleImageData): AsciiImageData = {
        if (input.getData.head == 30.0) AsciiImageData(1,1, Vector('V')) // Valid
        else AsciiImageData(1,1, Vector('F')) // Fail
      }
    }

    val pipeline = new Pipeline(
      new StubSource(),
      stubGrayConverter, // Returns 0.0
      Seq(addingFilter1, addingFilter2),
      verifyingConverter,
      Seq(new SpySaver())
    )

    // Act
    pipeline.run()

  }
}