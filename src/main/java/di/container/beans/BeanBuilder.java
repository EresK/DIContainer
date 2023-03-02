package di.container.beans;

import di.container.scope.Scope;

public class BeanBuilder {
    private Bean bean;

    public BeanBuilder() {
        bean = new Bean();
    }

    public BeanBuilder setName(String name) {
        bean.setBeanName(name);
        return this;
    }

    public BeanBuilder setClass(Class<?> clazz) {
        bean.setBeanClass(clazz);
        return this;
    }

    public BeanBuilder setScope(Scope scope) {
        bean.setBeanScope(scope);
        return this;
    }

    public BeanBuilder setLazyInit(boolean lazyInit) {
        bean.setLazyInit(lazyInit);
        return this;
    }

    public BeanBuilder setFactoryBeanName(String factoryBeanName) {
        bean.setFactoryBeanName(factoryBeanName);
        return this;
    }

    public BeanBuilder setFactoryMethodName(String factoryMethodName) {
        bean.setFactoryMethodName(factoryMethodName);
        return this;
    }

    public Bean build() {
        Bean beanToReturn = bean;
        bean = new Bean();
        return beanToReturn;
    }
}
