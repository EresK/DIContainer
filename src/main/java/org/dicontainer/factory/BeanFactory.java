package org.dicontainer.factory;

public interface BeanFactory {
    <T> T getBean(Class<T> clazz) throws Exception;

    <T> T getBean(Class<T> clazz, Object ... args) throws Exception;
}
