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
                Arguments.of("223119635012322", "incorrect department 2"),
                Arguments.of("2231159zzz12322", "incorrect city"),
                Arguments.of("223115935012321", "incorrect key"),
                Arguments.of("2231159350123221", "too long")
        );
    }

    public static Stream<Arguments> validNIRs() {
        return Stream.of(
                Arguments.of("223115935012322"),
                Arguments.of("200029923123486"),
                Arguments.of("254031088723464"),
                Arguments.of("195017262676215"),
                Arguments.of("155053933981739"),
                Arguments.of("106099955391094")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNIRs")
    void should_return_false(String input, String reason) {
        assertThat(NIR.validate(input))
                .as(reason)
                .isFalse();
    }

    @ParameterizedTest
    @MethodSource("validNIRs")
    void should_return_true(String input) {
        assertThat(NIR.validate(input))
                .isTrue();
    }
}
