package mock;

import lombok.Getter;

public class InterfaceBasedApplication {
    @Getter
    private ILogger logger;

    public InterfaceBasedApplication(ILogger logger) {
        this.logger = logger;
    }
}
