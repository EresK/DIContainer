package mock;

public class InterfaceBasedApplication implements IApplication {
    private ILogger logger;

    public InterfaceBasedApplication() {
    }

    public InterfaceBasedApplication(ILogger logger) {
        this.logger = logger;
    }

    public ILogger getLogger() {
        return logger;
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
}
