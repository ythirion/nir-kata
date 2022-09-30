package parse.dont.validate

import org.scalacheck.Gen.*
import org.scalacheck.Prop.{classify, collect, falsified, forAll}
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalatest.OptionValues
import parse.dont.validate.Generators.validNIR
import parse.dont.validate.NIRDomain.*
import parse.dont.validate.NIRDomain.NIR.parseNIR

object NIRMutatedProperties
    extends Properties("Mutated NIR")
    with OptionValues {

  private case class Mutator(name: String, func: NIR => Gen[String]) {
    def mutate(nir: NIR): String = func(nir).sample.value
  }

  private def mutantGenerator: Gen[Mutator] =
    oneOf(
      sexMutator,
      yearMutator,
      monthMutator,
      departmentMutator,
      cityMutator,
      serialNumberMutator,
      keyMutator,
      truncateMutator
    )

  private val sexMutator: Mutator =
    Mutator(
      "sex mutator",
      nir =>
        choose(3, 9)
          .map(s => s + nir.toString.substring(1))
    )

  private val yearMutator: Mutator =
    Mutator(
      "year mutator",
      nir =>
        frequency(
          (7, choose(100, 999)),
          (3, choose(1, 9))
        ).map(y => nir.toString.take(1) + y + nir.toString.substring(3))
    )

  private val monthMutator: Mutator =
    Mutator(
      "month mutator",
      nir =>
        choose(13, 99)
          .map(m => nir.toString.take(3) + m + nir.toString.substring(5))
    )

  private val departmentMutator: Mutator =
    Mutator(
      "department mutator",
      nir =>
        frequency(
          (9, choose(100, 999)),
          (1, choose(96, 98))
        ).map(d => nir.toString.take(5) + d + nir.toString.substring(7))
    )

  private val cityMutator: Mutator =
    Mutator(
      "city mutator",
      nir =>
        digits3Gen.map(c =>
          nir.toString.take(7) + c + nir.toString.substring(10)
        )
    )

  private val serialNumberMutator: Mutator =
    Mutator(
      "serial mutator",
      nir =>
        digits3Gen.map(s =>
          nir.toString.take(10) + s + nir.toString.substring(13)
        )
    )

  private val keyMutator: Mutator =
    Mutator(
      "key mutator",
      nir =>
        oneOf(
          (0 to 97)
            .filter(x => x != nir.calculateKey().value)
        ).map(k => nir.toString.take(13) + k)
    )

  private val truncateMutator: Mutator =
    Mutator(
      "truncate mutator",
      nir =>
        choose(1, 14)
          .map(size => nir.toString.take(size))
    )

  private val digits3Gen =
    frequency(
      (7, choose(1000, 9999)),
      (3, choose(1, 99))
    )

  property("can never be parsed") = forAll(validNIR, mutantGenerator) {
    (nir, mutator) =>
      {
        classify(true, mutator.name) {
          parseNIR(mutator.mutate(nir)).isLeft
        }
      }
  }
}
