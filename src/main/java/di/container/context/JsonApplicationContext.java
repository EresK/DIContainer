package di.container.context;

import di.container.beans.BeanDefinition;
import di.container.beans.ListableBean;
import di.container.beans.factory.BeanFactory;
import di.container.configuration.Configuration;
import di.container.configuration.JsonConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonApplicationContext implements ApplicationContext {
    private final BeanFactory beanFactory;
    private final Configuration configuration;
    private final ListableBean listableBean;
    private final Map<Class<?>, Object> classObjectMap = new ConcurrentHashMap<>();

    public JsonApplicationContext(String configurationPath) throws Exception {
        beanFactory = new BeanFactory(this);
        configuration = new JsonConfiguration(configurationPath);
        listableBean = new ListableBean(configuration.getBeanDefinitions());
        preInitialization();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        BeanDefinition bean = listableBean.getBeanByType(requiredType);
        return (T) getInstanceByBeanDefinition(bean);
    }

    @Override
    public Object getBean(String name) throws Exception {
        BeanDefinition bean = listableBean.getBeanByName(name);
        return getInstanceByBeanDefinition(bean);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) throws Exception {
        return listableBean.getBeanByName(name);
    }

    private Object getInstanceByBeanDefinition(BeanDefinition bean) throws Exception {
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
                getInstanceByBeanDefinition(bean);
        }
    }
}
