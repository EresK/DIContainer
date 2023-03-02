package di.container.configuration;

import di.container.application.Application;
import di.container.application.Logger;
import di.container.beans.Bean;
import di.container.beans.BeanBuilder;
import di.container.scope.Scope;

import java.util.List;

public class Configuration {

    public Configuration(String configurationPath) {
    }

    public List<Bean> getBeanDefinitions() {
        Bean b1 = new BeanBuilder()
                .setName("app")
                .setClass(Application.class)
                .setLazyInit(true)
                .build();

        Bean b2 = new BeanBuilder()
                .setName("logger")
                .setClass(Logger.class)
                .setScope(Scope.PROTOTYPE)
                .build();

        return List.of(b1, b2);
    }
}
