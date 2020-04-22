package todo

package object models {
  type Id = Long

  implicit def intToId(int: Int): Id = int
}
