package ascii.model.image

case class AsciiImageData(w: Int, h: Int, d: Vector[Char])
  extends AbstractGridImage[Char](w, h, d)
