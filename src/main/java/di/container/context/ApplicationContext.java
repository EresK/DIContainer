package di.container.context;

import di.container.beans.BeanDefinition;
import di.container.beans.ListableBean;
import di.container.configuration.Configuration;
import di.container.configuration.JsonConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private Configuration configuration;
    private ListableBean listableBean;
    private final Map<Class<?>, Object> classObjectMap;

    public ApplicationContext() {
        classObjectMap = new HashMap<>();
        // TODO: run classpath scanner
    }

    public ApplicationContext(String configurationPath) throws Exception {
        this();
        configuration = new JsonConfiguration(configurationPath);
        listableBean = new ListableBean(configuration.getBeanDefinitions());
        preInitialization();
    }

    public <T> T getBean(Class<T> requiredType) throws Exception {
        BeanDefinition bean = listableBean.getBeanByType(requiredType);
        return (T) getObjectByBean(bean);
    }

    public <T> T getBean(Class<T> requiredType, Object... args) throws Exception {
        BeanDefinition bean = listableBean.getBeanByType(requiredType);
        return (T) getObjectByBean(bean, args);
    }

    public Object getBean(String name) throws Exception {
        BeanDefinition bean = listableBean.getBeanByName(name);
        return getObjectByBean(bean);
    }

    public Object getBean(String name, Object... args) throws Exception {
        BeanDefinition bean = listableBean.getBeanByName(name);
        return getObjectByBean(bean, args);
    }

    private Object getObjectByBean(BeanDefinition bean, Object... args) throws Exception {
        Object obj;

        // Check singleton is in map
        if (bean.isSingleton() && ((obj = classObjectMap.get(bean.getBeanClass())) != null))
            return obj;

        // Common part of creating instances
        if (args.length > 0) {
            var parameterTypes = Arrays.stream(args).map(Object::getClass).toList();
            obj = bean.getBeanClass().getDeclaredConstructor(parameterTypes.toArray(new Class[0])).newInstance(args);
        }
        else {
            obj = bean.getBeanClass().getDeclaredConstructor().newInstance();
        }

        // Add singleton instance to map
        if (bean.isSingleton())
            classObjectMap.put(bean.getBeanClass(), obj);

        return obj;
    }

    private void preInitialization() throws Exception {
        for (BeanDefinition bean: listableBean.getBeanList()) {
            if (bean.isSingleton() && !bean.isLazyInit())
                getObjectByBean(bean);
        }
    }
}
