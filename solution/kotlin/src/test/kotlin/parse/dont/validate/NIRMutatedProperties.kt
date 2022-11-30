package parse.dont.validate

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.forAll
import parse.dont.validate.Generators.nirGenerator
import parse.dont.validate.NIR.Parser.parseNIR

class NIRMutatedProperties : StringSpec({
    "mutated NIR can never be parsed" {
        forAll(nirGenerator, mutantGenerator) { validNIR, mutator ->
            collect(mutator.name)
            parseNIR(mutator.mutate(validNIR)).isFailure
        }
    }
})

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

private val monthMutator: Mutator = Mutator("month mutator") { nir ->
    Arb.int(13, 99).map { nir.toString().take(3) + it + nir.toString().substring(5) }
}

private val departmentMutator: Mutator = Mutator("department mutator") { nir ->
    Arb.frequency(
        9 to Arb.int(100, 999),
        1 to Arb.int(96, 98)
    ).map { nir.toString().take(5) + it + nir.toString().substring(7) }
}

private val digits3Gen = Arb.frequency(
    7 to Arb.int(1000, 9999),
    3 to Arb.int(1, 99)
)

private val cityMutator: Mutator = Mutator("city mutator") { nir ->
    digits3Gen.map { nir.toString().take(7) + it + nir.toString().substring(10) }
}

private val serialNumberMutator: Mutator = Mutator("serial number mutator") { nir ->
    digits3Gen.map { nir.toString().take(10) + it + nir.toString().substring(13) }
}

private val keyMutator: Mutator = Mutator("key mutator") { nir ->
    Arb.int(0, 97)
        .filter { k -> k != nir.key() }
        .map { nir.toString().take(13) + it }
}

private val truncateMutator: Mutator = Mutator("truncate mutator") { nir ->
    Arb.int(1, 14)
        .map { nir.toString().take(it) }
}

private val mutantGenerator: Arb<Mutator> = Arb.of(
    sexMutator,
    yearMutator,
    monthMutator,
    departmentMutator,
    cityMutator,
    serialNumberMutator,
    keyMutator,
    truncateMutator
)