package di.container.example.server;

import di.container.example.database.IDataBase;

public interface IWebServer {
    String getStatus();

    IDataBase getDataBase();
}
