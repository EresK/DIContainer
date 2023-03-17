package di.container.application;

import di.container.context.ApplicationContext;
import di.container.context.JsonApplicationContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Application {

    private Logger logger;

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new JsonApplicationContext("src/main/resources/cyclic-config.json");

        Application app1 = (Application) context.getBean("app");
        Application app2 = (Application) context.getBean("app");

        assert app1 == app2;

        Logger logger1 = (Logger) context.getBean("logger");
        Logger logger2 = (Logger) context.getBean("logger");

        assert logger1 != logger2;

        assert logger1.getApp() == app1;
        assert logger2.getApp() == app1;
    }
}
