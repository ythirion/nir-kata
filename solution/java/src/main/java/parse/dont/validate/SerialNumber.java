package parse.dont.validate;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import primitive.obsession.StringExtensions;

@EqualsAndHashCode
@ExtensionMethod(StringExtensions.class)
public class SerialNumber {
    private final int value;

    private SerialNumber(int value) {
        this.value = value;
    }

    public static Either<ParsingError, SerialNumber> parseSerialNumber(String potentialSerialNumber) {
        return potentialSerialNumber
                .toInt()
                .filter(x -> x > 0 && x <= 999)
                .toEither(new ParsingError("city should be gt 0 and lt 1000"))
                .map(SerialNumber::new);
    }

    public static SerialNumber fromInt(Integer x) {
        return parseSerialNumber(x.toString())
                .getOrElseThrow(() -> new IllegalArgumentException("SerialNumber"));
    }

    @Override
    public String toString() {
        return String.format("%03d", value);
    }
}
