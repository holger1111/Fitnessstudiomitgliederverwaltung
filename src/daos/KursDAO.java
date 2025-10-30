package daos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class KursDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Kurs> findAll() {
		String sql = "SELECT KursID, Bezeichnung, Kostenfrei, Aktiv, Teilnehmerzahl, Preis, AnzahlTermine, Kommentar FROM Kurs";
		return getResults(sql, null, rs -> {
			try {
				int kursid = rs.getInt("KursID");
				String bezeichnung = rs.getString("Bezeichnung");
				boolean kostenfrei = rs.getBoolean("Kostenfrei");
				boolean aktiv = rs.getBoolean("Aktiv");
				int teilnehmerzahl = rs.getInt("Teilnehmerzahl");
				double preis = rs.getDouble("Preis");
				int anzahltermine = rs.getInt("AnzahlTermine");
				String kommentar = rs.getString("Kommentar");
                return new Kurs(kursid, bezeichnung, kostenfrei, aktiv, teilnehmerzahl, preis, anzahltermine, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Kurs findById(int id) {
		String sql = "SELECT KursID, Bezeichnung, Kostenfrei, Aktiv, Teilnehmerzahl, Preis, AnzahlTermine, Kommentar FROM Kurs WHERE KursID = ?";
		ArrayList<Kurs> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int kursid = rs.getInt("KursID");
				String bezeichnung = rs.getString("Bezeichnung");
				boolean kostenfrei = rs.getBoolean("Kostenfrei");
				boolean aktiv = rs.getBoolean("Aktiv");
				int teilnehmerzahl = rs.getInt("Teilnehmerzahl");
				double preis = rs.getDouble("Preis");
				int anzahltermine = rs.getInt("AnzahlTermine");
				String kommentar = rs.getString("Kommentar");
				return new Kurs(kursid, bezeichnung, kostenfrei, aktiv, teilnehmerzahl, preis, anzahltermine, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertKurs(Kurs m) {
		String sql = "INSERT INTO Kurs (KursID, Bezeichnung, Kostenfrei, Aktiv, Preis, AnzahlTermine, Kommentar VALUES (?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { m.getKursID, m.getBezeichnung(), m.getKostenfrei(), m.getAktiv(), m.getPreis(), m.getAnzahlTermine(), m.getKommentar() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateKurs(Kurs m) {
		String sql = "UPDATE Kurs SET Bezeichnung=?, Kostenfrei=?, Aktiv=?, Teilnehmerzahl=?, Preis=?, AnzahlTermine=?, Kommentar=? WHERE KursID=?";
		Object[] params = { m.getKursID, m.getBezeichnung(), m.getKostenfrei(), m.getAktiv(), m.getPreis(), m.getAnzahlTermine(), m.getKommentar() };
		return update(sql, params);
	}

	// DELETE
	public int deleteKurs(int kursid) {
		String sql = "DELETE FROM Kurs WHERE KursID=?";
		Object[] params = { kursid };
		return delete(sql, params);
	}
}