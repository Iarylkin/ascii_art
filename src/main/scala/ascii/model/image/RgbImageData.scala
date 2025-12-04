package ascii.model.image

case class RgbImageData(
                         w: Int,
                         h: Int,
                         d: Vector[Pixel],
                         val format: String
                       ) extends AbstractGridImage[Pixel](w, h, d)
