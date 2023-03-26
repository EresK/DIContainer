package di.container.beans.factory;

import di.container.beans.BeanDefinition;
import di.container.configuration.value.AbstractValue;
import di.container.configuration.value.ConstructorValue;
import di.container.context.ApplicationContext;
import di.container.scope.Scope;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanFactory {
    private final ApplicationContext applicationContext;
    private final BeanRecorder beanRecorder;

    public BeanFactory(ApplicationContext context) {
        applicationContext = context;
        beanRecorder = new BeanRecorder();
    }

    public Object getInstance(BeanDefinition bean) throws Exception {
        return switch (bean.getBeanScope()) {
            case SINGLETON, THREAD -> {
                Scope scope = bean.getBeanScope();

                Object instance = (scope == Scope.SINGLETON) ?
                        beanRecorder.getSingletonBean(bean.getBeanClass()) :
                        beanRecorder.getThreadBean(Thread.currentThread(), bean.getBeanClass());

                if (instance != null)
                    yield instance;

                instance = constructInstance(bean);

                if (scope == Scope.SINGLETON)
                    beanRecorder.registerSingletonBean(bean.getBeanClass(), instance);
                else
                    beanRecorder.registerThreadBean(Thread.currentThread(), bean.getBeanClass(), instance);

                injectSetterDependencies(bean, instance);
                yield instance;
            }
            case PROTOTYPE -> {
                Object instance = constructInstance(bean);
                injectSetterDependencies(bean, instance);
                yield instance;
            }
        };
    }

    private Object constructInstance(BeanDefinition bean) throws Exception {
        /* Получаем типы параметров бина */
        var beanParameters = bean.getConstructorArguments()
                .stream()
                .map(ConstructorValue::getType)
                .toList()
                .toArray(new Class[0]);

        /* Находим все конструкторы, с таким же количеством аргументов как у бина */
        var matchedConstructors = Arrays.stream(bean.getBeanClass().getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() == beanParameters.length)
                .toList();

        Constructor<?> primaryConstructor = null;

        /* Ищем подходящий по типам конструктор */
        for (var constructor : matchedConstructors) {
            if (primaryConstructor != null)
                break;

            if (constructor.getParameterCount() == 0) {
                primaryConstructor = constructor;
                break;
            }

            var parameterTypes = constructor.getParameterTypes();

            for (int i = 0; i < parameterTypes.length; i++) {
                /* Проверка, является ли тип параметра текущего конструктора
                таким же типом или супертипом для параметра бина */
                if (!parameterTypes[i].isAssignableFrom(beanParameters[i]))
                    break;
                /* Если дошли до конца, то все типы в конструкторе сходятся */
                if (i == parameterTypes.length - 1)
                    primaryConstructor = constructor;
            }
        }

        if (primaryConstructor == null)
            throw new NoSuchMethodException(String.format(
                    "There is no any constructor for bean: %s with the specific arguments", bean.getBeanClass()));

        var constructorParameters = argumentValuesToList(bean.getConstructorArguments().toArray(new AbstractValue[0]));

        return primaryConstructor.newInstance(constructorParameters.toArray());
    }

    private void injectSetterDependencies(BeanDefinition bean, Object instance) throws Exception {
        for (var property : bean.getPropertyArguments()) {
            if (!property.isCorrect())
                throw new IllegalStateException("Property has incorrect state: " + property);

            Class<?> type = property.isBeanReference() ?
                    applicationContext.getBeanDefinition(property.getBeanReference()).getBeanClass() :
                    property.getType();

            String setterName = SetterName.get(property.getName());

            var matchedMethods = Arrays.stream(bean.getBeanClass().getDeclaredMethods())
                    .filter(method -> method.getParameterCount() == 1 &&
                            method.getName().equals(setterName) &&
                            method.getParameterTypes()[0].isAssignableFrom(type))
                    .toList();

            if (matchedMethods.isEmpty())
                throw new NoSuchMethodException("No setter found for bean: " + bean);
            else if (matchedMethods.size() > 1)
                throw new IllegalStateException("Too many possible setters for bean: " + bean);

            Object value = argumentValueToObject(property);

            matchedMethods.stream().findFirst().get().invoke(instance, value);
        }
    }

    private List<Object> argumentValuesToList(AbstractValue[] argList) throws Exception {
        List<Object> objects = new ArrayList<>();

        for (AbstractValue value : argList)
            objects.add(argumentValueToObject(value));

        return objects;
    }

    private Object argumentValueToObject(AbstractValue value) throws Exception {
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

    static class SetterName {
        static String get(String str) {
            StringBuilder builder = new StringBuilder(str);
            builder.setCharAt(0, Character.toUpperCase(str.charAt(0)));
            builder.insert(0, "set");
            return builder.toString();
        }
    }
}
