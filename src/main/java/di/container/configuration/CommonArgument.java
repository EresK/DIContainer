package di.container.configuration;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CommonArgument implements ArgumentValue {
    private String argName;
    private Class<?> argType;
    private Object argValue;
    private String argBeanReference;

    public CommonArgument(Class<?> type, Object value) {
        argType = type;
        argValue = value;
    }

    public CommonArgument(String name, Class<?> type, Object value) {
        this(type, value);
        this.argName = name;
    }

    public CommonArgument(String beanReference) {
        argBeanReference = beanReference;
    }

    /* GETTERS */

    @JsonGetter("name")
    @Override
    public String getName() {
        return argName;
    }

    @JsonGetter("type")
    @Override
    public Class<?> getType() {
        return argType;
    }

    @JsonGetter("value")
    @Override
    public Object getValue() {
        return argValue;
    }

    @JsonGetter("ref")
    @Override
    public String getBeanReference() {
        return argBeanReference;
    }

    /* SETTERS */

    @JsonSetter("name")
    @Override
    public void setName(String name) {
        argName = name;
    }

    @JsonSetter("type")
    @Override
    public void setType(Class<?> type) {
        argType = type;
    }

    @JsonSetter("value")
    @Override
    public void setValue(Object value) {
        argValue = value;
    }

    @JsonSetter("ref")
    @Override
    public void setBeanReference(String beanReference) {
        argBeanReference = beanReference;
    }

    /* PREDICATES */

    @JsonIgnore
    @Override
    public boolean hasName() {
        return argName != null && !argName.equals("");
    }

    @JsonIgnore
    @Override
    public boolean hasType() {
        return argType != null;
    }

    @JsonIgnore
    @Override
    public boolean hasValue() {
        return argValue != null;
    }

    @JsonIgnore
    @Override
    public boolean isBeanReference() {
        return argBeanReference != null && !argBeanReference.equals("");
    }

    @JsonIgnore
    @Override
    public boolean isCorrect() {
        return (isBeanReference() && !hasName() && !hasType() && !hasValue()) ||
                (hasName() && hasType() && hasValue() && !isBeanReference()) ||
                (hasType() && hasValue() && !isBeanReference());
    }

    @Override
    public String toString() {
        return String.format("name: %s%n" +
                "type: %s%n" +
                "value: %s%n" +
                "ref: %s",
                argName, argType, argValue, argBeanReference);
    }
}
