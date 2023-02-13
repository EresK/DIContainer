package org.example.context;

import org.example.factory.BeanFactory;
import org.example.postprocessor.BeanPostProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public <T> T getBean(Class<T> clazz) {
        if (beanMap.containsKey(clazz)) {
            return (T) beanMap.get(clazz);
        }

        T bean = beanFactory.getBean(clazz);
        callPostProcessors(bean);

        beanMap.put(clazz, bean);

        return bean;
    }

    private void callPostProcessors(Object bean) {
        try {
            for (Class<? extends BeanPostProcessor> processor :
                    beanFactory.getBeanConfigurator().getScanner().getSubTypesOf(BeanPostProcessor.class)) {
                BeanPostProcessor postProcessor = processor.getDeclaredConstructor().newInstance();
                postProcessor.process(bean);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Can not call post process method for bean: " + bean.getClass());
        }
    }
}
