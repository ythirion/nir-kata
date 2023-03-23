package parse.dont.validate;

import io.vavr.test.Arbitrary;
import io.vavr.test.Property;
import org.junit.jupiter.api.Test;

class NIRProperties {
    private Arbitrary<NIR> validNIR = Arbitrary.of(new NIR());

    @Test
    void roundTrip() {
        Property.def("parseNIR(nir.ToString()) == nir")
                .forAll(validNIR)
                .suchThat(nir -> NIR.parse(nir.toString()).contains(nir))
                .check()
                .assertIsSatisfied();
    }
}
