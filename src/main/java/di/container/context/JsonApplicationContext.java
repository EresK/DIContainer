package di.container.context;

import di.container.beans.BeanDefinition;
import di.container.beans.ListableBean;
import di.container.beans.factory.BeanFactory;
import di.container.configuration.Configuration;
import di.container.configuration.JsonConfiguration;

import java.util.List;

public class JsonApplicationContext implements ApplicationContext {
    private final BeanFactory beanFactory;
    private final Configuration configuration;
    private final ListableBean listableBean;

    public JsonApplicationContext(String configurationPath) throws Exception {
        beanFactory = new BeanFactory(this);
        configuration = new JsonConfiguration(configurationPath);
        listableBean = new ListableBean(configuration.getBeanDefinitions());
        preInitialization();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        List<BeanDefinition> beans = listableBean.getBeansOfType(requiredType);

        if (beans.size() != 1)
            throw new IllegalStateException(String.format(
                    "Can not find or resolve BeanDefinitions, beans: %d, type: %s", beans.size(), requiredType));

        return (T) getInstanceByBeanDefinition(beans.stream().findFirst().get());
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
        return beanFactory.getInstance(bean);
    }

    private void preInitialization() throws Exception {
        for (BeanDefinition bean : listableBean.getBeanList()) {
            if (bean.isSingleton() && !bean.isLazyInit())
                getInstanceByBeanDefinition(bean);
        }
    }
}
