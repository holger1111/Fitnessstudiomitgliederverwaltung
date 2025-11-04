package New.DAOs;

import New.Objekte.Zahlungsdaten;
import New.Validator.StringValidator;
import New.Validator.IBANValidator;
import New.Validator.BICValidator;
import New.Validator.IntValidator;
import New.Exception.StringException;
import New.Exception.PaymentDetailsException;
import New.Exception.IntException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZahlungsdatenDAO extends BaseDAO<Zahlungsdaten> {

    private final StringValidator nameValidator = new StringValidator();
    private final IBANValidator ibanValidator = new IBANValidator();
    private final BICValidator bicValidator = new BICValidator();
    private final IntValidator intValidator = new IntValidator();

    public ZahlungsdatenDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Zahlungsdaten findById(int id) throws SQLException, IntException {
        try {
            intValidator.validate(id);
        } catch (Exception e) {
            throw new IntException("Fehler bei der ZahlungsdatenID-Validierung: " + e.getMessage());
        }

        String sql = "SELECT ZahlungsdatenID, Name, IBAN, BIC FROM Zahlungsdaten WHERE ZahlungsdatenID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Zahlungsdaten(
                        rs.getInt("ZahlungsdatenID"),
                        rs.getString("Name"),
                        rs.getString("IBAN"),
                        rs.getString("BIC")
                );
            }
        } finally {
            closeResources(rs, ps);
        }
        return null;
    }

    @Override
    public void insert(Zahlungsdaten entity) throws SQLException, StringException, PaymentDetailsException, IntException {
        try {
            intValidator.validate(entity.getZahlungsdatenID());
        } catch (Exception e) {
            throw new IntException("Fehler bei ZahlungsdatenID: " + e.getMessage());
        }
        try {
            nameValidator.validate(entity.getName());
        } catch (Exception e) {
            throw new StringException("Fehler bei Name: " + e.getMessage());
        }
        try {
            ibanValidator.validate(entity.getIban());
        } catch (Exception e) {
            throw new PaymentDetailsException("Fehler bei IBAN: " + e.getMessage());
        }
        try {
            bicValidator.validate(entity.getBic());
        } catch (Exception e) {
            throw new PaymentDetailsException("Fehler bei BIC: " + e.getMessage());
        }

        String sql = "INSERT INTO Zahlungsdaten (ZahlungsdatenID, Name, IBAN, BIC) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getZahlungsdatenID());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getIban());
            ps.setString(4, entity.getBic());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void update(Zahlungsdaten entity) throws SQLException, StringException, PaymentDetailsException, IntException {
        try {
            intValidator.validate(entity.getZahlungsdatenID());
        } catch (Exception e) {
            throw new IntException("Fehler bei ZahlungsdatenID: " + e.getMessage());
        }
        try {
            nameValidator.validate(entity.getName());
        } catch (Exception e) {
            throw new StringException("Fehler bei Name: " + e.getMessage());
        }
        try {
            ibanValidator.validate(entity.getIban());
        } catch (Exception e) {
            throw new PaymentDetailsException("Fehler bei IBAN: " + e.getMessage());
        }
        try {
            bicValidator.validate(entity.getBic());
        } catch (Exception e) {
            throw new PaymentDetailsException("Fehler bei BIC: " + e.getMessage());
        }

        String sql = "UPDATE Zahlungsdaten SET Name = ?, IBAN = ?, BIC = ? WHERE ZahlungsdatenID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getIban());
            ps.setString(3, entity.getBic());
            ps.setInt(4, entity.getZahlungsdatenID());
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
            throw new IntException("Fehler bei ZahlungsdatenID: " + e.getMessage());
        }

        String sql = "DELETE FROM Zahlungsdaten WHERE ZahlungsdatenID = ?";
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
