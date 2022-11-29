package primitive.obsession

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.forAll

class ValidateNIR : StringSpec({
    "returns false" {
        forAll<String> { invalidNIR ->
            !NIR.validateNIR(invalidNIR)
        }
    }
})