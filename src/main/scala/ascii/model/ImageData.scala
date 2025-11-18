package ascii.model

sealed abstract class ImageData(val width: Int, val height: Int)

case class RgbImageData(
                         override val width: Int,
                         override val height: Int,
                         pixels: Array[Pixel],
                         format: String
                       ) extends ImageData(width, height)

case class GrayscaleImageData(
                               override val width: Int,
                               override val height: Int,
                               pixels: Array[Double]
                             ) extends ImageData(width, height)

case class AsciiImageData(
                           override val width: Int,
                           override val height: Int,
                           content: String
                         ) extends ImageData(width, height)
