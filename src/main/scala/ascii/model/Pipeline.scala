package ascii.model

import ascii.model.convert.tables.TableRegistry
import ascii.model.convert.{AsciiConverter, Converter, GrayScaleConverter}
import ascii.model.filter.ImageFilter
import ascii.model.image.{AsciiImageData, GrayscaleImageData, RgbImageData}
import ascii.model.load.ImageSource
import ascii.model.save.{ConsoleSaver, Saver}

/**
 * Orchestrates the entire image processing workflow.
 *
 * Data flows through the pipeline in the following order:
 * 1. ImageSource (Load)
 * 2. GrayScaleConverter (RGB -> Gray)
 * 3. Filters (Transform Gray image sequentially)
 * 4. AsciiConverter (Gray -> Text)
 * 5. Savers (Output)
 */
class Pipeline(
                val imageSource: ImageSource,
                val grayConverter: Converter[RgbImageData, GrayscaleImageData],
                val filters: Seq[ImageFilter[GrayscaleImageData, GrayscaleImageData]],
                val asciiConverter: Converter[GrayscaleImageData, AsciiImageData],
                val savers: Seq[Saver]
              ) {

  /**
   * Executes the pipeline.
   * Loads the image, processes it, and outputs the result.
   */
  def run(): Unit = {
    val rgb = imageSource.load()

    val gray = grayConverter.convert(rgb)

    // Apply all filters sequentially using functional folding
    val filtered = filters.foldLeft(gray)((currentImage, filter) => filter.apply(currentImage))

    val ascii = asciiConverter.convert(filtered)

    savers.foreach(_.save(ascii))
  }
}

/**
 * Builder pattern implementation for configuring the Pipeline.
 *
 * Allows for fluent configuration of the processing steps.
 * Provides sensible defaults for converters and savers if they are not explicitly set.
 */
class PipelineBuilder {
  private var imageSource: Option[ImageSource] = None
  private var grayConverter: Option[Converter[RgbImageData, GrayscaleImageData]] = None
  private var filters: Seq[ImageFilter[GrayscaleImageData, GrayscaleImageData]] = Seq.empty
  private var asciiConverter: Option[Converter[GrayscaleImageData, AsciiImageData]] = None
  private var savers: Seq[Saver] = Seq.empty

  private val tableRegistry = new TableRegistry()

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

  /**
   * Constructs the Pipeline instance.
   *
   * @return A configured Pipeline.
   * @throws IllegalStateException if the image source is not set.
   */
  def build(): Pipeline = {
    new Pipeline(
      imageSource.getOrElse(throw new IllegalStateException("ImageData source must be set before running.")),
      grayConverter.getOrElse(new GrayScaleConverter()),
      filters,
      asciiConverter.getOrElse(new AsciiConverter(tableRegistry.defaultTable)),
      if (savers.nonEmpty) savers else Seq(new ConsoleSaver())
    )
  }
}