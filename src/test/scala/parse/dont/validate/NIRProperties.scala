package parse.dont.validate

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalatest.EitherValues
import parse.dont.validate.Generators.validNIRGenerator
import parse.dont.validate.NIRDomain.*
import parse.dont.validate.NIRDomain.NIR.parseNIR

object NIRProperties extends Properties("NIR") with EitherValues {
  property("isomorphic") = forAll(validNIRGenerator) { nir =>
    parseNIR(nir.toString).value == nir
  }
}
