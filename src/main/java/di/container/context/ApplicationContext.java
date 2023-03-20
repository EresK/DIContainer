package di.container.context;

import di.container.beans.BeanDefinition;

public interface ApplicationContext {
    <T> T getBean(Class<T> requiredType) throws Exception;

    Object getBean(String name) throws Exception;

    BeanDefinition getBeanDefinition(String name) throws Exception;
}
