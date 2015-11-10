package scalaprops

import cats.data.StreamingT
import cats.std.all._

object StreamingTTest extends Scalaprops {

  implicit def streamingTGen[F[_]: cats.Applicative, A: Gen]: Gen[StreamingT[F, A]] =
    Gen.oneOf(
      Gen.value(StreamingT.empty[F, A]),
      Gen[A].map(StreamingT(_)),
      Gen[List[A]].map(StreamingT.fromList(_))
    )

  val option = neko.monad.all[({type l[a] = StreamingT[Option, a]})#l]

  val list = neko.monad.all[({type l[a] = StreamingT[List, a]})#l].andThenParam(Param.maxSize(2))

  def filterTest[F[_]](implicit
    M: cats.MonadCombine[({type l[a] = StreamingT[F, a]})#l],
    F: cats.Monad[F],
    E1: cats.Eq[StreamingT[F, Byte]],
    E2: cats.Eq[F[List[Byte]]]
  ) = Property.forAll{ (s: StreamingT[F, Byte], f: Byte => Boolean) =>
    val x = s.filter(f)
    val y = M.flatMap(s)(a => if (f(a)) M.pure(a) else M.empty[Byte])
    if(E2.neqv(s.toList, x.toList)){
      println(List(s, x, y).map(_.toList))
    }
    E1.eqv(x, y)
  }.toProperties((), Param.minSuccessful(1000) andThen Param.maxSize(10))

  val filterList = filterTest[List]
  val filterOption = filterTest[Option]
}
