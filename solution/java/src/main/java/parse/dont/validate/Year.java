package parse.dont.validate;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import primitive.obsession.StringExtensions;

@EqualsAndHashCode
@ExtensionMethod(StringExtensions.class)
public class Year {
    private final int value;

    public Year(int value) {
        this.value = value;
    }

    public static Either<ParsingError, Year> parseYear(String input) {
        return input.toInt()
                .map(Year::new)
                .toEither(new ParsingError("year should be between 0 and 99"));
    }

    @Override
    public String toString() {
        return String.format("%02d", value);
    }
}