package daos;

import objects.Zahlungsdaten;

import java.sql.SQLException;
import java.util.ArrayList;

public class ZahlungsdatenDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Zahlungsdaten> findAll() {
		String sql = "SELECT ZahlungsdatenID, Name, IBAN, BIC FROM Zahlungsdaten";
		return getResults(sql, null, rs -> {
			try {
				int zahlungsdatenid = rs.getInt("ZahlungsdatenID");
				String name = rs.getString("Name");
				String IBAN = rs.getString("IBAN");
				String BIC = rs.getString("BIC");
                return new Zahlungsdaten(zahlungsdatenid, name, IBAN, BIC);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Zahlungsdaten findById(int id) {
		String sql = "SELECT ZahlungsdatenID, Name, IBAN, BIC FROM Zahlungsdaten WHERE ZahlungsdatenID = ?";
		ArrayList<Zahlungsdaten> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int zahlungsdatenid = rs.getInt("ZahlungsdatenID");
				String name = rs.getString("Name");
				String IBAN = rs.getString("IBAN");
				String BIC = rs.getString("BIC");
				return new Zahlungsdaten(zahlungsdatenid, name, IBAN, BIC);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	public Zahlungsdaten findByNameIBANBIC(String NAME, String iban, String bic) {
		String sql = "SELECT ZahlungsdatenID, Name, IBAN, BIC FROM Zahlungsdaten WHERE Name = ? AND IBAN = ? AND BIC = ?";
		ArrayList<Zahlungsdaten> list = getResults(sql, new Object[]{NAME, iban, bic}, rs -> {
			try {
				int zahlungsdatenid = rs.getInt("ZahlungsdatenID");
				String name = rs.getString("Name");
	            String IBAN = rs.getString("IBAN");
	            String BIC = rs.getString("BIC");
	            Zahlungsdaten zahlungsdatenO = new Zahlungsdaten(name, IBAN, BIC);
	            zahlungsdatenO.setZahlungsdatenid(zahlungsdatenid);
				return zahlungsdatenO;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertZahlungsdaten(Zahlungsdaten m) {
		String sql = "INSERT INTO Zahlungsdaten (ZahlungsdatenID, Name, IBAN, BIC) VALUES (?, ?, ?, ?)";
		Object[] params = { m.getZahlungsdatenID(), m.getName(), m.getIBAN(), m.getBIC() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateZahlungsdaten(Zahlungsdaten m) {
		String sql = "UPDATE Zahlungsdaten SET Name=?, Einzelpreis=?, Kommentar=? WHERE ZahlungsdatenID=?";
		Object[] params = { m.getZahlungsdatenID(), m.getName(), m.getIBAN(), m.getBIC() };
		return update(sql, params);
	}

	// DELETE
	public int deleteZahlungsdaten(int zahlungsdatenid) {
		String sql = "DELETE FROM Zahlungsdaten WHERE ZahlungsdatenID=?";
		Object[] params = { zahlungsdatenid };
		return delete(sql, params);
	}
}
