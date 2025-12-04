package ascii.model.image

trait ImageData[T] {
  def width: Int
  def height: Int

  def pixel(x: Int, y: Int): T
}
