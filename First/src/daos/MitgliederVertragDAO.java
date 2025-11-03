package daos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MitgliederVertragDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<MitgliederVertrag> findAll() {
		String sql = "SELECT VertragNr, MitgliederID, VertragID, Vertragsbeginn, Vertragsende, Verlängerung, Aktiv, Gekündigt, Preisrabatt, IntervallID, ZahlungID, Trainingsbeginn, Kommentar FROM MitgliederVertrag";
		return getResults(sql, null, rs -> {
			try {
				int vertragnr = rs.getInt("VertragNr");
				int mitgliederid = rs.getInt("MitgliederID");
				int vertragid = rs.getInt("VertragID");
				LocalDate vertragsbeginn = rs.getDate("Vertragsbeginn").toLocalDate();
				LocalDate vertragsende = rs.getDate("Vertragsende").toLocalDate();
				boolean verlaengerung = rs.getBoolean("Verlängerung");
				boolean aktiv = rs.getBoolean("Aktiv");
				boolean gekuendigt = rs.getBoolean("Gekündigt");
				double preisrabatt = rs.getDouble("Preisrabatt");
				int intervallid = rs.getInt("IntervallID");
				int zahlungid = rs.getInt("ZahlungID");
				LocalDate trainingsbeginn = rs.getDate("Trainingsbeginn").toLocalDate();
				String kommentar = rs.getString("Kommentar");
                return new MitgliederVertrag(vertragnr, mitgliederid, vertragid, vertragsbeginn, vertragsende, verlaengerung, aktiv, gekuendigt, preisrabatt, intervallid, zahlungid, trainingsbeginn, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public MitgliederVertrag findById(int id) {
		String sql = "SELECT VertragNr, MitgliederID, VertragID, Vertragsbeginn, Vertragsende, Verlängerung, Aktiv, Gekündigt, Preisrabatt, IntervallID, ZahlungID, Trainingsbeginn, Kommentar FROM MitgliederVertrag WHERE VertragNr = ? AND MitgliederID = ?";
		ArrayList<MitgliederVertrag> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int vertragnr = rs.getInt("VertragNr");
				int mitgliederid = rs.getInt("MitgliederID");
				int vertragid = rs.getInt("VertragID");
				LocalDate vertragsbeginn = rs.getDate("Vertragsbeginn").toLocalDate();
				LocalDate vertragende = rs.getDate("Vertragsende").toLocalDate();
				boolean verlaengerung = rs.getBoolean("Verlängerung");
				boolean aktiv = rs.getBoolean("Aktiv");
				boolean gekuendigt = rs.getBoolean("Gekündigt");
				double preisrabatt = rs.getDouble("Preisrabatt");
				int intervallid = rs.getInt("IntervallID");
				int zahlungid = rs.getInt("ZahlungID");
				LocalDate trainingsbeginn = rs.getDate("Trainingsbeginn").toLocalDate();
				String kommentar = rs.getString("Kommentar");
				return new MitgliederVertrag(vertragnr, mitgliederid, vertragid, vertragsbeginn, vertragende, verlaengerung, aktiv, gekuendigt, preisrabatt, intervallid, zahlungid, trainingsbeginn, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertMitgliederVertrag(MitgliederVertrag m) {
		String sql = "INSERT INTO MitgliederVertrag (VertragNr, MitgliederID, VertragID, Vertragsbeginn, Vertragsende, Verlängerung, Aktiv, Gekündigt, Preisrabatt, IntervallID, ZahlungID, Trainingsbeginn, Kommentar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { m.getVertragNr(), m.getMitgliederID(), m.getVertragID(), Date.valueOf(m.getVertragsbeginn()), Date.valueOf(m.getVertragsende()), m.getVerlaengerung(), m.getAktiv(), m.getGekuendigt(), m.getPreisrabatt(), m.getIntervallID(), m.getZahlungID(), Date.valueOf(m.getTrainingsbeginn()), m.getKommentar() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateMitgliederVertrag(MitgliederVertrag m) {
		String sql = "UPDATE MitgliederVertrag SET Vertragsbeginn=?, Vertragsende=?, Verlängerung=?, Aktiv=?, Gekündigt=?, Preisrabatt=?, IntervallID=?, ZahlungID=?, Trainingsbeginn, Kommentar=? WHERE VertragNr=? AND MitgliederID=? AND VertragID=?";
		Object[] params = { m.getVertragNr(), m.getMitgliederID(), m.getVertragID(), Date.valueOf(m.getVertragsbeginn()), Date.valueOf(m.getVertragsende()), m.getVerlaengerung(), m.getAktiv(), m.getGekuendigt(), m.getPreisrabatt(), m.getIntervallID(), m.getZahlungID(), Date.valueOf(m.getTrainingsbeginn()), m.getKommentar() };
		return update(sql, params);
	}

	// DELETE
	public int deleteMitgliederVertrag(int vertragnr, int mitgliederid, int vertragid) {
		String sql = "DELETE FROM MitgliederVertrag WHERE VertragNr=? AND MitgliederID=? AND VertragID=?";
		Object[] params = { vertragnr, mitgliederid, vertragid };
		return delete(sql, params);
	}
}