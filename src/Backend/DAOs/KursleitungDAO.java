package Backend.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Backend.Objekte.Kursleitung;

public class KursleitungDAO extends BaseDAO<Kursleitung> {

    public KursleitungDAO(Connection connection) {
        super(connection);
    }

    // Composite Key
    public Kursleitung findByCompositeKey(int kursterminID, int mitarbeiterID) throws SQLException {
        String sql = "SELECT * FROM Kursleitung WHERE KursterminID = ? AND MitarbeiterID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            ps.setInt(2, mitarbeiterID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToKursleitung(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Kursleitung findById(int id) {
        throw new UnsupportedOperationException("Nutze findByCompositeKey(kursterminID, mitarbeiterID)");
    }

    @Override
    public void insert(Kursleitung k) throws SQLException {
        String sql = "INSERT INTO Kursleitung (KursterminID, MitarbeiterID, Bestätigt, Bestätigungszeit, Abgemeldet, Abmeldezeit, Kommentar) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, k.getKursterminID());
            ps.setInt(2, k.getMitarbeiterID());
            ps.setBoolean(3, k.isBestätigt());
            ps.setTimestamp(4, k.getBestätigungszeit());
            ps.setBoolean(5, k.isAbgemeldet());
            ps.setTimestamp(6, k.getAbmeldezeit());
            ps.setString(7, k.getKommentar());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Kursleitung k) throws SQLException {
        String sql = "UPDATE Kursleitung SET Bestätigt = ?, Bestätigungszeit = ?, Abgemeldet = ?, Abmeldezeit = ?, Kommentar = ? WHERE KursterminID = ? AND MitarbeiterID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, k.isBestätigt());
            ps.setTimestamp(2, k.getBestätigungszeit());
            ps.setBoolean(3, k.isAbgemeldet());
            ps.setTimestamp(4, k.getAbmeldezeit());
            ps.setString(5, k.getKommentar());
            ps.setInt(6, k.getKursterminID());
            ps.setInt(7, k.getMitarbeiterID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Use deleteByCompositeKey(kursterminID, mitarbeiterID)");
    }

    public void deleteByCompositeKey(int kursterminID, int mitarbeiterID) throws SQLException {
        String sql = "DELETE FROM Kursleitung WHERE KursterminID = ? AND MitarbeiterID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            ps.setInt(2, mitarbeiterID);
            ps.executeUpdate();
        }
    }
    
    public Kursleitung findByKursterminId(int kursterminID) throws SQLException {
        String sql = "SELECT * FROM Kursleitung WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToKursleitung(rs);
                }
            }
        }
        return null;
    }
    
    public List<Kursleitung> findAll() throws SQLException {
        List<Kursleitung> list = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToKursleitung(rs));
            }
        }
        return list;
    }

    public List<Kursleitung> findByMitarbeiterID(int mitarbeiterID) throws SQLException {
        List<Kursleitung> list = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung WHERE MitarbeiterID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mitarbeiterID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToKursleitung(rs));
                }
            }
        }
        return list;
    }

    public List<Kursleitung> findByKursterminID(int kursterminID) throws SQLException {
        List<Kursleitung> list = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursterminID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToKursleitung(rs));
                }
            }
        }
        return list;
    }

    public List<Kursleitung> searchAllAttributes(String searchTerm) throws SQLException {
        List<Kursleitung> results = new ArrayList<>();
        String sql = "SELECT * FROM Kursleitung WHERE " +
                "CAST(KursterminID AS CHAR) LIKE ? OR " +
                "CAST(MitarbeiterID AS CHAR) LIKE ? OR " +
                "CAST(Bestätigungszeit AS CHAR) LIKE ? OR " +
                "CAST(Abmeldezeit AS CHAR) LIKE ? OR " +
                "Kommentar LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 5; i++) {
                ps.setString(i, pattern);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToKursleitung(rs));
                }
            }
        }
        return results;
    }

    private Kursleitung mapRowToKursleitung(ResultSet rs) throws SQLException {
        Kursleitung k = new Kursleitung();
        k.setKursterminID(rs.getInt("KursterminID"));
        k.setMitarbeiterID(rs.getInt("MitarbeiterID"));
        k.setBestätigt(rs.getBoolean("Bestätigt"));
        k.setBestätigungszeit(rs.getTimestamp("Bestätigungszeit"));
        k.setAbgemeldet(rs.getBoolean("Abgemeldet"));
        k.setAbmeldezeit(rs.getTimestamp("Abmeldezeit"));
        k.setKommentar(rs.getString("Kommentar"));
        return k;
    }
}
