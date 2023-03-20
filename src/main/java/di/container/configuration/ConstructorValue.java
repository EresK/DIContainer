package di.container.configuration;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConstructorValue extends AbstractValue {
    private final Class<?> type;
    private final Object value;
    private final String beanReference;

    public ConstructorValue(@JsonProperty(value = "type", required = true) Class<?> type,
                            @JsonProperty("value") Object value,
                            @JsonProperty("ref") String beanReference) {
        this.type = type;
        this.value = value;
        this.beanReference = beanReference;
    }

    public ConstructorValue(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
        beanReference = null;
    }

    public ConstructorValue(Class<?> type, String beanReference) {
        this.type = type;
        this.beanReference = beanReference;
        value = null;
    }

    @JsonGetter("type")
    public Class<?> getType() {
        return type;
    }

    @JsonGetter("value")
    @Override
    public Object getValue() {
        return value;
    }

    @JsonGetter("ref")
    @Override
    public String getBeanReference() {
        return beanReference;
    }

    @JsonIgnore
    @Override
    public boolean isBeanReference() {
        return type != null &&
                beanReference != null && !beanReference.equals("") &&
                value == null;
    }

    @JsonIgnore
    @Override
    public boolean isPrimitive() {
        return type != null &&
                value != null &&
                beanReference == null;
    }

    @JsonIgnore
    @Override
    public boolean isCorrect() {
        return isBeanReference() ^ isPrimitive();
    }

    @Override
    public String toString() {
        return String.format("type: %s, value: %s, reference: %s", type, value, beanReference);
    }
}
