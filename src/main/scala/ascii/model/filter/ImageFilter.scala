package ascii.model.filter

trait ImageFilter[I, O] {
  def apply(image: I): O
}