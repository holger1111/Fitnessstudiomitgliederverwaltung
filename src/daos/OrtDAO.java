package daos;

import objects.Ort;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrtDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Ort> findAll() {
		String sql = "SELECT OrtID, PLZ, Ort FROM Ort";
		return getResults(sql, null, rs -> {
			try {
				int ortid = rs.getInt("OrtID");
				String PLZ = rs.getString("PLZ");
				String ort = rs.getString("Ort");
                return new Ort(ortid, PLZ, ort);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Ort findById(int id) {
		String sql = "SELECT OrtID, PLZ, Ort FROM Ort WHERE OrtID = ?";
		ArrayList<Ort> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int ortid = rs.getInt("OrtID");
				String PLZ = rs.getString("PLZ");
				String ort = rs.getString("Ort");
				return new Ort(ortid, PLZ, ort);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	public Ort findByPLZOrt(String plz, String Ort) {
		String sql = "SELECT OrtID, PLZ, Ort FROM Ort WHERE PLZ = ? AND Ort = ?";
		ArrayList<Ort> list = getResults(sql, new Object[]{plz, Ort}, rs -> {
			try {
				int ortid = rs.getInt("OrtID");
	            String PLZ = rs.getString("PLZ");
	            String ort = rs.getString("Ort");
				Ort ortO = new Ort(PLZ, ort);
				ortO.setOrtID(ortid);
				return ortO;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertOrt(Ort m) {
		String sql = "INSERT INTO Ort (OrtID, PLZ, Ort) VALUES (?, ?, ?)";
		Object[] params = { m.getOrtID(), m.getPLZ(), m.getOrt() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateOrt(Ort m) {
		String sql = "UPDATE Ort SET PLZ=?, Ort=? WHERE OrtID=?";
		Object[] params = { m.getOrtID(), m.getPLZ(), m.getOrt() };
		return update(sql, params);
	}

	// DELETE
	public int deleteOrt(int ortid) {
		String sql = "DELETE FROM Ort WHERE OrtID=?";
		Object[] params = { ortid };
		return delete(sql, params);
	}
}
