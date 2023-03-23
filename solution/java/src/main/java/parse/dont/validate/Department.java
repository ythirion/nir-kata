package parse.dont.validate;

import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import primitive.obsession.StringExtensions;

@EqualsAndHashCode
@ExtensionMethod(StringExtensions.class)
public class Department {
    private final int value;

    private Department(int value) {
        this.value = value;
    }

    public static Either<ParsingError, Department> parseDepartment(String potentialDepartment) {
        return potentialDepartment
                .toInt()
                .filter(x -> x > 0 && (x <= 95 || x == 99))
                .toEither(new ParsingError("department should be gt 0 and lt 96 or equal to 99"))
                .map(Department::new);
    }

    public static Department fromInt(Integer x) {
        return parseDepartment(x.toString())
                .getOrElseThrow(() -> new IllegalArgumentException("Department"));
    }

    @Override
    public String toString() {
        return String.format("%02d", value);
    }
}
