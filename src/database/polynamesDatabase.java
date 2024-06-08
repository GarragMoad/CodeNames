package database;
import java.sql.SQLException;

public class polynamesDatabase extends MySQLDatabase{

    public polynamesDatabase()
            throws SQLException {
        super("localhost", 3306, "polynames", "root", "");
    }
}
