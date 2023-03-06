package di.container.scope;

import java.util.Locale;

public enum Scope {
    SINGLETON,
    PROTOTYPE,
    THREAD;

    public static Scope getScope(String scope) {
        return switch (scope.toLowerCase(Locale.ROOT)) {
            case "singleton" -> Scope.SINGLETON;
            case "prototype" -> Scope.PROTOTYPE;
            case "thread" -> Scope.THREAD;
            default -> Scope.SINGLETON;
        };
    }

    public String getScopeValue() {
        return switch (this) {
            case SINGLETON -> "singleton";
            case PROTOTYPE -> "prototype";
            case THREAD -> "thread";
        };
    }
}
