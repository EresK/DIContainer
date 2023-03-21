package di.container.scanner;

import di.container.annotations.Inject;
import di.container.annotations.Named;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Scanner {
    private Reflections scanner;

    public Scanner(String packagePath) {
        this.scanner = new Reflections(packagePath);
    }

    public Set<Class<?>> getAllNamedClasses() {
        return scanner.getTypesAnnotatedWith(Named.class);
    }

    public Class<?> getComponentByName(String name) {
        Set<Class<?>> components = getAllNamedClasses();
        for (Class<?> component: components) {
            String componentName = component.getAnnotation(Named.class).value();
            if (componentName.isEmpty() || componentName.isBlank()) {
                return null;
            }

            if (componentName.equals(name)) {
                return component;
            }

            return null;
        }

        return null;
    }

//    public Class<?> getInjectableComponent(Parameter parameter) {
//        if (parameter.isAnnotationPresent(Inject.class)) {
//            String name = parameter.getAnnotation(Inject.class);
//            if (!name.isEmpty() && !name.isBlank()) {
//                return getComponentByName(name);
//            }
//        }
//
//        Class<?> type = parameter.getType();
//        if (type.isInterface()) {
//            return getImplementation(type);
//        }
//        return type;
//    }

    public Class<?> getImplementation(Class<?> type) {
        return scanner.getSubTypesOf(type)
                .stream()
                .filter(t -> t.isAnnotationPresent(Named.class))
                .findFirst()
                .orElse(null);
    }

    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) throws Exception {
        Set<Class<? extends T>> implementationClasses = scanner.getSubTypesOf(interfaceClass);
        if (implementationClasses.size() != 1) {
            throw new Exception("Interface has 0 or more than 1 implementations");
        }

        return implementationClasses.stream().findFirst().get();
    }

    public List<Method> getAllInjectedMethods(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        List<Method> injectedMethods = new ArrayList<>();
        for (Method m: declaredMethods) {
            if (m.isAnnotationPresent(Inject.class) && m.getName().startsWith("set")) {
                injectedMethods.add(m);
            }
        }

        return injectedMethods;
    }
}
