package Backend.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Backend.Objekte.Mitarbeiter;

public class MitarbeiterDAO extends BaseDAO<Mitarbeiter> {

    public MitarbeiterDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Mitarbeiter m) throws SQLException {
        String sql = "INSERT INTO Mitarbeiter (Vorname, Nachname, Geburtsdatum, Aktiv, Straße, Hausnr, OrtID, ZahlungsdatenID, Telefon, Mail, BenutzerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getVorname());
            ps.setString(2, m.getNachname());
            ps.setDate(3, m.getGeburtsdatum() != null ? new java.sql.Date(m.getGeburtsdatum().getTime()) : null);
            ps.setBoolean(4, m.isAktiv());
            ps.setString(5, m.getStrasse());
            ps.setString(6, m.getHausnr());
            ps.setInt(7, m.getOrtID());
            ps.setInt(8, m.getZahlungsdatenID());
            ps.setString(9, m.getTelefon());
            ps.setString(10, m.getMail());
            if (m.getBenutzerID() != null) ps.setInt(11, m.getBenutzerID());
            else ps.setNull(11, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setMitarbeiterID(rs.getInt(1));
            }
        }
    }

    @Override
    public Mitarbeiter findById(int id) throws SQLException {
        String sql = "SELECT * FROM Mitarbeiter WHERE MitarbeiterID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToMitarbeiter(rs);
            }
        }
        return null;
    }

    @Override
    public void update(Mitarbeiter m) throws SQLException {
        String sql = "UPDATE Mitarbeiter SET Vorname=?, Nachname=?, Geburtsdatum=?, Aktiv=?, Straße=?, Hausnr=?, OrtID=?, ZahlungsdatenID=?, Telefon=?, Mail=?, BenutzerID=? WHERE MitarbeiterID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, m.getVorname());
            ps.setString(2, m.getNachname());
            ps.setDate(3, m.getGeburtsdatum() != null ? new java.sql.Date(m.getGeburtsdatum().getTime()) : null);
            ps.setBoolean(4, m.isAktiv());
            ps.setString(5, m.getStrasse());
            ps.setString(6, m.getHausnr());
            ps.setInt(7, m.getOrtID());
            ps.setInt(8, m.getZahlungsdatenID());
            ps.setString(9, m.getTelefon());
            ps.setString(10, m.getMail());
            if (m.getBenutzerID() != null) ps.setInt(11, m.getBenutzerID());
            else ps.setNull(11, Types.INTEGER);
            ps.setInt(12, m.getMitarbeiterID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Mitarbeiter WHERE MitarbeiterID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Mitarbeiter> findAll() throws SQLException {
        String sql = "SELECT * FROM Mitarbeiter";
        List<Mitarbeiter> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToMitarbeiter(rs));
            }
        }
        return list;
    }

    public List<Mitarbeiter> searchAllAttributes(String suchbegriff) throws SQLException {
        List<Mitarbeiter> list = new ArrayList<>();
        String sql = "SELECT * FROM Mitarbeiter WHERE "
                   + "CAST(MitarbeiterID AS CHAR) LIKE ? OR "
                   + "Vorname LIKE ? OR "
                   + "Nachname LIKE ? OR "
                   + "Telefon LIKE ? OR "
                   + "Mail LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + suchbegriff + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToMitarbeiter(rs));
                }
            }
        }
        return list;
    }

    private Mitarbeiter mapRowToMitarbeiter(ResultSet rs) throws SQLException {
        Mitarbeiter m = new Mitarbeiter();
        m.setMitarbeiterID(rs.getInt("MitarbeiterID"));
        m.setVorname(rs.getString("Vorname"));
        m.setNachname(rs.getString("Nachname"));
        m.setGeburtsdatum(rs.getDate("Geburtsdatum"));
        m.setAktiv(rs.getBoolean("Aktiv"));
        m.setStrasse(rs.getString("Straße"));
        m.setHausnr(rs.getString("Hausnr"));
        m.setOrtID(rs.getInt("OrtID"));
        m.setZahlungsdatenID(rs.getInt("ZahlungsdatenID"));
        m.setTelefon(rs.getString("Telefon"));
        m.setMail(rs.getString("Mail"));
        int benutzerId = rs.getInt("BenutzerID");
        m.setBenutzerID(rs.wasNull() ? null : benutzerId);
        return m;
    }
}
