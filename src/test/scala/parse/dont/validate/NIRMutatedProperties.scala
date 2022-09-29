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
  type mutator = NIR => Gen[String]

  def nirGenerator: Gen[NIR] = validNIRGenerator.arbitrary
  def mutantGenerator: Gen[mutator] =
    oneOf(
      sexMutator,
      yearMutator,
      monthMutator,
      departmentMutator,
      cityMutator,
      serialNumberMutator,
      keyMutator
    )

  private val sexMutator: mutator = nir =>
    choose(3, 9)
      .map(s => s + nir.toString.substring(1))

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

  private val yearMutator: mutator = nir =>
    digits2Gen.map(y => nir.toString.take(1) + y + nir.toString.substring(3))

  private val monthMutator: mutator = nir =>
    digits2Gen.map(m => nir.toString.take(3) + m + nir.toString.substring(5))

  private val departmentMutator: mutator = nir =>
    frequency(
      (9, digits2Gen),
      (1, choose(96, 98))
    ).map(d => nir.toString.take(5) + d + nir.toString.substring(7))

  private val cityMutator: mutator = nir =>
    digits3Gen.map(c => nir.toString.take(7) + c + nir.toString.substring(10))

  private val serialNumberMutator: mutator = nir =>
    digits3Gen.map(s => nir.toString.take(10) + s + nir.toString.substring(13))

  private val keyMutator: mutator = nir =>
    choose(0, 99)
      .filter(key => Key(key) != nir.calculateKey())
      .map(k => nir.toString.take(13) + k)

  property("can never be parsed") = forAll(nirGenerator, mutantGenerator) {
    (nir, mutator) =>
      {
        classify(mutator == sexMutator, "sex mutator") {
          classify(mutator == yearMutator, "year mutator") {
            classify(mutator == monthMutator, "month mutator") {
              classify(mutator == departmentMutator, "department mutator") {
                classify(mutator == cityMutator, "city mutator") {
                  classify(mutator == serialNumberMutator, "serial mutator") {
                    classify(mutator == keyMutator, "key mutator") {
                      mutator(nir).sample match {
                        case Some(m) => parseNIR(m).isLeft
                        case _ => false
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
  }
}
