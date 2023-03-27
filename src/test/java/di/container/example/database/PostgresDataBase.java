package di.container.example.database;

public class PostgresDataBase implements IDataBase {
    private int version;

    public PostgresDataBase(int version) {
        this.version = version;
    }

    @Override
    public String getInfo() {
        return "PostgreSQL database, version" + version;
    }
}
