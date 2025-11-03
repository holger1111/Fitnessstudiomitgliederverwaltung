package Manager;

import DAOs.Transaction;
import DAOs.ConnectionDB;
import java.sql.Connection;
import java.sql.SQLException;
package Manager;

import java.sql.SQLException;
import java.util.List;
import DAOs.ConnectionDB;
import DAOs.Transaction;

public class TransactionManager extends Manager<Transaction> {

    private ConnectionDB connectionDB;

    public TransactionManager(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }


    public void begin() throws SQLException {
        if (currentTransaction != null && currentTransaction.isActive()) {
            throw new SQLException("Transaktion bereits aktiv");
        }
        Connection connection = connectionDB.getConnection();
        currentTransaction = new Transaction(connection);
    }

    public void commit() throws SQLException {
        if (currentTransaction == null || !currentTransaction.isActive()) {
            throw new SQLException("Keine aktive Transaktion");
        }
        currentTransaction.commit();
    }

    public void rollback() throws SQLException {
        if (currentTransaction == null || !currentTransaction.isActive()) {
            throw new SQLException("Keine aktive Transaktion");
        }
        currentTransaction.rollback();
    }

    public void close() throws SQLException {
        if (currentTransaction != null) {
            currentTransaction.close();
        }
    }
    
    @Override
    public void process() {
        for (Transaction tx : items) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        clear();
    }
}
