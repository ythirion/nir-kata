package parse.dont.validate;

import io.vavr.test.Arbitrary;
import io.vavr.test.Gen;
import io.vavr.test.Property;
import org.junit.jupiter.api.Test;

class NIRProperties {
    private final Gen<Sex> sexGenerator = Gen.choose(Sex.values());
    private final Arbitrary<NIR> validNIR =
            sexGenerator.map(NIR::new)
                    .arbitrary();

    @Test
    void roundTrip() {
        Property.def("parseNIR(nir.ToString()) == nir")
                .forAll(validNIR)
                .suchThat(nir -> NIR.parseNIR(nir.toString()).contains(nir))
                .check()
                .assertIsSatisfied();
    }
}
