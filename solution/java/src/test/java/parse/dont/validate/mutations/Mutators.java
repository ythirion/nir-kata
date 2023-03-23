package parse.dont.validate.mutations;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.test.Arbitrary;
import io.vavr.test.Gen;

public class Mutators {
    private static Gen<Integer> digits3Gen = Gen.frequency(
            Tuple.of(7, Gen.choose(1000, 9999)),
            Tuple.of(3, Gen.choose(1, 99))
    );

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

    private static Mutator serialNumberMutator = new Mutator("Serial Number mutator", nir ->
            digits3Gen.map(invalidSerialNumber -> concat(
                    nir.toString().substring(0, 10),
                    invalidSerialNumber,
                    nir.toString().substring(13))
            )
    );

    private static Mutator keyMutator = new Mutator("Key mutator", nir ->
            Gen.choose(0, 97)
                    .filter(x -> x != nir.key())
                    .map(invalidKey -> concat(
                            nir.toString().substring(0, 13),
                            String.format("%02d", invalidKey)
                    ))
    );

    private static String concat(Object... elements) {
        return List.of(elements).mkString();
    }

    private static Mutator truncateMutator = new Mutator("Truncate mutator", nir ->
            Gen.choose(1, 13).map(size ->
                    size == 1 ? "" : nir.toString().substring(0, size - 1)
            )
    );

    public static Arbitrary<Mutator> mutators = Gen.choose(
            sexMutator,
            yearMutator,
            departmentMutator,
            cityMutator,
            serialNumberMutator,
            keyMutator,
            truncateMutator
    ).arbitrary();
}