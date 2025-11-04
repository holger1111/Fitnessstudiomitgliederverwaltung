package New.DAOs;

import New.Objekte.Interessenten;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InteressentenDAO extends BaseDAO<Interessenten> {

    public InteressentenDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Interessenten findById(int id) throws SQLException {
        String sql = "SELECT MitgliederID, Vorname, Nachname, Telefon FROM Mitglieder WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Interessenten(
                    rs.getInt("MitgliederID"),
                    rs.getString("Vorname"),
                    rs.getString("Nachname"),
                    rs.getString("Telefon")
                );
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void insert(Interessenten entity) throws SQLException {
        String sql = "INSERT INTO Mitglieder (MitgliederID, Vorname, Nachname, Telefon) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getMitgliederID());
            ps.setString(2, entity.getVorname());
            ps.setString(3, entity.getNachname());
            ps.setString(4, entity.getTelefon());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void update(Interessenten entity) throws SQLException {
        String sql = "UPDATE Mitglieder SET Vorname = ?, Nachname = ?, Telefon = ? WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getVorname());
            ps.setString(2, entity.getNachname());
            ps.setString(3, entity.getTelefon());
            ps.setInt(4, entity.getMitgliederID());
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
