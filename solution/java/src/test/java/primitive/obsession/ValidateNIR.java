package primitive.obsession;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidateNIR {
    @Test
    void validate_empty_string_should_return_false() {
        assertThat(NIR.validate(""))
                .isFalse();
    }

    @Test
    void validate_short_string_should_return_false() {
        assertThat(NIR.validate("2230"))
                .isFalse();
    }
}
