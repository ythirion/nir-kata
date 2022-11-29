package primitive.obsession

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe

private val invalidNIRs = table(
    headers("input"),
    row("2230"),
    row("323115935012322"),
    row("2ab115935012322"),
    row("223ab5935012322"),
    row("223145935012322"),
    row("223005935012322"),
    row("22311xx35012322"),
    row("223119635012322"),
    row("2231159zzz12322"),
    row("2231159123zzz22"),
    row("223115935012321")
)

class ValidateNIR : StringSpec({
    "it should return false for invalid NIRs" {
        forAll(invalidNIRs) { invalidNIR ->
            NIR.validateNIR(invalidNIR) shouldBe false
        }
    }
})