package daos;

import java.sql.Timestamp;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class BestellungDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Bestellung> findAll() {
		String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID FROM Bestellung";
		return getResults(sql, null, rs -> {
			try {
				int bestellungid = rs.getInt("BestellungID");
				int mitgliederid = rs.getInt("MitgliederID");
				double gesamtpreis = rs.getDouble("Gesamtpreis");
				Timestamp bestelldatum = rs.getTimestamp("Bestelldatum");
				int zahlungid = rs.getInt("ZahlungID");
                return new Bestellung(bestellungid, mitgliederid, gesamtpreis, bestelldatum, zahlungid);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Bestellung findById(int id) {
		String sql = "SELECT BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID FROM Bestellung WHERE BestellungID = ?";
		ArrayList<Bestellung> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int bestellungid = rs.getInt("BestellungID");
				int mitgliederid = rs.getInt("MitgliederID");
				double gesamtpreis = rs.getDouble("Gesamtpreis");
				Timestamp bestelldatum = rs.getTimestamp("Bestelldatum");
				int zahlungid = rs.getInt("ZahlungID");
				return new Bestellung(bestellungid, mitgliederid, gesamtpreis, bestelldatum, zahlungid);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertBestellung(Bestellung m) {
		String sql = "INSERT INTO Bestellung (BestellungID, MitgliederID, Gesamtpreis, Bestelldatum, ZahlungID) VALUES (?, ?, ?, ?, ?)";
		Object[] params = { m.getBestellungID(), m.getMitgliederID(), m.getGesamtpreis(), Date.valueIf(m.getBestelldatum()), m.getZahlungID() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateBestellung(Bestellung m) {
		String sql = "UPDATE Bestellung SET Gesamtpreis=?, Bestelldatum=?, ZahlungID=? WHERE BestellungID=?";
		Object[] params = { m.getGesamtpreis(), m.getBestelldatum(), m.getZahlungID(), m.getBestellungID() };
		return update(sql, params);
	}

	// DELETE
	public int deleteBestellung(int bestellungid) {
		String sql = "DELETE FROM Bestellung WHERE BestellungID=?";
		Object[] params = { bestellungid };
		return delete(sql, params);
	}
}
