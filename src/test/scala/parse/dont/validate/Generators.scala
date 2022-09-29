package parse.dont.validate

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Gen.{choose, frequency, oneOf}
import parse.dont.validate.NIRDomain.*

object Generators {
  val validNIRGenerator: Gen[NIR] =
    Arbitrary {
      for {
        sex <- oneOf(Sex.values)
        year <- choose(0, 99)
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
    }.arbitrary
}
