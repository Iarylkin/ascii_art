package ascii.model

import ascii.model.convert.tables.LinearTable
import ascii.model.convert.{AsciiConverter, Converter, GrayScaleConverter}
import ascii.model.filter.ImageFilter
import ascii.model.image.{AsciiImageData, GrayscaleImageData, RgbImageData}
import ascii.model.load.ImageSource
import ascii.model.save.{ConsoleSaver, Saver}

// The Process: Immutable definition of the workflow
class Pipeline(
                val imageSource: ImageSource,
                val grayConverter: Converter[RgbImageData, GrayscaleImageData],
                val filters: Seq[ImageFilter[GrayscaleImageData, GrayscaleImageData]],
                val asciiConverter: Converter[GrayscaleImageData, AsciiImageData],
                val savers: Seq[Saver]
              ) {
  def run(): Unit = {
    // 1. Load
    val rgb = imageSource.load()

    // 2. Convert to Gray
    val gray = grayConverter.convert(rgb)

    // 3. Apply Filters (Chain them sequentially)
    val filtered = filters.foldLeft(gray)((currentImage, filter) => filter.apply(currentImage))

    // 4. Convert to ASCII
    val ascii = asciiConverter.convert(filtered)

    // 5. Save/Output
    savers.foreach(_.save(ascii))
  }
}

// The Builder: Mutable helper to construct the Pipeline step-by-step
class PipelineBuilder {
  private var imageSource: Option[ImageSource] = None
  private var grayConverter: Option[Converter[RgbImageData, GrayscaleImageData]] = None
  private var filters: Seq[ImageFilter[GrayscaleImageData, GrayscaleImageData]] = Seq.empty
  private var asciiConverter: Option[Converter[GrayscaleImageData, AsciiImageData]] = None
  private var savers: Seq[Saver] = Seq.empty

  def withImageSource(src: ImageSource): PipelineBuilder = {
    imageSource = Some(src)
    this
  }

  def withGrayConverter(conv: Converter[RgbImageData, GrayscaleImageData]): PipelineBuilder = {
    grayConverter = Some(conv)
    this
  }

  def addFilter(f: ImageFilter[GrayscaleImageData, GrayscaleImageData]): PipelineBuilder = {
    filters = filters :+ f
    this
  }

  def withAsciiConverter(conv: Converter[GrayscaleImageData, AsciiImageData]): PipelineBuilder = {
    asciiConverter = Some(conv)
    this
  }

  def addSaver(s: Saver): PipelineBuilder = {
    savers = savers :+ s
    this
  }

  def build(): Pipeline = {
    new Pipeline(
      imageSource.getOrElse(throw new IllegalStateException("Image source must be set before running.")),
      grayConverter.getOrElse(new GrayScaleConverter()),
      filters,
      asciiConverter.getOrElse(new AsciiConverter(new LinearTable())),
      if (savers.nonEmpty) savers else Seq(new ConsoleSaver())
    )
  }
}
