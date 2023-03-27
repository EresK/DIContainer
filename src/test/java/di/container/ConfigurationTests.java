package di.container;

import di.container.beans.BeanDefinition;
import di.container.configuration.JsonConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ConfigurationTests {
    @Test
    public void correctConfiguration() {
        /* simple singleton configuration */
        Assertions.assertDoesNotThrow(() -> {
            List<BeanDefinition> beans = new JsonConfiguration(
                    "src/test/resources/correct/simple-config.json")
                    .getBeanDefinitions();

            Assertions.assertEquals(2, beans.size());
        });

        /* cyclic configuration */
        Assertions.assertDoesNotThrow(() -> {
            List<BeanDefinition> beans = new JsonConfiguration(
                    "src/test/resources/correct/cyclic-config.json")
                    .getBeanDefinitions();

            Assertions.assertEquals(2, beans.size());
        });
    }

    @Test
    public void incorrectConfiguration() {
        /* missing required json property */
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JsonConfiguration("src/test/resources/incorrect/missing-type-config.json")
                        .getBeanDefinitions());

        /* recursive beans configuration */
        Assertions.assertThrows(IllegalStateException.class, () ->
                new JsonConfiguration("src/test/resources/incorrect/recursive-config.json")
                        .getBeanDefinitions());

        /* illegal state of bean argument */
        Assertions.assertThrows(IllegalStateException.class, () ->
                new JsonConfiguration("src/test/resources/incorrect/bad-property-config.json")
                        .getBeanDefinitions());
    }
}
