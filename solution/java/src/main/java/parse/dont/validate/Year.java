package parse.dont.validate;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import primitive.obsession.StringExtensions;

@EqualsAndHashCode
@ExtensionMethod(StringExtensions.class)
public class Year {
    private final int value;

    private Year(int value) {
        this.value = value;
    }

    public static Either<ParsingError, Year> parseYear(String potentialYear) {
        return potentialYear.toInt()
                .filter(x -> x >= 0 && x <= 99)
                .map(Year::new)
                .toEither(new ParsingError("year should be between 0 and 99"));
    }

    public static Year fromInt(Integer x) {
        return parseYear(x.toString())
                .getOrElseThrow(() -> new IllegalArgumentException("Year"));
    }

    @Override
    public String toString() {
        return String.format("%02d", value);
    }
}