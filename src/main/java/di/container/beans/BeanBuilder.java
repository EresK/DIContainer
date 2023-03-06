package di.container.beans;

import di.container.scope.Scope;

public class BeanBuilder {
    private BeanDefinition bean;

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

    public BeanBuilder setPrimary(boolean primary) {
        bean.setPrimary(primary);
        return this;
    }

    public BeanBuilder setLazyInit(boolean lazyInit) {
        bean.setLazyInit(lazyInit);
        return this;
    }

    public BeanDefinition build() {
        BeanDefinition beanDefinition = bean;
        bean = new Bean();
        return beanDefinition;
    }
}
