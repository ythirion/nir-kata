package primitive.obsession

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.prop.Tables.Table
import primitive.obsession.NIR._
import primitive.obsession.ValidateNIRShould.{invalidNirs, validNirs}

class ValidateNIRShould
    extends AnyFlatSpec
    with Matchers
    with TableDrivenPropertyChecks {

  "validate" should "return false" in {
    forAll(invalidNirs) { (input, reason) =>
      assert(!validate(input), reason)
    }
  }

  "validate" should "return true" in {
    forAll(validNirs) { input =>
      assert(validate(input))
    }
  }
}

object ValidateNIRShould {
  private val invalidNirs =
    Table(
      ("input", "reason"),
      ("2230", "too short"),
      ("323115935012322", "incorrect sex"),
      ("2ab115935012322", "incorrect year"),
      ("223ab5935012322", "incorrect month"),
      ("223145935012322", "incorrect month 2"),
      ("223005935012322", "incorrect month 3"),
      ("22311xx35012322", "incorrect department"),
      ("223119635012322", "incorrect department 2"),
      ("2231159zzz12322", "incorrect city"),
      ("2231159123zzz22", "incorrect serial number"),
      ("223115935012321", "incorrect control key")
    )

  private val validNirs =
    Table(
      "input",
      "223115935012322",
      "200029923123486"
    )
}
