package scalaprops

import scala.util.Random

object RandTest extends Scalaprops{
  val testEqualLaw = scalazlaws.equal.all[Rand]

  val testRandChoose: Property = Property.forAll(
    List.fill(10000)((Random.nextLong, Random.nextInt, Random.nextInt)).forall {
      case (seed, y, z) =>
        val r = Rand.standard(seed).choose(y, z)._2
        val min = math.min(y, z)
        val max = math.max(y, z)
        (min <= r) && (r <= max)
    }
  )

  val chooseLong1 = Property.forAllG(Gen[Rand], Gen[Long], Gen[Long]){ (rng, y, z) =>
    val r = rng.chooseLong(y, z)._2
    val min = math.min(y, z)
    val max = math.max(y, z)
    (min <= r) && (r <= max)
  }.toProperties((), Param.minSuccessful(10000))

  val chooseLong2 = Property.forAllG(Gen[Long], Gen.choose(0, 30), Gen[Long]){ (a, b, seed) =>
    val c = a + b
    val max = if(c >= a) c else Long.MaxValue
    val x = Gen.chooseLong(a, max).samples(listSize = 1000, seed = seed).distinct.size
    x == (max - a + 1)
  }

}
