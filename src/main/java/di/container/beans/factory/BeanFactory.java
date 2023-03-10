package di.container.beans.factory;

import di.container.beans.BeanDefinition;
import di.container.configuration.ArgumentValue;
import di.container.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.*;

public class BeanFactory {
    private final ApplicationContext applicationContext;

    public BeanFactory(ApplicationContext context) {
        applicationContext = context;
    }

    public Object getInstance(BeanDefinition beanDefinition) throws Exception {
        Object instance;

        /* Gets instance and sets constructor arguments */
        var constructorArgs = valuesToList(beanDefinition.getConstructorArguments());
        var constructorArgsClasses = constructorArgs.stream().map(Object::getClass).toList().toArray(new Class[0]);

        if (constructorArgs.size() > 0) {
            instance = beanDefinition
                    .getBeanClass()
                    .getDeclaredConstructor(constructorArgsClasses)
                    .newInstance(constructorArgs.toArray());
        }
        else {
            instance = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
        }

        /* Sets property arguments */
        Map<String, Field> fieldMap = new HashMap<>();

        for (Field filed : beanDefinition.getBeanClass().getDeclaredFields())
            fieldMap.put(filed.getName(), filed);

        for (ArgumentValue arg : beanDefinition.getPropertyArguments()) {
            if (!fieldMap.containsKey(arg.getName()))
                throw new NoSuchFieldException(String.format(
                        "There is no field %s in class %s",
                        arg.getName(),
                        beanDefinition.getBeanClass()));

            Field field = fieldMap.get(arg.getName());

            field.setAccessible(true);
            field.set(instance, valueToObject(arg));
        }

        return instance;
    }

    private List<Object> valuesToList(List<ArgumentValue> argList) throws Exception {
        List<Object> objects = new ArrayList<>();

        for (ArgumentValue arg : argList)
            objects.add(valueToObject(arg));

        return objects;
    }

    private Object valueToObject(ArgumentValue arg) throws Exception {
        if (!arg.isCorrect())
            throw new IllegalStateException("Can not resolve is ArgumentValue bean or primitive type: " + arg);

        Object obj;

        if (arg.isBeanReference()) {
            // TODO: fix cyclic dependencies
            obj = applicationContext.getBean(arg.getBeanReference());
        }
        else {
            Object val = arg.getValue();

            if (val instanceof String)
                obj = String.valueOf(val);
            else if (val instanceof Byte)
                obj = Byte.valueOf((Byte) val);
            else if (val instanceof Short)
                obj = Short.valueOf((Short) val);
            else if (val instanceof Integer)
                obj = Integer.valueOf((Integer) val);
            else if (val instanceof Long)
                obj = Long.valueOf((Long) val);
            else if (val instanceof Float)
                obj = Float.valueOf((Float) val);
            else if (val instanceof Double)
                obj = Double.valueOf((Double) val);
            else if (val instanceof Boolean)
                obj = Boolean.valueOf((Boolean) val);
            else if (val instanceof Character)
                obj = Character.valueOf((Character) val);
            else
                throw new IllegalStateException("ArgumentValue is neither a bean ref not a primitive type: " + arg);
        }

        return obj;
    }
}
