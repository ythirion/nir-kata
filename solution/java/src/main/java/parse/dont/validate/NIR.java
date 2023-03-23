package parse.dont.validate;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;

import static parse.dont.validate.Sex.parseSex;

@EqualsAndHashCode
public class NIR {
    private final Sex sex;

    public NIR(Sex sex) {
        this.sex = sex;
    }

    public static Either<ParsingError, NIR> parseNIR(String input) {
        return parseSex(input.charAt(0))
                .map(NIR::new);
    }

    @Override
    public String toString() {
        return sex.toString();
    }
}