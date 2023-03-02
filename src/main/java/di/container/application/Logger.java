package di.container.application;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Logger {
    @Getter
    private Application app;

    public Logger(Application app) {
        this.app = app;
    }
}
