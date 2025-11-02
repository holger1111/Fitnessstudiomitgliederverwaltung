package daos;

import objects.Trainer;

import java.sql.SQLException;
import java.util.ArrayList;

public class TrainerDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Trainer> findAll() {
		String sql = "SELECT TrainerID, Vorname, Nachname, Kommentar FROM Trainer";
		return getResults(sql, null, rs -> {
			try {
				int trainerid = rs.getInt("TrainerID");
				String vorname = rs.getString("Vorname");
				String nachname = rs.getString("Nachname");
				String kommentar = rs.getString("Kommentar");
                return new Trainer(trainerid, vorname, nachname, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Trainer findById(int id) {
		String sql = "SELECT TrainerID, Vorname, Nachname, Kommentar FROM Trainer WHERE TrainerID = ?";
		ArrayList<Trainer> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int trainerid = rs.getInt("TrainerID");
				String vorname = rs.getString("Vorname");
				String nachname = rs.getString("Nachname");
				String kommentar = rs.getString("Kommentar");
				return new Trainer(trainerid, vorname, nachname, kommentar);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	public Trainer findByVornameNachname(String vorname, String nachname) {
		String sql = "SELECT MitgliederID, Vorname, Nachname, Telefon, Mail FROM Trainer WHERE Vorname = ? AND Nachname = ? AND Telefon = ? AND Mail = ?";
		ArrayList<Trainer> list = getResults(sql, new Object[]{vorname, nachname}, rs -> {
			try {
				int trainerid = rs.getInt("TrainerID");
	            String Vorname = rs.getString("Vorname");
	            String Nachname = rs.getString("Nachname");
	            Trainer interessentO = new Trainer(Vorname, Nachname);
				interessentO.setTrainerID(trainerid);
				return interessentO;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertTrainer(Trainer m) {
		String sql = "INSERT INTO Trainer (TrainerID, Vorname, Nachname, Kommentar) VALUES (?, ?, ?, ?)";
		Object[] params = { m.getTrainerID(), m.getVorname(), m.getNachname(), m.getKommentar() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateTrainer(Trainer m) {
		String sql = "UPDATE Trainer SET Vorname=?, Nachname=?, Kommentar=? WHERE TrainerID=?";
		Object[] params = { m.getTrainerID(), m.getVorname(), m.getNachname(), m.getKommentar() };
		return update(sql, params);
	}

	// DELETE
	public int deleteTrainer(int trainerid) {
		String sql = "DELETE FROM Trainer WHERE TrainerID=?";
		Object[] params = { trainerid };
		return delete(sql, params);
	}
}
