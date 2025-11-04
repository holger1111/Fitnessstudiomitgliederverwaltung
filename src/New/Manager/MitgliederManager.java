package New.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import New.DAOs.MitgliederDAO;
import New.Objekte.Mitglieder;
import New.Exception.ConnectionException;
import New.Exception.NotFoundException;
import New.DAOs.ConnectionDB;

public class MitgliederManager extends Manager<Mitglieder> {

    private final MitgliederDAO mitgliederDAO;

    public MitgliederManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
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
