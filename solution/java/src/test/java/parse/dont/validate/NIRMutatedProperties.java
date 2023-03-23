package parse.dont.validate;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.test.Arbitrary;
import io.vavr.test.Gen;
import io.vavr.test.Property;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.vavr.API.println;
import static parse.dont.validate.NIRGenerator.validNIR;

class NIRMutatedProperties {
    private static final Random random = new Random();

    private static Gen<Integer> digits3Gen = Gen.frequency(
            Tuple.of(7, Gen.choose(1000, 9999)),
            Tuple.of(3, Gen.choose(1, 99))
    );

    private record Mutator(String name, Function1<NIR, Gen<String>> func) {
        public String mutate(NIR nir) {
            return func.apply(nir).apply(random);
        }
    }

    private static Mutator sexMutator = new Mutator("Sex mutator", nir ->
            Gen.choose(3, 9).map(invalidSex -> concat(invalidSex, nir.toString().substring(1)))
    );

    private static Mutator yearMutator = new Mutator("Year mutator", nir ->
            Gen.frequency(
                    Tuple.of(7, Gen.choose(100, 999)),
                    Tuple.of(3, Gen.choose(1, 9))
            ).map(invalidYear -> concat(
                            nir.toString().charAt(0),
                            invalidYear,
                            nir.toString().substring(3)
                    )
            )
    );

    private static Mutator departmentMutator = new Mutator("Department mutator", nir ->
            Gen.frequency(
                    Tuple.of(7, Gen.choose(100, 999)),
                    Tuple.of(3, Gen.choose(96, 98))
            ).map(invalidDepartment -> concat(
                            nir.toString().substring(0, 5),
                            invalidDepartment,
                            nir.toString().substring(7)
                    )
            )
    );

    private static Mutator cityMutator = new Mutator("City mutator", nir ->
            digits3Gen.map(invalidCity -> concat(
                    nir.toString().substring(0, 7),
                    invalidCity,
                    nir.toString().substring(10))
            )
    );

    private static String concat(Object... elements) {
        return List.of(elements).mkString();
    }

    private static Mutator truncateMutator = new Mutator("Truncate mutator", nir ->
            Gen.choose(1, 13).map(size ->
                    size == 1 ? "" : nir.toString().substring(0, size - 1)
            )
    );

    private static Arbitrary<Mutator> mutators = Gen.choose(
            sexMutator,
            yearMutator,
            departmentMutator,
            cityMutator,
            truncateMutator
    ).arbitrary();

    @Test
    void invalidNIRCanNeverBeParsed() {
        Property.def("parseNIR(nir.ToString()) == nir")
                .forAll(validNIR, mutators)
                .suchThat(NIRMutatedProperties::canNotParseMutatedNIR)
                .check()
                .assertIsSatisfied();
    }

    private static boolean canNotParseMutatedNIR(NIR nir, Mutator mutator) {
        var mutatedNIR = mutator.mutate(nir);
        println("NIR: " + nir + " Mutator: " + mutator.name + " / " + mutatedNIR);
        return NIR.parseNIR(mutatedNIR).isLeft();
    }
}
