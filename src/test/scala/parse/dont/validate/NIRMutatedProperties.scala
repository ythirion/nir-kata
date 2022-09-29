package parse.dont.validate

import org.scalacheck.Prop.{classify, collect, forAll}
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalatest.OptionValues
import parse.dont.validate.Generators.validNIRGenerator
import parse.dont.validate.NIRDomain.*
import parse.dont.validate.NIRDomain.NIR.parseNIR

object NIRMutatedProperties
    extends Properties("Mutated NIR")
    with OptionValues {
  type mutator = NIR => Gen[String]

  def nirGenerator: Gen[NIR] = validNIRGenerator().arbitrary
  def mutantGenerator: Gen[mutator] = Gen.oneOf(mutateSex, mutateSex)

  private val mutateSex: mutator =
    nir =>
      Gen
        .choose(3, 9)
        .map(c => c + nir.toString.substring(1))

  property("mutated NIR should not be parsed") =
    forAll(nirGenerator, mutantGenerator) { (nir, mutator) =>
      {
        classify(mutator == mutateSex, "sex mutator", "other") {
          val mutatedNIR = mutator(nir).sample.get
          parseNIR(mutatedNIR).isLeft
        }
      }
    }
}
