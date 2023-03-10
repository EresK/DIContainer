package di.container.context;

import di.container.beans.BeanDefinition;
import di.container.beans.ListableBean;
import di.container.beans.factory.BeanFactory;
import di.container.configuration.Configuration;
import di.container.configuration.JsonConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private Configuration configuration;
    private ListableBean listableBean;
    private final Map<Class<?>, Object> classObjectMap = new ConcurrentHashMap<>();

    public ApplicationContext() {
        // TODO: run classpath scanner
    }

    public ApplicationContext(String configurationPath) throws Exception {
        this();
        beanFactory = new BeanFactory(this);
        configuration = new JsonConfiguration(configurationPath);
        listableBean = new ListableBean(configuration.getBeanDefinitions());
        preInitialization();
    }

    public <T> T getBean(Class<T> requiredType) throws Exception {
        BeanDefinition bean = listableBean.getBeanByType(requiredType);
        return (T) getObjectByBeanDefinition(bean);
    }

    public <T> T getBean(Class<T> requiredType, Object... args) throws Exception {
        BeanDefinition bean = listableBean.getBeanByType(requiredType);
        return (T) getObjectByBeanDefinition(bean, args);
    }

    public Object getBean(String name) throws Exception {
        BeanDefinition bean = listableBean.getBeanByName(name);
        return getObjectByBeanDefinition(bean);
    }

    public Object getBean(String name, Object... args) throws Exception {
        BeanDefinition bean = listableBean.getBeanByName(name);
        return getObjectByBeanDefinition(bean, args);
    }

    private Object getObjectByBeanDefinition(BeanDefinition bean, Object... args) throws Exception {
        if (bean.isSingleton() && classObjectMap.containsKey(bean.getBeanClass()))
            return classObjectMap.get(bean.getBeanClass());

        Object obj = beanFactory.getInstance(bean);

        if (bean.isSingleton())
            classObjectMap.put(bean.getBeanClass(), obj);

        return obj;
    }

    private void preInitialization() throws Exception {
        for (BeanDefinition bean : listableBean.getBeanList()) {
            if (bean.isSingleton() && !bean.isLazyInit())
                getObjectByBeanDefinition(bean);
        }
    }
}
