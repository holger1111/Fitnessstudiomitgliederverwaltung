package daos;

import java.sql.Timestamp;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class IntervallDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Intervall> findAll() {
		String sql = "SELECT IntervallID, Zahlungsintervall, Bezeichnung FROM Intervall";
		return getResults(sql, null, rs -> {
			try {
				int intervallid = rs.getInt("IntervallID");
				String zahlungsintervall = rs.getString("Zahlungsintervall");
				String bezeichnung = rs.getString("Bezeichnung");
                return new Intervall(intervallid, zahlungsintervall, bezeichnung);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Intervall findById(int id) {
		String sql = "SELECT IntervallID, Zahlungsintervall, Bezeichnung FROM Intervall WHERE IntervallID = ?";
		ArrayList<Intervall> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int intervallid = rs.getInt("IntervallID");
				String zahlungsintervall = rs.getString("Zahlungsintervall");
				String bezeichnung = rs.getString("Bezeichnung");
				return new Intervall(intervallid, zahlungsintervall, bezeichnung);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertIntervall(Intervall m) {
		String sql = "INSERT INTO Intervall (IntervallID, Zahlungsintervall, Bezeichnung) VALUES (?, ?, ?)";
		Object[] params = { m.getIntervallID(), m.getZahlungsintervall(), m.getBezeichnung() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateIntervall(Intervall m) {
		String sql = "UPDATE Intervall SET Zahlungsintervall=?, Bezeichnung=? WHERE IntervallID=?";
		Object[] params = { m.getIntervallID(), m.getZahlungsintervall(), m.getBezeichnung() };
		return update(sql, params);
	}

	// DELETE
	public int deleteIntervall(int intervallid) {
		String sql = "DELETE FROM Intervall WHERE IntervallID=?";
		Object[] params = { intervallid };
		return delete(sql, params);
	}
}
