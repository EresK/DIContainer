package di.container.example.server;

import di.container.example.database.IDataBase;

public class NodeJSWebServer implements IWebServer {
    private IDataBase dataBase;
    private String status;

    public NodeJSWebServer(IDataBase dataBase, String status) {
        this.dataBase = dataBase;
        this.status = status;
    }

    @Override
    public String getStatus() {
        return "Node JS web server status: " + status;
    }
}
