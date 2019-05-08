package exception;

public class InvalidDataBaseOperation extends Exception {
    private static final String MESSAGE = "Something wrong with your SQL operation. See your LOG";

    public InvalidDataBaseOperation() {
        super(MESSAGE);
    }

    public InvalidDataBaseOperation(String message) {
        super(message);
    }
}
