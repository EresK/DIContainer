package di.container;

import di.container.context.ApplicationContext;
import di.container.context.JsonApplicationContext;
import di.container.example.database.IDataBase;
import di.container.example.database.MySQLDataBase;
import di.container.example.database.PostgresDataBase;
import di.container.example.server.IWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

public class ApplicationContextTests {
    @Test
    public void basicConfiguration() {
        Assertions.assertDoesNotThrow(() -> {
            ApplicationContext context = new JsonApplicationContext(
                    "src/test/resources/config/basic-config.json");

            IWebServer firstServer = context.getBean(IWebServer.class);
            IWebServer secondServer = context.getBean(IWebServer.class);

            IDataBase dataBase = (IDataBase) context.getBean("dataBase");

            Assertions.assertNotNull(firstServer);
            Assertions.assertNotNull(secondServer);
            Assertions.assertNotNull(dataBase);

            Assertions.assertNotSame(firstServer, secondServer);

            Assertions.assertSame(dataBase, context.getBean(IDataBase.class));
            Assertions.assertSame(firstServer.getDataBase(), dataBase);
            Assertions.assertSame(firstServer.getDataBase(), secondServer.getDataBase());

            Assertions.assertEquals("Node JS web server status: active", firstServer.getStatus());
            Assertions.assertEquals("Node JS web server status: active", secondServer.getStatus());
        });
    }

    @Test
    public void singletonScope() {
        Assertions.assertDoesNotThrow(() -> {
            ApplicationContext context = new JsonApplicationContext(
                    "src/test/resources/config/singleton.json");

            IDataBase dataBase = context.getBean(IDataBase.class);
            IDataBase dataBase2 = context.getBean(IDataBase.class);

            Assertions.assertNotNull(dataBase);
            Assertions.assertNotNull(dataBase2);

            Assertions.assertTrue(dataBase instanceof PostgresDataBase);
            Assertions.assertSame(dataBase, dataBase2);
        });
    }

    @Test
    public void prototypeScope() {
        Assertions.assertDoesNotThrow(() -> {
            ApplicationContext context = new JsonApplicationContext(
                    "src/test/resources/config/prototype.json");

            IDataBase dataBase = context.getBean(IDataBase.class);
            IDataBase dataBase2 = context.getBean(IDataBase.class);

            Assertions.assertNotNull(dataBase);
            Assertions.assertNotNull(dataBase2);

            Assertions.assertTrue(dataBase instanceof MySQLDataBase);
            Assertions.assertNotSame(dataBase, dataBase2);
        });
    }

    @Test
    public void threadScope() {
        Assertions.assertDoesNotThrow(() -> {
            ApplicationContext context = new JsonApplicationContext(
                    "src/test/resources/config/thread.json");

            AtomicReference<IDataBase> dataBase = new AtomicReference<>();
            AtomicReference<IDataBase> dataBase2 = new AtomicReference<>();

            Thread thread1 = new Thread(() ->
                    Assertions.assertDoesNotThrow(() -> dataBase.set(context.getBean(IDataBase.class))));

            Thread thread2 = new Thread(() ->
                    Assertions.assertDoesNotThrow(() -> dataBase2.set(context.getBean(IDataBase.class))));

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            Assertions.assertNotNull(dataBase.get());
            Assertions.assertNotNull(dataBase2.get());
            Assertions.assertNotSame(dataBase.get(), dataBase2.get());
        });
    }
}
