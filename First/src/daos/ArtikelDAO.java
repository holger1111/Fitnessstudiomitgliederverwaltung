package daos;

import objects.Artikel;

import java.sql.SQLException;
import java.util.ArrayList;

public class ArtikelDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Artikel> findAll() {
		String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar FROM Artikel";
		return getResults(sql, null, rs -> {
			try {
				int artikelid = rs.getInt("ArtikelID");
				String name = rs.getString("Name");
				double einzelpreis = rs.getDouble("Einzelpreis");
				String kommentar = rs.getString("Kommentar");
                return new Artikel(artikelid, name, einzelpreis, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Artikel findById(int id) {
		String sql = "SELECT ArtikelID, Name, Einzelpreis, Kommentar, ZahlungID FROM Artikel WHERE ArtikelID = ?";
		ArrayList<Artikel> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int artikelid = rs.getInt("ArtikelID");
				String name = rs.getString("Name");
				double einzelpreis = rs.getDouble("Einzelpreis");
				String kommentar = rs.getString("Kommentar");
				return new Artikel(artikelid, name, einzelpreis, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	public Artikel findByNameEinzelpreis(String name, double einzelpreis) {
		String sql = "SELECT ArtikelID, Name, Einzelpreis FROM Artikel WHERE Name = ? AND Einzelpreis = ?";
		ArrayList<Artikel> list = getResults(sql, new Object[]{name, einzelpreis}, rs -> {
			try {
				int artikelid = rs.getInt("ArtikelID");
	            String Name = rs.getString("Name");
	            double Einzelpreis = rs.getDouble("Einzelpreis");
	            Artikel interessentO = new Artikel(Name, Einzelpreis);
				interessentO.setArtikelID(artikelid);
				return interessentO;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertArtikel(Artikel m) {
		String sql = "INSERT INTO Artikel (ArtikelID, Name, Einzelpreis, Kommentar) VALUES (?, ?, ?, ?)";
		Object[] params = { m.getArtikelID(), m.getName(), m.getEinzelpreis(), m.getKommentar() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateArtikel(Artikel m) {
		String sql = "UPDATE Artikel SET Name=?, Einzelpreis=?, Kommentar=? WHERE ArtikelID=?";
		Object[] params = { m.getName(), m.getEinzelpreis(), m.getKommentar() };
		return update(sql, params);
	}

	// DELETE
	public int deleteArtikel(int artikelid) {
		String sql = "DELETE FROM Artikel WHERE ArtikelID=?";
		Object[] params = { artikelid };
		return delete(sql, params);
	}
}
