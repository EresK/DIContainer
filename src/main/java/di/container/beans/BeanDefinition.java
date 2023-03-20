package di.container.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import di.container.configuration.value.ConstructorValue;
import di.container.configuration.value.PropertyValue;
import di.container.scope.Scope;

import java.util.List;

@JsonSerialize(as = Bean.class)
@JsonDeserialize(as = Bean.class)
public interface BeanDefinition {
    /* GETTERS */
    String getBeanName();
    Class<?> getBeanClass();
    Scope getBeanScope();
    List<ConstructorValue> getConstructorArguments();
    List<PropertyValue> getPropertyArguments();
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
