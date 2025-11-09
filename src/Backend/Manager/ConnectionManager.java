package Backend.Manager;

import java.sql.Connection;

import Backend.DAOs.ConnectionDB;
import Backend.Exception.ConnectionException;

public class ConnectionManager {

    public ConnectionManager() {
    }

    public Connection getConnection() throws ConnectionException {
        return ConnectionDB.getConnection();
    }
}
