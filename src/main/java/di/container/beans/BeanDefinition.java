package di.container.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import di.container.configuration.ArgumentValue;
import di.container.scope.Scope;

import java.util.List;

@JsonDeserialize(as = Bean.class)
public interface BeanDefinition {
    /* GETTERS */
    String getBeanName();
    Class<?> getBeanClass();
    Scope getBeanScope();
    List<ArgumentValue> getConstructorArguments();
    List<ArgumentValue> getPropertyArguments();
    String getDescription();

    /* SETTERS */

    void setBeanName(String name);
    void setBeanClass(Class<?> clazz);
    void setBeanScope(Scope scope);
    void setPrimary(boolean primary);
    void setLazyInit(boolean lazyInit);

    /* PREDICATES */

    boolean isSingleton();
    boolean isPrototype();
    boolean isThread();
    boolean isPrimary();
    boolean isLazyInit();
}
