package primitive.obsession;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ValidateNIR {
    public static Stream<Arguments> invalidNIRs() {
        return Stream.of(
                Arguments.of("", "empty string"),
                Arguments.of("2230", "too short"),
                Arguments.of("323115935012322", "incorrect sex")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNIRs")
    void should_return_false(String input, String reason) {
        assertThat(NIR.validate(input))
                .isFalse()
                .as(reason);
    }
}
