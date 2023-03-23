package primitive.obsession;

public class NIR {
    private static final int VALID_LENGTH = 15;

    public static Boolean validate(String potentialNIR) {
        return validateLength(potentialNIR)
                && validateSex(potentialNIR);
    }

    private static boolean validateLength(String potentialNIR) {
        return potentialNIR.length() == VALID_LENGTH;
    }

    private static boolean validateSex(String potentialNIR) {
        return potentialNIR.charAt(0) == '1' || potentialNIR.charAt(0) == '2';
    }
}
