package di.container.configuration;

import di.container.beans.BeanDefinition;

import java.util.List;

public interface Configuration {
    List<BeanDefinition> getBeanDefinitions() throws Exception;
}
