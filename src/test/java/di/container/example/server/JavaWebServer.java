package di.container.example.server;

import di.container.example.database.IDataBase;

public class JavaWebServer implements IWebServer {
    private IDataBase dataBase;
    private String status;

    public JavaWebServer(String status) {
        this.status = status;
    }

    public JavaWebServer(IDataBase dataBase, String status) {
        this.dataBase = dataBase;
        this.status = status;
    }

    @Override
    public IDataBase getDataBase() {
        return dataBase;
    }

    public void setDataBase(IDataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public String getStatus() {
        return "Java web server status: " + status;
    }
}
