package mock;

public class DefaultLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println("Default logger: " + message);
    }
}
