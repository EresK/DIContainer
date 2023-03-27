package di.container.context;

import di.container.annotations.Inject;
import di.container.annotations.Named;
import di.container.annotations.Provider;
import di.container.beans.BeanDefinition;
import di.container.scanner.Scanner;

import java.lang.reflect.*;
import java.util.*;

public class AnnotationApplicationContext implements ApplicationContext{
    private static final HashMap<String, Class<?>> idForNamed = new HashMap<>();
    public static Scanner scanner;

    public AnnotationApplicationContext(String pathPackage) {
        AnnotationApplicationContext.scanner = new Scanner(pathPackage);
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
                } else if (parameter.getType().equals(Provider.class)) {

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

            if (field.getType().equals(Provider.class)) {
                String str = field.getGenericType().getTypeName();
                field.set(bean, getInstanceProvider(str));
                break;
            }

            field.set(bean, this.getBean(field.getType()));
        }

        List<Method> methods = scanner.getAllInjectedMethods(implementationClass);
        for(Method m: methods) {
            m.setAccessible(true);
            Class<T> type;
            if (m.isAnnotationPresent(Named.class)) {
                type = (Class<T>) idForNamed.get(m.getDeclaredAnnotation(Named.class).value());
            } else if (Arrays.stream(m.getParameterTypes()).findFirst().get().equals(Provider.class)) {
                String str = Arrays.stream(m.getGenericParameterTypes()).findFirst().get().getTypeName();
                m.invoke(bean, getInstanceProvider(str));
                break;
            } else {
                type = (Class<T>) Arrays.stream(m.getParameterTypes()).findFirst().get();
            }
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

    public Provider getInstanceProvider(String str) throws ClassNotFoundException {
        List<String> list = Arrays.stream(str.split("[<>]")).toList();
        str = list.get(list.size() - 1);
        Class<?> clazz = Class.forName(str);
        return new Provider<>(clazz, this);
    }
}
