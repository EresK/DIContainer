package mock;

import lombok.Getter;

public class ClassBasedApplication {
    @Getter
    private DefaultLogger logger;

    public ClassBasedApplication(DefaultLogger logger) {
        this.logger = logger;
    }
}
