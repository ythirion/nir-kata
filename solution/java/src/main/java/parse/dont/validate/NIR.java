package parse.dont.validate;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;

import static io.vavr.control.Either.right;

@EqualsAndHashCode
public class NIR {
    public static Either<ParseError, NIR> parse(String input) {
        return right(new NIR());
    }

    @Override
    public String toString() {
        return "";
    }
}