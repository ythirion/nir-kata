package parse.dont.validate

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

object NIRProperties extends Properties("NIR") {
  property("demo prop") = forAll { (x: Int) =>
    x - x == 0
  }
}
