package org.dicontainer.configurator;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanConfiguratorImpl implements BeanConfigurator {
    private final Map<Class<?>, Class<?>> implementationsMap;
    private final Reflections scanner;

    public BeanConfiguratorImpl(String packageName) {
        implementationsMap = new HashMap<>();
        this.scanner = new Reflections(packageName);
    }

    @Override
    public <T> Class<? extends T> getImplementationOfInterface(Class<T> clazz) {
        Class<?> bean = implementationsMap.computeIfAbsent(clazz, realization -> {
            // Returns extended interfaces too
            Set<Class<? extends T>> subtypes = scanner.getSubTypesOf(clazz);

            return getSpecificClass(subtypes);
        });

        return (Class<? extends T>) bean;
    }

    private <T> Class<? extends T> getSpecificClass(Set<Class<? extends T>> subtypes) {
        Class<? extends T> implementation = null;

        for (var type : subtypes) {
            if (!type.isInterface()) {
                if (implementation == null)
                    implementation = type;
                else
                    throw new RuntimeException("Interface has more than 1 implementations");
            }
        }

        if (implementation == null)
            throw new RuntimeException("Interface has no implementation");

        return implementation;
    }
}
