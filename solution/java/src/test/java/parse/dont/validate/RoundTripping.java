package parse.dont.validate;

import io.vavr.test.Property;
import org.junit.jupiter.api.Test;

import static parse.dont.validate.NIRGenerator.validNIR;

class RoundTripping {
    @Test
    void roundTrip() {
        Property.def("parseNIR(nir.ToString()) == nir")
                .forAll(validNIR)
                .suchThat(nir -> NIR.parseNIR(nir.toString()).contains(nir))
                .check()
                .assertIsSatisfied();
    }
}
