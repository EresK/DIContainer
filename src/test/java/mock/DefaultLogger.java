package mock;

public class DefaultLogger implements ILogger {
    private IApplication application;

    public DefaultLogger() {
    }

    public DefaultLogger(IApplication application) {
        this.application = application;
    }

    @Override
    public void log(String message) {
        System.out.println("Default logger: " + message);
    }
}
