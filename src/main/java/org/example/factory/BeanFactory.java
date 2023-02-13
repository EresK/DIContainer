package org.example.factory;

import lombok.Getter;
import org.example.annotation.Inject;
import org.example.config.Configuration;
import org.example.config.JavaConfiguration;
import org.example.configurator.BeanConfigurator;
import org.example.configurator.JavaBeanConfigurator;
import org.example.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BeanFactory {
    private final Configuration configuration;
    private final BeanConfigurator beanConfigurator;
    private ApplicationContext applicationContext;

    public BeanFactory(ApplicationContext applicationContext) {
        configuration = new JavaConfiguration();
        beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan(),
                configuration.interfaceToImplementations());
        this.applicationContext = applicationContext;
    }

    public BeanConfigurator getBeanConfigurator() {
        return beanConfigurator;
    }

    public <T> T getBean(Class<T> typeClass) {
        Class<? extends T> implementationClass = typeClass;

        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }

        try {
            T bean = implementationClass.getDeclaredConstructor().newInstance();

            for (Field field : Arrays
                    .stream(implementationClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .toList()) {
                field.setAccessible(true);
                field.set(bean, applicationContext.getBean(field.getType()));
            }

            return bean;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Some error in BeanFactory");
        }
    }
}
