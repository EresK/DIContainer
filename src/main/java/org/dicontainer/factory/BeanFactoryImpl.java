package org.dicontainer.factory;

import org.dicontainer.configurator.BeanConfigurator;
import org.dicontainer.configurator.BeanConfiguratorImpl;
import org.reflections.Reflections;

import java.util.Arrays;

public class BeanFactoryImpl implements BeanFactory {
    // TODO: BeanFactory should has some information about beans its scope: singleton or prototype,
    //  and mapping that is there such bean with specified name

    private final BeanConfigurator configurator;
    private final Reflections reflections;

    public BeanFactoryImpl(String packageName) {
        configurator = new BeanConfiguratorImpl(packageName);
        reflections = new Reflections();
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        Class<? extends T> implementationClass = clazz;

        if (implementationClass.isInterface()) {
            implementationClass = configurator.getImplementationOfInterface(clazz);
        }

        T bean = implementationClass.getDeclaredConstructor().newInstance();

        // TODO: add cache (map)

        return bean;
    }

    @Override
    public <T> T getBean(Class<T> clazz, Object... args) throws Exception {
        Class<? extends T> implementationClass = clazz;

        if (implementationClass.isInterface()) {
            implementationClass = configurator.getImplementationOfInterface(clazz);
        }

        Class<?>[] parameterTypes = (Class<?>[]) Arrays.stream(args).map(Object::getClass).toArray();

        T bean = implementationClass.getDeclaredConstructor(parameterTypes).newInstance(args);

        // TODO: add cache (map)

        return bean;
    }
}
