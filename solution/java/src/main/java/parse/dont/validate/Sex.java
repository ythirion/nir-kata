package parse.dont.validate;

import io.vavr.control.Either;

import static io.vavr.API.*;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

public enum Sex {
    M(1), F(2);

    private final int value;

    Sex(int value) {
        this.value = value;
    }

    public static Either<ParsingError, Sex> parseSex(char potentialSex) {
        return Match(potentialSex).of(
                Case($('1'), right(M)),
                Case($('2'), right(F)),
                Case($(), left((new ParsingError("Not a valid sex"))))
        );
    }

    @Override
    public String toString() {
        return "" + value;
    }
}