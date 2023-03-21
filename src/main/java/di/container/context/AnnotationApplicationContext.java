package di.container.context;

import di.container.annotations.Inject;
import di.container.annotations.Named;
import di.container.beans.BeanDefinition;
import di.container.scanner.Scanner;
import javassist.tools.reflect.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationApplicationContext implements ApplicationContext{
    private final HashMap<String, Class<?>> idForNamed = new HashMap<>();
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

        Set<Class<?>> classesWithNamed = scanner.getAllNamedClasses();
        for(Class<?> clazz: classesWithNamed) {
            if (!idForNamed.containsKey(clazz.getAnnotation(Named.class).value())) {
                idForNamed.put(clazz.getAnnotation(Named.class).value(), clazz);
            }
        }

        String namedValue = new String();
        if (implementationClass.isAnnotationPresent(Named.class)) {
            namedValue = implementationClass.getAnnotation(Named.class).value();
            if (!idForNamed.containsKey(namedValue)) {
                idForNamed.put(namedValue, implementationClass);
            }
        }

        if (Objects.equals(namedValue, "")) {
            String a = implementationClass.getName();
            namedValue = a.substring(0, 1).toLowerCase() + a.substring(1);
            if (!idForNamed.containsKey(namedValue)) {
                idForNamed.put(namedValue, implementationClass);
            }
        }

        T bean = null;
        List<Object> instances = new ArrayList<>();
        for (Constructor constructor: Arrays.stream(implementationClass.getDeclaredConstructors()).filter(constructor -> constructor.isAnnotationPresent(Inject.class)).toList()) {
            List<Parameter> parameters = List.of(constructor.getParameters());
            for(Parameter parameter: parameters) {
                if (parameter.isAnnotationPresent(Named.class)) {
                    instances.add(this.getBean(idForNamed.get(parameter.getAnnotation(Named.class).value())));
                } else {
                    instances.add(parameter.getType());
                }
            }
            bean = (T) constructor.newInstance(instances.toArray());
        }

        if (bean == null) {
            Constructor constructor = Arrays.stream(implementationClass.getDeclaredConstructors()).findFirst().get();

            List<Class<?>> classesList = List.of(constructor.getParameterTypes());
            for (Class clazz: classesList) {
                instances.add(this.getBean(clazz));
            }

            bean = (T) constructor.newInstance(instances.toArray());
        }

        for (Field field: Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class) && field.isAnnotationPresent(Named.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, this.getBean(idForNamed.get(field.getAnnotation(Named.class).value())));
        }

        for (Field field: Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
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
