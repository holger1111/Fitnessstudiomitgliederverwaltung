package Backend.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Backend.Objekte.Benutzer;

public class BenutzerDAO extends BaseDAO<Benutzer> {

    public BenutzerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Benutzer benutzer) throws SQLException {
        String sql = "INSERT INTO Benutzer (Benutzername, Passwort, RolleID, MitarbeiterID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, benutzer.getBenutzername());
            ps.setString(2, benutzer.getPasswort());
            ps.setInt(3, benutzer.getRolleID());
            if (benutzer.getMitarbeiterID() != null)
                ps.setInt(4, benutzer.getMitarbeiterID());
            else
                ps.setNull(4, Types.INTEGER);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    benutzer.setBenutzerID(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Benutzer findById(int id) throws SQLException {
        String sql = "SELECT BenutzerID, Benutzername, Passwort, RolleID, MitarbeiterID FROM Benutzer WHERE BenutzerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBenutzer(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Benutzer benutzer) throws SQLException {
        String sql = "UPDATE Benutzer SET Benutzername = ?, Passwort = ?, RolleID = ?, MitarbeiterID = ? WHERE BenutzerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, benutzer.getBenutzername());
            ps.setString(2, benutzer.getPasswort());
            ps.setInt(3, benutzer.getRolleID());
            if (benutzer.getMitarbeiterID() != null)
                ps.setInt(4, benutzer.getMitarbeiterID());
            else
                ps.setNull(4, Types.INTEGER);
            ps.setInt(5, benutzer.getBenutzerID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Benutzer WHERE BenutzerID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Benutzer> findAll() throws SQLException {
        List<Benutzer> list = new ArrayList<>();
        String sql = "SELECT BenutzerID, Benutzername, Passwort, RolleID, MitarbeiterID FROM Benutzer";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToBenutzer(rs));
            }
        }
        return list;
    }

    private Benutzer mapRowToBenutzer(ResultSet rs) throws SQLException {
        int benutzerID = rs.getInt("BenutzerID");
        String benutzername = rs.getString("Benutzername");
        String passwort = rs.getString("Passwort");
        int rolleID = rs.getInt("RolleID");
        int mitarbeiterID = rs.getInt("MitarbeiterID");
        return new Benutzer(
            benutzerID,
            benutzername,
            passwort,
            rolleID,
            rs.wasNull() ? null : mitarbeiterID
        );
    }
}
