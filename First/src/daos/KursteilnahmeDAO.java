package daos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class KursteilnahmeDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Kursteilnahme> findAll() {
		String sql = "SELECT MitgliederID, KursterminID, Angemeldet, Anmeldezeit, Abgemeldet, Abmeldezeit, Aktiv, Kommentar FROM Kursteilnahme";
		return getResults(sql, null, rs -> {
			try {
				int mitgliederid = rs.getInt("MitgliederID");
				int kursterminid = rs.getInt("KursterminID");
				boolean angemeldet = rs.getBoolean("Angemeldet");
				LocalDateTime anmeldezeit = rs.getTimestamp("Anmeldezeit").toLocalDateTime();
				boolean abgemeldet = rs.getBoolean("Abgemeldet");
				LocalDateTime abmeldezeit = rs.getTimestamp("Abmeldezeit").toLocalDateTime();
				boolean aktiv = rs.getBoolean("Aktiv");
				String kommentar = rs.getString("Kommentar");
                return new Kursteilnahme(mitgliederid, kursterminid, angemeldet, anmeldezeit, abgemeldet, abmeldezeit, aktiv, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Kursteilnahme findById(int id) {
		String sql = "SELECT MitgliederID, KursterminID, Angemeldet, Anmeldezeit, Abgemeldet, Abmeldezeit, Aktiv, Kommentar FROM Kursteilnahme WHERE MitgliederID = ?";
		ArrayList<Kursteilnahme> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int mitgliederid = rs.getInt("KursterminID");
				int kursterminid = rs.getInt("KursterminID");
				boolean angemeldet = rs.getBoolean("Angemeldet");
				LocalDateTime anmeldezeit = rs.getTimestamp("Anmeldezeit").toLocalDateTime();
				boolean abgemeldet = rs.getBoolean("Abgemeldet");
				LocalDateTime abmeldezeit = rs.getTimestamp("Abmeldezeit").toLocalDateTime();
				boolean aktiv = rs.getBoolean("Aktiv");
				String kommentar = rs.getString("Kommentar");
				return new Kursteilnahme(mitgliederid, kursterminid, angemeldet, anmeldezeit, abgemeldet, abmeldezeit, aktiv, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertKursteilnahme(Kursteilnahme m) {
		String sql = "INSERT INTO Kursteilnahme (MitgliederID, KursterminID, Angemeldet, Anmeldezeit, Abgemeldet, Abmeldezeit, Aktiv, Kommentar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { m.getKursterminID, m.getKursID(), Timestamp.valueOf(m.getTermin()), m.getTrainerID(), m.getTeilnehmerfrei(), m.getAnmeldbar(), m.getAktiv(), m.getKommentar() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateKursteilnahme(Kursteilnahme m) {
		String sql = "UPDATE Kursteilnahme SET Angemeldet=?, Anmeldezeit=?, Abgemeldet=?, Abmeldezeit=?, Aktiv=?, Kommentar=? WHERE MitgliederID=? AND KursterminID=?";
		Object[] params = { m.getKursterminID, m.getKursID(), Timestamp.valueOf(m.getTermin()), m.getTrainerID(), m.getTeilnehmerfrei(), m.getAnmeldbar(), m.getAktiv(), m.getKommentar() };
		return update(sql, params);
	}

	// DELETE
	public int deleteKursteilnahme(int mitgliederid, int kursterminid) {
		String sql = "DELETE FROM Kursteilnahme WHERE MitgliederID=? AND KursterminID=?";
		Object[] params = { mitgliederid, kursterminid };
		return delete(sql, params);
	}
}