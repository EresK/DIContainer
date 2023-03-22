package di.container;

import di.container.beans.BeanDefinition;
import di.container.configuration.JsonConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ConfigurationTests {

    @Test
    public void correctConfiguration() {
        Assertions.assertDoesNotThrow(() -> {
            List<BeanDefinition> beans = new JsonConfiguration(
                    "src/test/resources/classBasedConfig.json").getBeanDefinitions();
            Assertions.assertEquals(2, beans.size());
        });

        Assertions.assertDoesNotThrow(() -> {
            List<BeanDefinition> beans = new JsonConfiguration(
                    "src/test/resources/interfaceBasedConfig.json").getBeanDefinitions();
            Assertions.assertEquals(2, beans.size());
        });

        Assertions.assertDoesNotThrow(() -> {
            List<BeanDefinition> beans = new JsonConfiguration(
                    "src/test/resources/cyclicConfig.json").getBeanDefinitions();
            Assertions.assertEquals(2, beans.size());
        });
    }

    @Test
    public void incorrectConfiguration() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JsonConfiguration("src/test/resources/incorrectSchemaConfig.json").getBeanDefinitions());

        Assertions.assertThrows(IllegalStateException.class, () ->
                new JsonConfiguration("src/test/resources/incorrectCyclicConfig.json").getBeanDefinitions());

        Assertions.assertThrows(IllegalStateException.class, () ->
                new JsonConfiguration("src/test/resources/incorrectParametersConfig.json").getBeanDefinitions());
    }
}
