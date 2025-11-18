package ascii.model.image

case class GrayscaleImageData(
                               override val width: Int,
                               override val height: Int,
                               pixels: Array[Double]
                             ) extends ImageData(width, height)
