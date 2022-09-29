package parse.dont.validate

import org.scalacheck.Gen.*
import org.scalacheck.Prop.{classify, collect, falsified, forAll}
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalatest.OptionValues
import parse.dont.validate.Generators.validNIRGenerator
import parse.dont.validate.NIRDomain.*
import parse.dont.validate.NIRDomain.NIR.parseNIR

object NIRMutatedProperties
    extends Properties("Mutated NIR")
    with OptionValues {
  type mutator = NIR => (String, Gen[String])

  def mutantGenerator: Gen[mutator] =
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

  private val sexMutator: mutator = nir =>
    (
      "sex mutator",
      choose(3, 9)
        .map(s => s + nir.toString.substring(1))
    )

  private val yearMutator: mutator = nir =>
    (
      "year mutator",
      digits2Gen.map(y => nir.toString.take(1) + y + nir.toString.substring(3))
    )

  private val monthMutator: mutator = nir =>
    (
      "month mutator",
      choose(13, 99)
        .map(m => nir.toString.take(3) + m + nir.toString.substring(5))
    )

  private val departmentMutator: mutator = nir =>
    (
      "department mutator",
      frequency(
        (9, choose(100, 999)),
        (1, choose(96, 98))
      ).map(d => nir.toString.take(5) + d + nir.toString.substring(7))
    )

  private val cityMutator: mutator = nir =>
    (
      "city mutator",
      digits3Gen.map(c => nir.toString.take(7) + c + nir.toString.substring(10))
    )

  private val serialNumberMutator: mutator = nir =>
    (
      "serial mutator",
      digits3Gen.map(s =>
        nir.toString.take(10) + s + nir.toString.substring(13)
      )
    )

  private val keyMutator: mutator = nir =>
    (
      "key mutator",
      choose(0, 99)
        .filter(key => key != nir.calculateKey().value)
        .map(k => nir.toString.take(13) + k)
    )

  private val truncateMutator: mutator = nir =>
    (
      "truncate mutator",
      choose(1, 14)
        .map(size => nir.toString.take(size))
    )

  private val digits2Gen =
    frequency(
      (7, choose(100, 999)),
      (3, choose(1, 9))
    )

  private val digits3Gen =
    frequency(
      (7, choose(1000, 9999)),
      (3, choose(1, 99))
    )

  property("can never be parsed") = forAll(validNIRGenerator, mutantGenerator) {
    (nir, mutator) =>
      {
        classify(true, mutator(nir)._1) {
          mutator(nir)._2.sample match {
            case Some(m) => parseNIR(m).isLeft
            case _ => false
          }
        }
      }
  }

  // TODO investigate:  failing seed for Mutated NIR.can never be parsed is u-cD4nAzvmsenpbtzB4W9BOGUlPtRngbfuzONB1t97I=
  // [info] > ARG_0: 246012405219215
  // [info] > ARG_1: parse.dont.validate.NIRMutatedProperties$$$Lambda$15116/0x0000000802ffafd0@64a07a1e
}
