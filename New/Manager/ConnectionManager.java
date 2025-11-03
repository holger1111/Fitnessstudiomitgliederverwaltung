package Manager;

import java.sql.SQLException;
import DAOs.ConnectionDB;

public class ConnectionManager extends Manager<ConnectionDB> {

    @Override
    public void process() {
        for (ConnectionDB conn : items) {
            try {
                conn.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        clear();
    }
    private ConnectionDB connectionDB;

    public ConnectionManager(String dbUrl, String user, String pass) throws SQLException, ClassNotFoundException {
        connectionDB = new ConnectionDB(dbUrl, user, pass);
    }

    public Connection getConnection() throws SQLException {
        return connectionDB.getConnection();
    }

    public void close() throws SQLException {
        if (connectionDB != null) {
            connectionDB.closeConnection();
        }
    }
}