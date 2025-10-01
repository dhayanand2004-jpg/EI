
interface DatabaseConnection {
    void connect();
    void execute(String sql);
    String getType();
}

class MySQLConnection implements DatabaseConnection {
    public void connect() {
        System.out.println("Connected to MySQL");
    }

    public void execute(String sql) {
        System.out.println("MySQL exec: " + sql);
    }

    public String getType() {
        return "MySQL";
    }
}

class PostgreSQLConnection implements DatabaseConnection {
    public void connect() {
        System.out.println("Connected to PostgreSQL");
    }

    public void execute(String sql) {
        System.out.println("PostgreSQL exec: " + sql);
    }

    public String getType() {
        return "PostgreSQL";
    }
}

class DatabaseFactory {
    public static DatabaseConnection getConnection(String type) {
        return switch (type) {
            case "MySQL" -> new MySQLConnection();
            case "PostgreSQL" -> new PostgreSQLConnection();
            default -> throw new IllegalArgumentException("Unknown DB type");
        };
    }
}

public class DataConnectionApp {
    public static void main(String[] args) {
        DatabaseConnection db = DatabaseFactory.getConnection("MySQL");
        db.connect();
        db.execute("SELECT * FROM users");
        
        DatabaseConnection db2 = DatabaseFactory.getConnection("PostgreSQL");
        db2.connect();
        db2.execute("SELECT * FROM orders");
    }
}
