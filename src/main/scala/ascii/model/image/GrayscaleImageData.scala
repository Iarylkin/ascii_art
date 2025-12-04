package ascii.model.image

case class GrayscaleImageData(w: Int, h: Int, d: Vector[Double])
  extends AbstractGridImage[Double](w, h, d)
