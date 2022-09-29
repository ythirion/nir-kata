package parse.dont.validate

import org.scalacheck.Gen.*
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.Checkers
import parse.dont.validate.NIRDomain.*
import parse.dont.validate.NIRDomain.Department.*
import parse.dont.validate.NIRDomain.NIR.parseNIR
import primitive.obsession.NIR.validate

object NIRProperties extends Properties("NIR") with EitherValues {
  implicit def nirGenerator: Arbitrary[NIR] =
    Arbitrary {
      for {
        sex <- oneOf(Sex.values)
        year <- choose(1, 99)
        month <- oneOf(Month.values)
        department <- frequency(
          (9, choose(1, 95)),
          (1, 99)
        )
        city <- choose(1, 999)
        serialNumber <- choose(1, 999)
      } yield new NIR(
        sex,
        Year(year),
        month,
        Department(department),
        City(city),
        SerialNumber(serialNumber)
      )
    }

  property("isomorphic") = forAll { (nir: NIR) =>
    val s = nir.toString
    parseNIR(s).value == nir
  }
}
