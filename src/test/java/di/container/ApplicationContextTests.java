package di.container;

import di.container.context.ApplicationContext;
import di.container.context.JsonApplicationContext;
import mock.ClassBasedApplication;
import mock.DefaultLogger;
import mock.ILogger;
import mock.InterfaceBasedApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationContextTests {
    @Test
    public void classBasedConfiguration() throws Exception {
        ApplicationContext context = new JsonApplicationContext(
                "src/test/resources/classBasedConfig.json");

        ClassBasedApplication application = context.getBean(ClassBasedApplication.class);
        ClassBasedApplication application2 = context.getBean(ClassBasedApplication.class);

        Assertions.assertNotNull(application);
        Assertions.assertSame(application, application2);

        Assertions.assertNotNull(application.getLogger());
        Assertions.assertEquals(DefaultLogger.class, application.getLogger().getClass());
    }

    @Test
    public void interfaceBasedConfiguration() throws Exception {
        ApplicationContext context = new JsonApplicationContext(
                "src/test/resources/interfaceBasedConfig.json");

        InterfaceBasedApplication application = context.getBean(InterfaceBasedApplication.class);
        InterfaceBasedApplication application2 = context.getBean(InterfaceBasedApplication.class);

        Assertions.assertNotNull(application);
        Assertions.assertSame(application, application2);

        Assertions.assertNotNull(application.getLogger());
        Assertions.assertEquals(DefaultLogger.class, application.getLogger().getClass());
    }

    @Test
    public void cyclicConfiguration() throws Exception {
        ApplicationContext context = new JsonApplicationContext(
                "src/test/resources/cyclicConfig.json");

        InterfaceBasedApplication application = context.getBean(InterfaceBasedApplication.class);
        ILogger logger = context.getBean(DefaultLogger.class);

        Assertions.assertNotNull(application);
        Assertions.assertNotNull(logger);

        Assertions.assertNotSame(application.getLogger(), logger);
    }
}
