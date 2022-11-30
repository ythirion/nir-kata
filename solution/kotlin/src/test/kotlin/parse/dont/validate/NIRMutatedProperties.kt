package parse.dont.validate

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.frequency
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.of
import io.kotest.property.forAll
import parse.dont.validate.Generators.nirGenerator
import parse.dont.validate.NIR.Parser.parseNIR

private data class Mutator(val name: String, val func: (NIR) -> Arb<String>) {
    fun mutate(nir: NIR): String = func(nir).samples().first().value
}

private val sexMutator: Mutator = Mutator("sex mutator") { nir ->
    Arb.int(3, 9).map {
        it.toString() + nir.toString().substring(1)
    }
}

private val yearMutator: Mutator = Mutator("year mutator") { nir ->
    Arb.frequency(
        7 to Arb.int(100, 999),
        3 to Arb.int(1, 9)
    ).map { nir.toString().take(1) + it + nir.toString().substring(3) }
}

private val mutantGenerator: Arb<Mutator> = Arb.of(
    sexMutator,
    yearMutator
)

class NIRMutatedProperties : StringSpec({
    "mutated NIR can never be parsed" {
        forAll(nirGenerator, mutantGenerator) { validNIR, mutator ->
            collect(mutator.name)
            parseNIR(mutator.mutate(validNIR)).isFailure
        }
    }
})