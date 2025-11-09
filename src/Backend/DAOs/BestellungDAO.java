package Backend.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Backend.Objekte.Bestellung;

public class BestellungDAO extends BaseDAO<Bestellung> {

    public BestellungDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Bestellung bestellung) throws SQLException {
        String sql = "INSERT INTO Bestellung (MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID, MitarbeiterID) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bestellung.getMitgliederID());
            ps.setDouble(2, bestellung.getGesamtpreis());
            ps.setTimestamp(3, bestellung.getBestelldatum());
            ps.setInt(4, bestellung.getZahlungID());
            ps.setInt(5, bestellung.getMitarbeiterID());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bestellung.setBestellungID(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Bestellung findById(int id) throws SQLException {
        String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID, MitarbeiterID " +
                     "FROM Bestellung WHERE BestellungID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBestellung(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Bestellung bestellung) throws SQLException {
        String sql = "UPDATE Bestellung SET MitgliederID = ?, Gesamtpreis = ?, Bestelldatum = ?, ZahlungID = ?, MitarbeiterID = ? WHERE BestellungID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bestellung.getMitgliederID());
            ps.setDouble(2, bestellung.getGesamtpreis());
            ps.setTimestamp(3, bestellung.getBestelldatum());
            ps.setInt(4, bestellung.getZahlungID());
            ps.setInt(5, bestellung.getMitarbeiterID());
            ps.setInt(6, bestellung.getBestellungID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Bestellung WHERE BestellungID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Bestellung> findByMitgliederId(int mitgliederID) throws SQLException {
        String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID, MitarbeiterID " +
                     "FROM Bestellung WHERE MitgliederID = ?";
        List<Bestellung> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mitgliederID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToBestellung(rs));
                }
            }
        }
        return list;
    }

    public List<Bestellung> findAll() throws SQLException {
        String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID, MitarbeiterID FROM Bestellung";
        List<Bestellung> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToBestellung(rs));
            }
        }
        return list;
    }

    public List<Bestellung> searchAllAttributes(String searchTerm) throws SQLException {
        List<Bestellung> result = new ArrayList<>();
        String sql = "SELECT * FROM Bestellung WHERE " +
                     "CAST(BestellungID AS CHAR) LIKE ? OR " +
                     "CAST(MitgliederID AS CHAR) LIKE ? OR " +
                     "CAST(MitarbeiterID AS CHAR) LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + searchTerm.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToBestellung(rs));
                }
            }
        }
        return result;
    }

    private Bestellung mapRowToBestellung(ResultSet rs) throws SQLException {
        return new Bestellung(
            rs.getInt("BestellungID"),
            rs.getInt("MitgliederID"),
            rs.getDouble("Gesamtpreis"),
            rs.getTimestamp("Bestelldatum"),
            rs.getInt("ZahlungID"),
            rs.getInt("MitarbeiterID")   // Mapping der neuen Spalte
        );
    }
}
