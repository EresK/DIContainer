package org.dicontainer.configurator;

import java.util.Map;

public interface BeanConfigurator {

    <T> Class<? extends T> getImplementationOfInterface(Class<T> clazz);
}
