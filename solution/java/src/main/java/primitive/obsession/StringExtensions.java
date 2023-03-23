package primitive.obsession;

import io.vavr.Function1;
import io.vavr.control.Option;
import lombok.experimental.UtilityClass;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;

@UtilityClass
public class StringExtensions {
    public static Option<Integer> toInt(String potentialNumber) {
        return toNumber(potentialNumber, Integer::parseInt);
    }

    public static Option<Long> toLong(String potentialNumber) {
        return toNumber(potentialNumber, Long::parseLong);
    }

    public static boolean isANumber(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    private static <T> Option<T> toNumber(String input, Function1<String, T> parser) {
        return isANumber(input)
                ? some(parser.apply(input))
                : none();
    }
}
