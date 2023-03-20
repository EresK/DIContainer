package di.container.beans;

import com.fasterxml.jackson.annotation.*;
import di.container.configuration.value.ConstructorValue;
import di.container.configuration.value.PropertyValue;
import di.container.scope.Scope;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Bean implements BeanDefinition {
    private String beanName;
    private Class<?> beanClass;
    private Scope beanScope = Scope.SINGLETON;
    private boolean primary = false;
    private boolean lazyInit = false;

    private List<ConstructorValue> constructorArguments = new ArrayList<>();
    private List<PropertyValue> propertyArguments = new ArrayList<>();

    public Bean(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
    }

    /* GETTERS */

    @JsonGetter("name")
    @Override
    public String getBeanName() {
        return beanName;
    }

    @JsonGetter("class")
    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @JsonIgnore
    @Override
    public Scope getBeanScope() {
        return beanScope;
    }

    @JsonGetter("scope")
    public String getBeanScopeValue() {
        return beanScope.getScopeValue();
    }

    @JsonGetter("primary")
    @Override
    public boolean isPrimary() {
        return primary;
    }

    @JsonGetter("lazy-init")
    @Override
    public boolean isLazyInit() {
        return lazyInit;
    }

    @JsonGetter("constructor-args")
    @Override
    public List<ConstructorValue> getConstructorArguments() {
        return constructorArguments;
    }

    @JsonGetter("property-args")
    @Override
    public List<PropertyValue> getPropertyArguments() {
        return propertyArguments;
    }

    /* SETTERS */

    @JsonSetter("name")
    @JsonProperty(required = true)
    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @JsonSetter("class")
    @JsonProperty(required = true)
    @Override
    public void setBeanClass(Class<?> clazz) {
        beanClass = clazz;
    }

    @JsonIgnore
    @Override
    public void setBeanScope(Scope scope) {
        beanScope = scope;
    }

    @JsonSetter("scope")
    public void setBeanScopeValue(String scope) {
        beanScope = Scope.getScope(scope);
    }

    @JsonSetter("primary")
    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @JsonSetter("lazy-init")
    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /* OTHER METHODS */

    @JsonIgnore
    @Override
    public boolean isSingleton() {
        return beanScope == Scope.SINGLETON;
    }

    @JsonIgnore
    @Override
    public boolean isPrototype() {
        return beanScope == Scope.PROTOTYPE;
    }

    @JsonIgnore
    @Override
    public boolean isThread() {
        return beanScope == Scope.THREAD;
    }

    @JsonIgnore
    @Override
    public String getDescription() {
        return String.format("name: %s%n" +
                        "class: %s%n" +
                        "scope: %s%n" +
                        "primary: %s%n" +
                        "lazy-init: %s",
                beanName, beanClass, beanScope.getScopeValue(), primary, lazyInit);
    }
}
