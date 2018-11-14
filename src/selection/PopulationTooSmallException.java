package selection;

public class PopulationTooSmallException extends Exception {

    private static final String ERROR = "Tour size is smaller than the configured Select Count.";

    public PopulationTooSmallException() {
        super(ERROR);
    }

    public PopulationTooSmallException(String message) {
        super(message);
    }
}
