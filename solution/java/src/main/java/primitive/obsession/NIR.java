package primitive.obsession;

public class NIR {
    private static final int VALID_LENGTH = 15;
    private static final char MALE = '1', FEMALE = '2';

    public static Boolean validate(String potentialNIR) {
        return validateLength(potentialNIR)
                && validateSex(potentialNIR.charAt(0))
                && validateYear(potentialNIR.substring(1, 2));
    }

    private static boolean validateLength(String potentialNIR) {
        return potentialNIR.length() == VALID_LENGTH;
    }

    private static boolean validateSex(char sex) {
        return sex == MALE || sex == FEMALE;
    }

    private static boolean validateYear(String year) {
        return isANumber(year);
    }

    private static boolean isANumber(String potentialNumber) {
        return potentialNumber.matches("[0-9.]+");
    }
}
