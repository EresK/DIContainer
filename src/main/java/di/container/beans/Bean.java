package di.container.beans;

import di.container.scope.Scope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Bean {
    private String beanName;
    private Class<?> beanClass;
    private Scope beanScope = Scope.SINGLETON;
    private boolean lazyInit = false;

    private String factoryBeanName;
    private String factoryMethodName;

    public Bean(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
    }

    public boolean hasFactoryBean() {
        return factoryBeanName != null && !factoryBeanName.equals("");
    }

    public boolean isSingleton() {
        return beanScope == Scope.SINGLETON;
    }

    public boolean isPrototype() {
        return beanScope == Scope.PROTOTYPE;
    }

    public boolean isThread() {
        return beanScope == Scope.THREAD;
    }
}
