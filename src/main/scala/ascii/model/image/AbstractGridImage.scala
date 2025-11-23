package ascii.model.image

abstract class AbstractGridImage[T](
                                     override val width: Int,
                                     override val height: Int,
                                     protected val data: Vector[T]
                                   ) extends ImageData[T] {

  require(width > 0 && height > 0, s"Dimensions must be positive, got ${width}x${height}")
  require(data.length == width * height, s"Data length (${data.length}) does not match dimensions ($width x $height = ${width * height})")

  override def pixel(x: Int, y: Int): T = {
    data(y * width + x)
  }
  def getData: Vector[T] = data
}
