package di.container.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PropertyValue {
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

    public boolean isBeanReference() {
        return name != null && !name.equals("") && beanReference != null && !beanReference.equals("")
                && type == null && value == null;
    }

    public boolean isPrimitive() {
        return name != null && !name.equals("") && type != null && value != null && beanReference == null;
    }

    @Override
    public String toString() {
        return String.format("name: %s, type: %s, value: %s, reference: %s", name, type, value, beanReference);
    }
}
