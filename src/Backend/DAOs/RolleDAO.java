package Backend.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Backend.Objekte.Rolle;

public class RolleDAO extends BaseDAO<Rolle> {

    public RolleDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Rolle findById(int id) throws SQLException {
        String sql = "SELECT * FROM Rolle WHERE RolleID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToRolle(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void insert(Rolle rolle) throws SQLException {
        String sql = "INSERT INTO Rolle (Bezeichnung, Kommentar) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, rolle.getBezeichnung());
            ps.setString(2, rolle.getKommentar());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rolle.setRolleID(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Rolle rolle) throws SQLException {
        String sql = "UPDATE Rolle SET Bezeichnung = ?, Kommentar = ? WHERE RolleID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, rolle.getBezeichnung());
            ps.setString(2, rolle.getKommentar());
            ps.setInt(3, rolle.getRolleID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Rolle WHERE RolleID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Rolle> findAll() throws SQLException {
        List<Rolle> list = new ArrayList<>();
        String sql = "SELECT * FROM Rolle";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToRolle(rs));
            }
        }
        return list;
    }

    private Rolle mapRowToRolle(ResultSet rs) throws SQLException {
        return new Rolle(
            rs.getInt("RolleID"),
            rs.getString("Bezeichnung"),
            rs.getString("Kommentar")
        );
    }
}
