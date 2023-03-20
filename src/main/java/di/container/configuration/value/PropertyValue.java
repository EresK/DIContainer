package di.container.configuration.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyValue extends AbstractValue {
    private final String name;
    private final Class<?> type;
    private final Object value;
    private final String beanReference;

    public PropertyValue(@JsonProperty(value = "name", required = true) String name,
                         @JsonProperty("type") Class<?> type,
                         @JsonProperty("value") Object value,
                         @JsonProperty("ref") String beanReference) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.beanReference = beanReference;
    }

    public PropertyValue(String name, Class<?> type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
        beanReference = null;
    }

    public PropertyValue(String name, String beanReference) {
        this.name = name;
        this.beanReference = beanReference;
        type = null;
        value = null;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
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
        return name != null && !name.equals("") &&
                beanReference != null && !beanReference.equals("") &&
                type == null && value == null;
    }

    @JsonIgnore
    @Override
    public boolean isPrimitive() {
        return name != null && !name.equals("") &&
                type != null &&
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
        return String.format("name: %s, type: %s, value: %s, reference: %s", name, type, value, beanReference);
    }
}
