package di.container.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ConstructorValue {
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

    public boolean isBeanReference() {
        return type != null && beanReference != null && !beanReference.equals("") && value == null;
    }

    public boolean isPrimitive() {
        return type != null && value != null && beanReference == null;
    }

    @Override
    public String toString() {
        return String.format("type: %s, value: %s, reference: %s", type, value, beanReference);
    }
}
