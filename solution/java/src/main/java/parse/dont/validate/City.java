package parse.dont.validate;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import primitive.obsession.StringExtensions;

@EqualsAndHashCode
@ExtensionMethod(StringExtensions.class)
public class City {
    private final int value;

    private City(int value) {
        this.value = value;
    }

    public static Either<ParsingError, City> parseCity(String potentialCity) {
        return potentialCity
                .toInt()
                .filter(x -> x > 0 && x <= 999)
                .toEither(new ParsingError("city should be gt 0 and lt 1000"))
                .map(City::new);
    }

    public static City fromInt(Integer x) {
        return parseCity(x.toString())
                .getOrElseThrow(() -> new IllegalArgumentException("City"));
    }

    @Override
    public String toString() {
        return String.format("%03d", value);
    }
}
