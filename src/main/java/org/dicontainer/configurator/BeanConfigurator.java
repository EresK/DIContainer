package org.dicontainer.configurator;

public interface BeanConfigurator {

    <T> Class<? extends T> getImplementationOfInterface(Class<T> clazz);
}
