package scalaprops

import scalaprops.Property.forAllG
import scalaz.CorecursiveList
import scalaz.std.anyVal._

object CorecursiveListTest extends Scalaprops {

  implicit def gen[A: Gen]: Gen[CorecursiveList[A]] =
    Gen[Stream[A]].map(CorecursiveList.fromStream)

  val testLaws = Properties.list(
    scalazlaws.order.all[CorecursiveList[Byte]],
    scalazlaws.monadPlusStrong.all[CorecursiveList],
    scalazlaws.foldable.all[CorecursiveList],
    scalazlaws.isEmpty.all[CorecursiveList],
    scalazlaws.align.all[CorecursiveList],
    scalazlaws.zip.all[CorecursiveList]
  )

}
