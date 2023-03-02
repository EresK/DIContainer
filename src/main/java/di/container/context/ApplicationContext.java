package di.container.context;

import di.container.beans.Bean;
import di.container.beans.ListableBean;
import di.container.configuration.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private ListableBean listableBean;
    private Map<Class<?>, Object> classObjectMap;
    private Configuration configuration;

    public ApplicationContext() {
        classObjectMap = new HashMap<>();
    }

    public ApplicationContext(String configurationPath) throws Exception {
        this();
        configuration = new Configuration(configurationPath);
        listableBean = new ListableBean(configuration.getBeanDefinitions());
        initSingletons();
    }

    public <T> T getBean(Class<T> requiredType) throws Exception {
        Bean bean = listableBean.getBeanByType(requiredType);
        return (T) getObjectByBean(bean);
    }

    public <T> T getBean(Class<T> requiredType, Object... args) throws Exception {
        Bean bean = listableBean.getBeanByType(requiredType);
        return (T) getObjectByBean(bean, args);
    }

    public Object getBean(String name) throws Exception {
        Bean bean = listableBean.getBeanByName(name);
        return getObjectByBean(bean);
    }

    public Object getBean(String name, Object... args) throws Exception {
        Bean bean = listableBean.getBeanByName(name);
        return getObjectByBean(bean, args);
    }

    private Object getObjectByBean(Bean bean, Object... args) throws Exception {
        Object obj;

        // check a singleton is in map
        if (bean.isSingleton() && ((obj = classObjectMap.get(bean.getBeanClass())) != null))
            return obj;

        // common part of creating instances
        if (args.length > 0) {
            var parameterTypes = Arrays.stream(args).map(Object::getClass).toList();
            obj = bean.getBeanClass().getDeclaredConstructor(parameterTypes.toArray(new Class[0])).newInstance(args);
        }
        else {
            obj = bean.getBeanClass().getDeclaredConstructor().newInstance();
        }

        // add singleton instance to map
        if (bean.isSingleton())
            classObjectMap.put(bean.getBeanClass(), obj);

        return obj;
    }

    private void initSingletons() throws Exception {
        for (Bean bean: listableBean.getBeanList()) {
            if (bean.isSingleton() && !bean.isLazyInit())
                getObjectByBean(bean);
        }
    }
}
