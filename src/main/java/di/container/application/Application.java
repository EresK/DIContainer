package di.container.application;

import di.container.context.ApplicationContext;

public class Application {

    public Application() {
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ApplicationContext("src/main/resources/app-config.json");

        Application app1 = (Application) context.getBean("app");
        Application app2 = (Application) context.getBean("app");

        assert app1 == app2;

        Logger logger1 = (Logger) context.getBean("logger");
        Logger logger2 = (Logger) context.getBean("logger");

        assert logger1 != logger2;

        Logger logger3 = (Logger) context.getBean("logger", app1);

        assert app1 == logger3.getApp();
    }
}
