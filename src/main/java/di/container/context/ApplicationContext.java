package di.container.context;

public interface ApplicationContext {
    <T> T getBean(Class<T> requiredType) throws Exception;

    Object getBean(String name) throws Exception;
}
