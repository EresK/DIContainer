package di.container.beans.factory;

import di.container.beans.BeanDefinition;
import di.container.configuration.value.AbstractValue;
import di.container.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    private final ApplicationContext applicationContext;
    private final Map<Class<?>, Object> classObjectMap = new ConcurrentHashMap<>();

    public BeanFactory(ApplicationContext context) {
        applicationContext = context;
    }

    public Object getInstance(BeanDefinition bean) throws Exception {
        if (bean.isSingleton() && classObjectMap.containsKey(bean.getBeanClass()))
            return classObjectMap.get(bean.getBeanClass());

        Object instance = constructInstance(bean);

        if (bean.isSingleton())
            classObjectMap.put(bean.getBeanClass(), instance);

        injectDependencies(bean, instance);

        return instance;
    }

    private Object constructInstance(BeanDefinition beanDefinition) throws Exception {
        Object instance;

        /* Gets instance and sets constructor arguments */
        var constructorArgs = argumentValuesToList(beanDefinition.getConstructorArguments().toArray(new AbstractValue[0]));
        var constructorArgsClasses = constructorArgs.stream().map(Object::getClass).toList().toArray(new Class[0]);

        // TODO: look up all constructors and choose best one based on subtypes supertypes information
        //  because there is a bug where subtype argument can not be found by explicit search by types
        //  if B subtype A => B.class != A.class but method(A) can be replaced with method(B)
        if (constructorArgs.size() > 0) {
            instance = beanDefinition
                    .getBeanClass()
                    .getDeclaredConstructor(constructorArgsClasses)
                    .newInstance(constructorArgs.toArray());
        }
        else {
            instance = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
        }

        return instance;
    }

    private void injectDependencies(BeanDefinition bean, Object instance) throws Exception {
        /* Sets property arguments */
        Map<String, Field> fieldMap = new HashMap<>();

        for (Field filed : bean.getBeanClass().getDeclaredFields())
            fieldMap.put(filed.getName(), filed);

        for (var arg : bean.getPropertyArguments()) {
            if (!arg.isCorrect())
                throw new IllegalStateException("Argument has incorrect state: " + arg);
            else if ((arg.isBeanReference() && !fieldMap.containsKey(arg.getBeanReference())) ||
                    (!arg.isBeanReference() && !fieldMap.containsKey(arg.getName())))
                throw new NoSuchFieldException(String.format(
                        "There is no field %s in class %s",
                        arg.getName(),
                        bean.getBeanClass()));

            Object value = argumentValueToObject(arg);

            Field field = arg.isBeanReference() ? fieldMap.get(arg.getBeanReference()) : fieldMap.get(arg.getName());
            field.setAccessible(true);
            field.set(instance, value);
        }
    }

    private List<Object> argumentValuesToList(AbstractValue[] argList) throws Exception {
        List<Object> objects = new ArrayList<>();

        for (AbstractValue value : argList)
            objects.add(argumentValueToObject(value));

        return objects;
    }

    private Object argumentValueToObject(AbstractValue value) throws Exception {
        if (!value.isCorrect())
            throw new IllegalStateException("Can not resolve is Argument value bean or primitive type: " + value);

        Object obj;

        if (value.isBeanReference()) {
            obj = applicationContext.getBean(value.getBeanReference());
        }
        else {
            Object val = value.getValue();

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
                throw new IllegalStateException("Argument value is neither a bean ref not a primitive type: " + value);
        }

        return obj;
    }
}
