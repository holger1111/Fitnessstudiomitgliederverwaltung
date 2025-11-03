package daos;

import objects.Zahlung;

import java.sql.SQLException;
import java.util.ArrayList;

public class ZahlungDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Zahlung> findAll() {
		String sql = "SELECT ZahlungID, Zahlungsart FROM Zahlung";
		return getResults(sql, null, rs -> {
			try {
				int zahlungid = rs.getInt("ZahlungID");
				String zahlungsart = rs.getString("Zahlungsart");
                return new Zahlung(zahlungid, zahlungsart);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Zahlung findById(int id) {
		String sql = "SELECT ZahlungID, Zahlungsart FROM Zahlung WHERE ZahlungID = ?";
		ArrayList<Zahlung> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int zahlungid = rs.getInt("ZahlungID");
				String zahlungsart = rs.getString("Zahlungsart");
				return new Zahlung(zahlungid, zahlungsart);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	public Zahlung findByZahlungsart(String zahlungsart) {
		String sql = "SELECT ZahlungID, Zahlungsart FROM Zahlung WHERE Zahlungsart = ?";
		ArrayList<Zahlung> list = getResults(sql, new Object[]{zahlungsart}, rs -> {
			try {
				int zahlungid = rs.getInt("ZahlungID");
	            String Zahlungsart = rs.getString("Zahlungsart");
	            Zahlung zahlungO = new Zahlung(zahlungid, Zahlungsart);
				zahlungO.setZahlungID(zahlungid);
				return zahlungO;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	
	}
	
	// INSERT
	public int insertZahlung(Zahlung m) {
		String sql = "INSERT INTO Zahlung (ZahlungID, Zahlungsart) VALUES (?, ?)";
		Object[] params = { m.getZahlungID(), m.getZahlungsart() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateZahlung(Zahlung m) {
		String sql = "UPDATE Zahlung SET ZahlungID, Zahlungsart WHERE ZahlungID=?";
		Object[] params = { m.getZahlungID(), m.getZahlungsart() };
		return update(sql, params);
	}

	// DELETE
	public int deleteZahlung(int zahlungid) {
		String sql = "DELETE FROM Zahlung WHERE ZahlungID=?";
		Object[] params = { zahlungid };
		return delete(sql, params);
	}
}
