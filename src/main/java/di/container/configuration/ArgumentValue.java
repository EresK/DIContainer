package di.container.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CommonArgument.class)
public interface ArgumentValue {
    /* GETTERS */
    String getName();
    Class<?> getType();
    Object getValue();
    String getBeanReference();

    /* SETTERS */

    void setName(String name);
    void setType(Class<?> type);
    void setValue(Object value);
    void setBeanReference(String beanReference);

    /* PREDICATES */

    boolean hasName();
    boolean hasType();
    boolean hasValue();
    boolean isBeanReference();
    boolean isCorrect();
}
