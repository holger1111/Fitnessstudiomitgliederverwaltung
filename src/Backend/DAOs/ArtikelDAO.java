package Backend.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Backend.Objekte.Artikel;
import Backend.Objekte.Kategorie;

public class ArtikelDAO extends BaseDAO<Artikel> {

    public ArtikelDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(Artikel artikel) throws SQLException {
        String sql = "INSERT INTO Artikel (Name, Einzelpreis, Kommentar, KategorieID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, artikel.getName());
            ps.setDouble(2, artikel.getEinzelpreis());
            ps.setString(3, artikel.getKommentar());
            ps.setInt(4, artikel.getKategorie() != null ? artikel.getKategorie().getKategorieID() : 0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    artikel.setArtikelID(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Artikel findById(int id) throws SQLException {
        String sql = "SELECT a.ArtikelID, a.Name, a.Einzelpreis, a.Kommentar, " +
                     "k.KategorieID, k.Bezeichnung " +
                     "FROM Artikel a LEFT JOIN Kategorie k ON a.KategorieID = k.KategorieID "+
                     "WHERE a.ArtikelID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToArtikel(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Artikel artikel) throws SQLException {
        String sql = "UPDATE Artikel SET Name = ?, Einzelpreis = ?, Kommentar = ?, KategorieID = ? WHERE ArtikelID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, artikel.getName());
            ps.setDouble(2, artikel.getEinzelpreis());
            ps.setString(3, artikel.getKommentar());
            ps.setInt(4, artikel.getKategorie() != null ? artikel.getKategorie().getKategorieID() : 0);
            ps.setInt(5, artikel.getArtikelID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Artikel WHERE ArtikelID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Artikel> findAll() throws SQLException {
        String sql = "SELECT a.ArtikelID, a.Name, a.Einzelpreis, a.Kommentar, " +
                     "k.KategorieID, k.Bezeichnung " +
                     "FROM Artikel a LEFT JOIN Kategorie k ON a.KategorieID = k.KategorieID";
        List<Artikel> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToArtikel(rs));
            }
        }
        return list;
    }

    public List<Artikel> searchAllAttributes(String searchTerm) throws SQLException {
        String sql = "SELECT a.ArtikelID, a.Name, a.Einzelpreis, a.Kommentar, " +
                     "k.KategorieID, k.Bezeichnung " +
                     "FROM Artikel a LEFT JOIN Kategorie k ON a.KategorieID = k.KategorieID " +
                     "WHERE CAST(a.ArtikelID AS CHAR) LIKE ? OR " +
                     "LOWER(a.Name) LIKE ? OR LOWER(a.Kommentar) LIKE ? OR " +
                     "LOWER(k.Bezeichnung) LIKE ?";
        List<Artikel> result = new ArrayList<>();
        String like = "%" + searchTerm.toLowerCase() + "%";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToArtikel(rs));
                }
            }
        }
        return result;
    }

    private Artikel mapRowToArtikel(ResultSet rs) throws SQLException {
        Artikel artikel = new Artikel(
            rs.getInt("ArtikelID"),
            rs.getString("Name"),
            rs.getDouble("Einzelpreis"),
            rs.getString("Kommentar")
        );
        int kategorieID = rs.getInt("KategorieID");
        if (kategorieID > 0) {
            Kategorie kategorie = new Kategorie(kategorieID, rs.getString("Bezeichnung"));
            artikel.setKategorie(kategorie);
        }
        return artikel;
    }
}
