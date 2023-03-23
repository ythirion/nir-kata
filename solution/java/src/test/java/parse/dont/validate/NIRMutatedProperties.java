package parse.dont.validate;

import io.vavr.Function1;
import io.vavr.test.Arbitrary;
import io.vavr.test.Gen;
import io.vavr.test.Property;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static parse.dont.validate.NIRGenerator.validNIR;

class NIRMutatedProperties {
    private static final Random random = new Random();

    private record Mutator(String name, Function1<NIR, Gen<String>> func) {
        public String mutate(NIR nir) {
            return func.apply(nir).apply(random);
        }
    }

    private static Mutator sexMutator = new Mutator("Sex mutator", nir ->
            Gen.choose(3, 9)
                    .map(invalidSex -> invalidSex + nir.toString().substring(1))
    );

    private static Arbitrary<Mutator> mutators = Gen.choose(
            sexMutator).arbitrary();

    @Test
    void invalidNIRCanNeverBeParsed() {
        Property.def("parseNIR(nir.ToString()) == nir")
                .forAll(validNIR, mutators)
                .suchThat(NIRMutatedProperties::canNotParseMutatedNIR)
                .check()
                .assertIsSatisfied();
    }

    private static boolean canNotParseMutatedNIR(NIR nir, Mutator mutator) {
        return NIR.parseNIR(mutator.mutate(nir)).isLeft();
    }
}
