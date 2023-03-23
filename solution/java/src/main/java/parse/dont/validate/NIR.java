package parse.dont.validate;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import primitive.obsession.StringExtensions;

import static java.lang.String.format;
import static parse.dont.validate.Sex.parseSex;

@EqualsAndHashCode
@AllArgsConstructor
@ExtensionMethod(StringExtensions.class)
public class NIR {
    private final Sex sex;
    private final Year year;
    private final Month month;
    private final Department department;
    private final City city;
    private final SerialNumber serialNumber;

    private int key() {
        return stringWithoutKey()
                .toLong()
                .map(x -> (97L - x % 97L))
                .map(Long::intValue)
                .get();
    }

    public static Either<ParsingError, NIR> parseNIR(String input) {
        return parseSafely(input)
                .flatMap(nir -> checkKey(input.substring(13), nir));
    }

    private static Either<ParsingError, NIR> parseSafely(String input) {
        return parseSex(input.charAt(0))
                .map(NIRBuilder::new)
                .flatMap(builder -> parseYear(input.substring(1, 3), builder))
                .flatMap(builder -> parseMonth(input.substring(3, 5), builder))
                .flatMap(builder -> parseDepartment(input.substring(5, 7), builder))
                .flatMap(builder -> parseCity(input.substring(7, 10), builder))
                .flatMap(builder -> parseSerialNumber(input.substring(10, 13), builder))
                .flatMap(builder -> parseKey(input.substring(13, 15), builder))
                .map(builder ->
                        new NIR(
                                builder.getSex(),
                                builder.getYear(),
                                builder.getMonth(),
                                builder.getDepartment(),
                                builder.getCity(),
                                builder.getSerialNumber()
                        )
                );
    }

    private static Either<ParsingError, NIRBuilder> parseYear(String input, NIRBuilder builder) {
        return Year.parseYear(input).map(builder::withYear);
    }

    private static Either<ParsingError, NIRBuilder> parseMonth(String input, NIRBuilder builder) {
        return Month.parseMonth(input).map(builder::withMonth);
    }

    private static Either<ParsingError, NIRBuilder> parseDepartment(String input, NIRBuilder builder) {
        return Department.parseDepartment(input).map(builder::withDepartment);
    }

    private static Either<ParsingError, NIRBuilder> parseCity(String input, NIRBuilder builder) {
        return City.parseCity(input).map(builder::withCity);
    }

    private static Either<ParsingError, NIRBuilder> parseSerialNumber(String input, NIRBuilder builder) {
        return SerialNumber.parseSerialNumber(input).map(builder::withSerialNumber);
    }

    private static Either<ParsingError, NIRBuilder> parseKey(String input, NIRBuilder builder) {
        return input.toInt()
                .map(builder::withKey)
                .toEither(new ParsingError("not a valid number for the key"));
    }

    private static Either<ParsingError, NIR> checkKey(String input, NIR nir) {
        return input.toInt()
                .filter(parsedKey -> nir.key() == parsedKey)
                .map(x -> nir)
                .toEither(new ParsingError("invalid key"));
    }

    @Override
    public String toString() {
        return stringWithoutKey() + format("%02d", key());
    }

    private String stringWithoutKey() {
        return sex.toString() + year + month + department + city + serialNumber;
    }
}