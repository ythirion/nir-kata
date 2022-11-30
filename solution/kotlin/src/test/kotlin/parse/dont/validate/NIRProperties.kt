package parse.dont.validate

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.forAll
import parse.dont.validate.Generators.nirGenerator
import parse.dont.validate.NIR.Parser.parseNIR

class NIRProperties : StringSpec({
    "Nir round tripping" {
        forAll(nirGenerator) { validNIR ->
            parseNIR(validNIR.toString()).getOrNull() == validNIR
        }
    }
})