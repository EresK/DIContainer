package di.container.example.database;

import di.container.example.server.IWebServer;

public class EmbeddedDataBase implements IDataBase {
    private IWebServer server;
    private int version;

    public EmbeddedDataBase(IWebServer server, int version) {
        this.server = server;
        this.version = version;
    }

    @Override
    public String getInfo() {
        return String.format("Embedded database, version: %d, server status: %s", version, server.getStatus());
    }
}
