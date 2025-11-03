package Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import DAOs.MitgliederDAO;
import DAOs.Transaction;
import DAOs.ConnectionDB;
import Objekte.Mitglieder;
import Exception.NotFoundException;

public class MitgliederManager extends Manager<Mitglieder> {

    private final MitgliederDAO mitgliederDAO;
    private final ConnectionDB connectionDB;

    public MitgliederManager(String dbUrl, String user, String password) throws SQLException, ClassNotFoundException {
        connectionDB = new ConnectionDB(dbUrl, user, password);
        Connection connection = connectionDB.getConnection();
        mitgliederDAO = new MitgliederDAO(connection);
    }

    @Override
    public void add(Mitglieder item) {
        super.add(item);
        // Zusätzliche Logik falls nötig
    }

    @Override
    public void remove(Mitglieder item) {
        super.remove(item);
        // Zusätzliche Logik falls nötig
    }

    @Override
    public List<Mitglieder> getAll() {
        return super.getAll();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void process() {
        // Beispielhafte Implementierung oder leer lassen
    }

    public Mitglieder findById(int id) throws NotFoundException, SQLException {
        Mitglieder mitglied = mitgliederDAO.findById(id);
        if (mitglied == null) {
            throw new NotFoundException("Mitglied mit ID " + id + " nicht gefunden.");
        }
        return mitglied;
    }
}