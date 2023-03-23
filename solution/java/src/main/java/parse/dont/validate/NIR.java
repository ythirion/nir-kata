package parse.dont.validate;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static parse.dont.validate.Sex.parseSex;

@EqualsAndHashCode
@AllArgsConstructor
public class NIR {
    private final Sex sex;
    private final Year year;

    public static Either<ParsingError, NIR> parseNIR(String input) {
        return parseSex(input.charAt(0))
                .map(NIRBuilder::new)
                .flatMap(builder -> parseYear(input.substring(1, 3), builder))
                .map(builder -> new NIR(builder.getSex(), builder.getYear()));
    }

    private static Either<ParsingError, NIRBuilder> parseYear(String input, NIRBuilder builder) {
        return Year.parseYear(input)
                .map(builder::withYear);
    }

    @Override
    public String toString() {
        return sex.toString() + year;
    }
}