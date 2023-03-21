package di.container.context;

import di.container.annotations.Inject;
import di.container.annotations.Named;
import di.container.beans.BeanDefinition;
import di.container.scanner.Scanner;
import javassist.tools.reflect.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationApplicationContext implements ApplicationContext{
    private static final HashMap<String, Class<?>> idForNamed = new HashMap<>();
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
            String name = clazz.getAnnotation(Named.class).value();

            if (Objects.equals(name, "")) {
                String a = clazz.getName();
                String[] list = a.split("\\.");
                name = Character.toLowerCase(list[list.length - 1].charAt(0)) + list[list.length - 1].substring(1);
            }

            if (!AnnotationApplicationContext.idForNamed.containsKey(name)) {
                AnnotationApplicationContext.idForNamed.put(name, clazz);
            }
        }


        T bean = null;
        List<Object> instances = new ArrayList<>();
        for (Constructor constructor: Arrays.stream(implementationClass.getDeclaredConstructors()).filter(constructor -> constructor.isAnnotationPresent(Inject.class)).toList()) {
            List<Parameter> parameters = List.of(constructor.getParameters());
            for(Parameter parameter: parameters) {
                if (parameter.isAnnotationPresent(Named.class)) {
                    instances.add(this.getBean(AnnotationApplicationContext.idForNamed.get(parameter.getAnnotation(Named.class).value())));
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
            String id = field.getAnnotation(Named.class).value();
            Object obj = getBean(AnnotationApplicationContext.idForNamed.get(id));
            field.set(bean, obj);
        }

        for (Field field: Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class) && !field.isAnnotationPresent(Named.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, this.getBean(field.getType()));
        }

        List<Method> methods = scanner.getAllInjectedMethods(implementationClass);
        for(Method m: methods) {
            m.setAccessible(true);
            Class<?> type = Arrays.stream(m.getParameterTypes()).findFirst().get();
            m.invoke(bean, getBean(type));
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
