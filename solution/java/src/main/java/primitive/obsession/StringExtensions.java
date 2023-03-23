package primitive.obsession;

import io.vavr.control.Option;
import lombok.experimental.UtilityClass;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;

@UtilityClass
public class StringExtensions {
    public static Option<Integer> toInt(String potentialNumber) {
        return isANumber(potentialNumber)
                ? some(Integer.parseInt(potentialNumber))
                : none();
    }

    public static Option<Long> toLong(String potentialNumber) {
        return isANumber(potentialNumber)
                ? some(Long.parseLong(potentialNumber))
                : none();
    }

    public static boolean isANumber(String str) {
        return str != null && str.matches("[0-9.]+");
    }
}
