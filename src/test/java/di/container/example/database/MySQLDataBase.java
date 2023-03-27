package di.container.example.database;

public class MySQLDataBase implements IDataBase {
    private int version;

    public MySQLDataBase(int version) {
        this.version = version;
    }

    @Override
    public String getInfo() {
        return "MySQL database, version: " + version;
    }
}
