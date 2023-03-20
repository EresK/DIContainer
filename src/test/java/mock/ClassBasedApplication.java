package mock;

public class ClassBasedApplication implements IApplication {

    private DefaultLogger logger;

    public ClassBasedApplication() {
    }

    public ClassBasedApplication(DefaultLogger logger) {
        this.logger = logger;
    }

    public DefaultLogger getLogger() {
        return logger;
    }

    public void setLogger(DefaultLogger logger) {
        this.logger = logger;
    }
}
