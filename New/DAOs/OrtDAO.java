package DAOs;

import Objekte.Ort;
import Validator.StringValidator;
import Validator.IntValidator;
import Exception.TooLongException;
import Exception.TooShortException;
import Exception.StringException;
import Exception.IntException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrtDAO extends BaseDAO<Ort> {

    private final IntValidator intValidator = new IntValidator();
    private final StringValidator stringValidator = new StringValidator();

    public OrtDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Ort findById(int id) throws SQLException, IntException {
        try {
            intValidator.validate(id);
        } catch (Exception e) {
            throw new IntException("Fehler bei der OrtID-Validierung: " + e.getMessage());
        }
        String sql = "SELECT OrtID, PLZ, Ort FROM Orte WHERE OrtID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Ort(
                    rs.getInt("OrtID"),
                    rs.getString("PLZ"),
                    rs.getString("Ort")
                );
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void insert(Ort entity) throws SQLException, StringException, TooShortException, TooLongException, IntException {
        try {
            intValidator.validate(entity.getOrtID());
        } catch (Exception e) {
            throw new IntException("Fehler bei OrtID: " + e.getMessage());
        }
        try {
            stringValidator.validate(entity.getPLZ());
        } catch (Exception e) {
            throw new StringException("Fehler bei PLZ: " + e.getMessage());
        }
        try {
            stringValidator.checkLength(entity.getPLZ(), "PLZ", 5, 5);
        } catch (TooShortException | TooLongException lenEx) {
            throw lenEx;
        }
        try {
            stringValidator.validate(entity.getName());
        } catch (Exception e) {
            throw new StringException("Fehler bei Ort: " + e.getMessage());
        }

        String sql = "INSERT INTO Orte (OrtID, PLZ, Ort) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getOrtID());
            ps.setString(2, entity.getPLZ());
            ps.setString(3, entity.getName());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void update(Ort entity) throws SQLException, StringException, TooShortException, TooLongException, IntException {
        try {
            intValidator.validate(entity.getOrtID());
        } catch (Exception e) {
            throw new IntException("Fehler bei OrtID: " + e.getMessage());
        }
        try {
            stringValidator.validate(entity.getPLZ());
        } catch (Exception e) {
            throw new StringException("Fehler bei PLZ: " + e.getMessage());
        }
        try {
            stringValidator.checkLength(entity.getPLZ(), "PLZ", 5, 5);
        } catch (TooShortException | TooLongException lenEx) {
            throw lenEx;
        }
        try {
            stringValidator.validate(entity.getName());
        } catch (Exception e) {
            throw new StringException("Fehler bei Ort: " + e.getMessage());
        }

        String sql = "UPDATE Orte SET PLZ = ?, Ort = ? WHERE OrtID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getPLZ());
            ps.setString(2, entity.getName());
            ps.setInt(3, entity.getOrtID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException, IntException {
        try {
            intValidator.validate(id);
        } catch (Exception e) {
            throw new IntException("Fehler bei OrtID: " + e.getMessage());
        }
        String sql = "DELETE FROM Orte WHERE OrtID = ?";
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
