package parse.dont.validate;

import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.experimental.ExtensionMethod;
import primitive.obsession.StringExtensions;

@ExtensionMethod(StringExtensions.class)
public enum Month {
    Jan(1), Feb(2), Mar(3), Apr(4),
    May(5), Jun(6), Jul(7), Aug(8),
    Sep(9), Oct(10), Nov(11), Dec(12);

    private final int value;

    Month(int value) {
        this.value = value;
    }

    public static Either<ParsingError, Month> parseMonth(String potentialMonth) {
        return potentialMonth
                .toInt()
                .toEither(new ParsingError("Not a valid month"))
                .flatMap(Month::fromInt);
    }

    private static Either<ParsingError, Month> fromInt(int x) {
        return List.of(Month.values())
                .find(month -> month.value == x)
                .toEither(new ParsingError("Month should be between 1 and 12"));
    }

    @Override
    public String toString() {
        return String.format("%02d", value);
    }
}
