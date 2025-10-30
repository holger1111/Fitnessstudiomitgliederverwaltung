package daos;

import java.sql.Timestamp;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ArtikelBestellungDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<ArtikelBestellung> findAll() {
		String sql = "SELECT BestellungID, ArtikelID, Name, Einzelpreis, Kommentar FROM ArtikelBestellung";
		return getResults(sql, null, rs -> {
			try {
				int bestellungid = rs.getInt("BestellungID");
				int artikelid = rs.getInt("ArtikelID");
				int menge = rs.getInt("Menge");
				double aufaddiert = rs.getDouble("Aufaddiert");
                return new ArtikelBestellung(bestellungid, artikelid, menge, aufaddiert);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public ArtikelBestellung findById(int id) {
		String sql = "SELECT BestellungID, ArtikelID, Name, Einzelpreis, Kommentar, ZahlungID FROM ArtikelBestellung WHERE BestellungID=? AND ArtikelID = ?";
		ArrayList<ArtikelBestellung> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int bestellungid = rs.getInt("BestellungID");
				int artikelid = rs.getInt("ArtikelID");
				int menge = rs.getInt("Menge");
				double aufaddiert = rs.getDouble("Aufaddiert");
				return new ArtikelBestellung(bestellungid, artikelid, menge, aufaddiert);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertArtikelBestellung(ArtikelBestellung m) {
		String sql = "INSERT INTO ArtikelBestellung (BestellungID, ArtikelID, Menge, Aufaddiert) VALUES (?, ?, ?, ?)";
		Object[] params = { m.getBestellungID(), m.getArtikelID(), m.getMenge(), m.getAufaddiert() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateArtikelBestellung(ArtikelBestellung m) {
		String sql = "UPDATE ArtikelBestellung SET Name=?, Einzelpreis=?, Kommentar=? WHERE BestellungID=? AND ArtikelID=?";
		Object[] params = { m.getBestellungID(), m.getArtikelID(), m.getMenge(), m.getAufaddiert() };
		return update(sql, params);
	}

	// DELETE
	public int deleteArtikelBestellung(int bestellungid, int artikelid) {
		String sql = "DELETE FROM ArtikelBestellung WHERE BestellungID=? AND ArtikelID=?";
		Object[] params = { bestellungid, artikelid };
		return delete(sql, params);
	}
}
