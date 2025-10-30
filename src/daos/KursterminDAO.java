package daos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class KursterminDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Kurstermin> findAll() {
		String sql = "SELECT KursterminID, KursID, Termin, TrainerID, Teilnehmerfrei, Anmeldbar, Aktiv, Kommentar FROM Kurstermin";
		return getResults(sql, null, rs -> {
			try {
				int kursterminid = rs.getInt("KursterminID");
				int kursid = rs.getInt("KursID");
				LocalDateTime termin = rs.getTimestamp("Termin").toLocalDateTime();
				int trainerid = rs.getInt("TrainerID");
				int teilnehmerfrei = rs.getInt("Teilnehmerfrei");
				boolean anmeldbar = rs.getBoolean("Anmeldbar");
				boolean aktiv = rs.getBoolean("Aktiv");
				String kommentar = rs.getString("Kommentar");
                return new Kurstermin(kursterminid, kursid, termin, trainerid, teilnehmerfrei, anmeldbar, aktiv, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Kurstermin findById(int id) {
		String sql = "SELECT KursterminID, KursID, Termin, TrainerID, Teilnehmerfrei, Anmeldbar, Aktiv, Kommentar FROM Kurstermin WHERE KursterminID = ?";
		ArrayList<Kurstermin> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int kursterminid = rs.getInt("KursterminID");
				int kursid = rs.getInt("KursID");
				LocalDateTime termin = rs.getTimestamp("Termin").toLocalDateTime();
				int trainerid = rs.getInt("TrainerID");
				int teilnehmerfrei = rs.getInt("Teilnehmerfrei");
				boolean anmeldbar = rs.getBoolean("Anmeldbar");
				boolean aktiv = rs.getBoolean("Aktiv");
				String kommentar = rs.getString("Kommentar");
				return new Kurstermin(kursterminid, kursid, termin, trainerid, teilnehmerfrei, anmeldbar, aktiv, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertKurstermin(Kurstermin m) {
		String sql = "INSERT INTO Kurstermin (KursterminID, KursID, Termin, TrainerID, Teilnehmerfrei, Anmeldbar, Aktiv, Kommentar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { m.getKursterminID, m.getKursID(), Timestamp.valueOf(m.getTermin()), m.getTrainerID(), m.getTeilnehmerfrei(), m.getAnmeldbar(), m.getAktiv(), m.getKommentar() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateKurstermin(Kurstermin m) {
		String sql = "UPDATE Kurstermin SET KursID=?, Termin=?, TrainerID=?, Teilnehmerfrei=?, Anmeldbar=?, Aktiv=?, Kommentar=? WHERE KursterminID=?";
		Object[] params = { m.getKursterminID, m.getKursID(), Timestamp.valueOf(m.getTermin()), m.getTrainerID(), m.getTeilnehmerfrei(), m.getAnmeldbar(), m.getAktiv(), m.getKommentar() };
		return update(sql, params);
	}

	// DELETE
	public int deleteKurstermin(int kursterminid) {
		String sql = "DELETE FROM Kurstermin WHERE KursterminID=?";
		Object[] params = { kursterminid };
		return delete(sql, params);
	}
}