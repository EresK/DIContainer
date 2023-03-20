package di.container.context;

import di.container.annotations.Inject;
import di.container.beans.BeanDefinition;
import di.container.scanner.Scanner;

import java.lang.reflect.Field;
import java.util.Arrays;

public class AnnotationApplicationContext implements ApplicationContext {
    private final Scanner scanner;

    public AnnotationApplicationContext(String pathPackage) {
        scanner = new Scanner(pathPackage);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        Class<?> implementationClass = requiredType;
        if (implementationClass.isInterface()) {
            implementationClass = scanner.getImplementationClass(implementationClass);
        }

        T bean = (T) implementationClass.getDeclaredConstructor().newInstance();
        for (Field field : Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, this.getBean(field.getType()));
        }

        return bean;
    }

    @Override
    public Object getBean(String name) throws Exception {
        return null;
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) throws Exception {
        return null;
    }
}
