package New.DAOs;

import New.Objekte.Mitglieder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class MitgliederDAO extends BaseDAO<Mitglieder> {

    public MitgliederDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Mitglieder findById(int id) throws SQLException {
        String sql = "SELECT * FROM Mitglieder WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Mitglieder(
                    rs.getInt("MitgliederID"),
                    rs.getString("Vorname"),
                    rs.getString("Nachname"),
                    rs.getString("Telefon"),
                    rs.getDate("Geburtstag"),
                    rs.getBoolean("Aktiv"),
                    rs.getString("Strasse"),
                    rs.getString("Hausnr"),
                    rs.getInt("OrtID"),
                    rs.getInt("ZahlungsdatenID"),
                    rs.getString("Mail")
                );
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void insert(Mitglieder entity) throws SQLException {
        String sql = "INSERT INTO Mitglieder (MitgliederID, Vorname, Nachname, Telefon, Geburtstag, Aktiv, Strasse, Hausnr, OrtID, ZahlungsdatenID, Mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getMitgliederID());
            ps.setString(2, entity.getVorname());
            ps.setString(3, entity.getNachname());
            ps.setString(4, entity.getTelefon());
            ps.setDate(5, entity.getGeburtstag() != null ? new Date(entity.getGeburtstag().getTime()) : null);
            ps.setBoolean(6, entity.isAktiv());
            ps.setString(7, entity.getStrasse());
            ps.setString(8, entity.getHausnr());
            ps.setInt(9, entity.getOrtID());
            ps.setInt(10, entity.getZahlungsdatenID());
            ps.setString(11, entity.getMail());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void update(Mitglieder entity) throws SQLException {
        String sql = "UPDATE Mitglieder SET Vorname = ?, Nachname = ?, Telefon = ?, Geburtstag = ?, Aktiv = ?, Strasse = ?, Hausnr = ?, OrtID = ?, ZahlungsdatenID = ?, Mail = ? WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getVorname());
            ps.setString(2, entity.getNachname());
            ps.setString(3, entity.getTelefon());
            ps.setDate(4, entity.getGeburtstag() != null ? new Date(entity.getGeburtstag().getTime()) : null);
            ps.setBoolean(5, entity.isAktiv());
            ps.setString(6, entity.getStrasse());
            ps.setString(7, entity.getHausnr());
            ps.setInt(8, entity.getOrtID());
            ps.setInt(9, entity.getZahlungsdatenID());
            ps.setString(10, entity.getMail());
            ps.setInt(11, entity.getMitgliederID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Mitglieder WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }
}
