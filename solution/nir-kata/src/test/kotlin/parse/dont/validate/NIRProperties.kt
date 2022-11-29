package parse.dont.validate

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.long
import io.kotest.property.forAll
import parse.dont.validate.NIR.Parser.parseNIR

class NIRProperties : StringSpec({
    val nirGenerator = arbitrary { _ ->
        NIR(
            sex = Arb.enum<NIR.Sex>().bind(),
            year = NIR.Year(Arb.long(0, 100).bind())
        )
    }

    "Nir round tripping" {
        forAll(nirGenerator) { validNIR ->
            parseNIR(validNIR.toString()).getOrNull() == validNIR
        }
    }
})