package daos;

import objects.Vertrag;

import java.sql.SQLException;
import java.util.ArrayList;

public class VertragDAO extends DAO {
	
	// Suchmethoden
	public ArrayList<Vertrag> findAll() {
		String sql = "SELECT VertragID, Bezeichnung, Laufzeit, Grundpreis FROM Vertrag";
		return getResults(sql, null, rs -> {
			try {
				int vertragid = rs.getInt("VertragID");
				String bezeichnung = rs.getString("Bezeichnung");
				int laufzeit = rs.getInt("Laufzeit");
				double grundpreis = rs.getDouble("Grundpreis");
                return new Vertrag(vertragid, bezeichnung, laufzeit, grundpreis);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Vertrag findById(int id) {
		String sql = "SELECT VertragID, Bezeichnung, Laufzeit, Grundpreis FROM Vertrag WHERE VertragID = ?";
		ArrayList<Vertrag> list = getResults(sql, new Object[]{id}, rs -> {
			try {
				int vertragid = rs.getInt("VertragID");
				String bezeichnung = rs.getString("Bezeichnung");
				int laufzeit = rs.getInt("Laufzeit");
				double grundpreis = rs.getDouble("Grundpreis");
				return new Vertrag(vertragid, bezeichnung, laufzeit, grundpreis);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}

	public Vertrag findByBezeichnungLaufzeit(String bezeichnung, int laufzeit) {
		String sql = "SELECT VertragID, Bezeichnung, Laufzeit, Grundpreis FROM Vertrag WHERE Bezeichnung = ? AND Laufzeit = ?";
		ArrayList<Vertrag> list = getResults(sql, new Object[]{bezeichnung, laufzeit}, rs -> {
			try {
				int vertragid = rs.getInt("VertragID");
	            String Bezeichnung = rs.getString("Bezeichnung");
	            int Laufzeit = rs.getInt("Laufzeit");
	            double Grundpreis = rs.getDouble("Grundpreis");
	            Vertrag interessentO = new Vertrag(Bezeichnung, Laufzeit, Grundpreis);
				interessentO.setVertragID(vertragid);
				return interessentO;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	// INSERT
	public int insertVertrag(Vertrag m) {
		String sql = "INSERT INTO Vertrag (VertragID, Bezeichnung, Laufzeit, Grundpreis) VALUES (?, ?, ?, ?)";
		Object[] params = { m.getVertragID(), m.getBezeichnung(), m.getLaufzeit(), m.getGrundpreis() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateVertrag(Vertrag m) {
		String sql = "UPDATE Vertrag SET Bezeichnung=?, Laufzeit=?, Grundpreis=? WHERE VertragID=?";
		Object[] params = { m.getVertragID(), m.getBezeichnung(), m.getLaufzeit(), m.getGrundpreis() };
		return update(sql, params);
	}

	// DELETE
	public int deleteVertrag(int vertragid) {
		String sql = "DELETE FROM Vertrag WHERE VertragID=?";
		Object[] params = { vertragid };
		return delete(sql, params);
	}
}
