package primitive.obsession;

public class NIR {
    private static final int VALID_LENGTH = 15;

    public static Boolean validate(String potentialNIR) {
        return validateLength(potentialNIR)
                && validateSex(potentialNIR.charAt(0))
                && validateYear(potentialNIR);
        //&& validateYear(potentialNIR.substring(1, 2));
    }

    private static boolean validateLength(String potentialNIR) {
        return potentialNIR.length() == VALID_LENGTH;
    }

    private static boolean validateSex(char sex) {
        return sex == '1' || sex == '2';
    }

    private static boolean validateYear(String potentialNIR) {
        return potentialNIR
                .substring(1, 2)
                .matches("[0-9.]+");
    }
}
