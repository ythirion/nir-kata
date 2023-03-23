package primitive.obsession;

public class NIR {
    private static final int VALID_LENGTH = 15;
    private static final char MALE = '1', FEMALE = '2';

    public static Boolean validate(String potentialNIR) {
        return validateLength(potentialNIR)
                && validateSex(potentialNIR.charAt(0))
                && validateYear(potentialNIR.substring(1, 3))
                && validateMonth(potentialNIR.substring(3, 5))
                && validateDepartment(potentialNIR.substring(5, 7));
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

    private static boolean validateMonth(String month) {
        if (!isANumber(month)) {
            return false;
        }

        var parsedMonth = Integer.parseInt(month);
        return parsedMonth > 0 && parsedMonth <= 12;
    }

    private static boolean validateDepartment(String department) {
        if (!isANumber(department)) {
            return false;
        }

        var parsedDepartment = Integer.parseInt(department);
        return parsedDepartment > 0
                && parsedDepartment <= 95
                || parsedDepartment == 99;
    }

    private static boolean isANumber(String potentialNumber) {
        return potentialNumber.matches("[0-9.]+");
    }
}
