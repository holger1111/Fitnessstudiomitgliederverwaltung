package Backend.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Backend.Objekte.Kurstermin;

public class KursterminDAO extends BaseDAO<Kurstermin> {

    public KursterminDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Kurstermin findById(int id) throws SQLException {
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar "
                   + "FROM Kurstermin WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToKurstermin(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void insert(Kurstermin entity) throws SQLException {
        String sql = "INSERT INTO Kurstermin (KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getKursID());
            ps.setTimestamp(2, entity.getTermin());
            ps.setInt(3, entity.getTeilnehmerfrei());
            ps.setBoolean(4, entity.isAnmeldebar());
            ps.setBoolean(5, entity.isAktiv());
            ps.setString(6, entity.getKommentar());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setKursterminID(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Kurstermin entity) throws SQLException {
        String sql = "UPDATE Kurstermin SET KursID = ?, Termin = ?, Teilnehmerfrei = ?, Anmeldebar = ?, Aktiv = ?, Kommentar = ? WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getKursID());
            ps.setTimestamp(2, entity.getTermin());
            ps.setInt(3, entity.getTeilnehmerfrei());
            ps.setBoolean(4, entity.isAnmeldebar());
            ps.setBoolean(5, entity.isAktiv());
            ps.setString(6, entity.getKommentar());
            ps.setInt(7, entity.getKursterminID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Kurstermin WHERE KursterminID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Kurstermin> findAll() throws SQLException {
        List<Kurstermin> termine = new ArrayList<>();
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar FROM Kurstermin";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                termine.add(mapRowToKurstermin(rs));
            }
        }
        return termine;
    }

    public List<Kurstermin> findByKursId(int kursID) throws SQLException {
        List<Kurstermin> termine = new ArrayList<>();
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar "
                   + "FROM Kurstermin WHERE KursID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, kursID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    termine.add(mapRowToKurstermin(rs));
                }
            }
        }
        return termine;
    }

    public List<Kurstermin> searchAllAttributes(String searchTerm) throws SQLException {
        List<Kurstermin> results = new ArrayList<>();
        String sql = "SELECT KursterminID, KursID, Termin, Teilnehmerfrei, Anmeldebar, Aktiv, Kommentar "
                   + "FROM Kurstermin "
                   + "WHERE CAST(KursterminID AS CHAR) LIKE ? "
                   + "OR CAST(KursID AS CHAR) LIKE ? "
                   + "OR CAST(Termin AS CHAR) LIKE ? "
                   + "OR CAST(Teilnehmerfrei AS CHAR) LIKE ? "
                   + "OR CAST(Anmeldebar AS CHAR) LIKE ? "
                   + "OR CAST(Aktiv AS CHAR) LIKE ? "
                   + "OR Kommentar LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 7; i++) {
                pstmt.setString(i, pattern);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToKurstermin(rs));
                }
            }
        }
        return results;
    }

    private Kurstermin mapRowToKurstermin(ResultSet rs) throws SQLException {
        Kurstermin termin = new Kurstermin();
        termin.setKursterminID(rs.getInt("KursterminID"));
        termin.setKursID(rs.getInt("KursID"));
        termin.setTermin(rs.getTimestamp("Termin"));
        termin.setTeilnehmerfrei(rs.getInt("Teilnehmerfrei"));
        termin.setAnmeldebar(rs.getBoolean("Anmeldebar"));
        termin.setAktiv(rs.getBoolean("Aktiv"));
        termin.setKommentar(rs.getString("Kommentar"));
        return termin;
    }
}
