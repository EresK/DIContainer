package di.container.configuration;

public abstract class AbstractValue {
    public abstract Object getValue();

    public abstract String getBeanReference();

    public abstract boolean isBeanReference();

    public abstract boolean isPrimitive();

    public abstract boolean isCorrect();
}
