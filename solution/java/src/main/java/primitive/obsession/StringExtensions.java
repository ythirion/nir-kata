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

    private static boolean isANumber(String str) {
        return str != null && str.matches("[0-9.]+");
    }
}
