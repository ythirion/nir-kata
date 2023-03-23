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
                Arguments.of("323115935012322", "incorrect sex"),
                Arguments.of("2ab115935012322", "incorrect year"),
                Arguments.of("223ab5935012322", "incorrect month"),
                Arguments.of("223145935012322", "incorrect month 2"),
                Arguments.of("223005935012322", "incorrect month 3"),
                Arguments.of("22311xx35012322", "incorrect department"),
                Arguments.of("223119635012322", "incorrect department 2")
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
