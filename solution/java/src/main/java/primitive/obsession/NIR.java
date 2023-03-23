package primitive.obsession;

public class NIR {
    private static final int VALID_LENGTH = 15;

    public static Boolean validate(String potentialNIR) {
        return potentialNIR.length() == VALID_LENGTH;
    }
}
