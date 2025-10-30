package daos;

import objects.Mitglieder;
import objects.Interessent;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MitgliederDAO extends DAO {

	// Suchmethoden
	public ArrayList<Mitglieder> findAll() {
		String sql = "SELECT MitgliederID, Vorname, Nachname, Geburtsdatum, Aktiv, Straße, Hausnr, OrtID, ZahlungsdatenID, Telefon, Mail FROM Mitglieder";
		return getResults(sql, null, rs -> {
			try {
				int mitgliederid = rs.getInt("MitgliederID");
				String vorname = rs.getString("Vorname");
				String nachname = rs.getString("Nachname");
				LocalDate geburtsdatum = rs.getDate("Geburtsdatum").toLocalDate();
				boolean aktiv = rs.getBoolean("Aktiv");
				String strasse = rs.getString("Straße");
				String hausnr = rs.getString("Hausnr");
				int ortid = rs.getInt("OrtID");
				int zahlungsdatenid = rs.getInt("ZahlungsdatenID");
				String telefon = rs.getString("Telefon");
				String mail = rs.getString("Mail");
				return new Mitglieder(mitgliederid, vorname, nachname, geburtsdatum, aktiv, strasse, hausnr, ortid,
						zahlungsdatenid, telefon, mail);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Mitglieder findById(int id) {
		String sql = "SELECT MitgliederID, Vorname, Nachname, Geburtsdatum, Aktiv, Straße, Hausnr, OrtID, ZahlungsdatenID, Telefon, Mail FROM Mitglieder WHERE MitgliederID = ?";
		ArrayList<Mitglieder> list = getResults(sql, new Object[] { id }, rs -> {
			try {
				int mitgliederid = rs.getInt("MitgliederID");
				String vorname = rs.getString("Vorname");
				String nachname = rs.getString("Nachname");
				LocalDate geburtsdatum = rs.getDate("Geburtsdatum").toLocalDate();
				boolean aktiv = rs.getBoolean("Aktiv");
				String strasse = rs.getString("Straße");
				String hausnr = rs.getString("Hausnr");
				int ortid = rs.getInt("OrtID");
				int zahlungsdatenid = rs.getInt("ZahlungsdatenID");
				String telefon = rs.getString("Telefon");
				String mail = rs.getString("Mail");
				return new Mitglieder(mitgliederid, vorname, nachname, geburtsdatum, aktiv, strasse, hausnr, ortid,
						zahlungsdatenid, telefon, mail);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	public Interessent findByVornameNachnameTelefonMail(String vorname, String nachname, String telefon, String mail) {
		String sql = "SELECT MitgliederID, Vorname, Nachname, Telefon, Mail FROM Mitglieder WHERE Vorname = ? AND Nachname = ? AND Telefon = ? AND Mail = ?";
		ArrayList<Interessent> list = getResults(sql, new Object[]{vorname, nachname, telefon, mail}, rs -> {
			try {
				int mitgliederid = rs.getInt("MitgliederID");
	            String Vorname = rs.getString("Vorname");
	            String Nachname = rs.getString("Nachname");
	            String Telefon = rs.getString("Telefon");
	            String Mail = rs.getString("Mail");
				Interessent interessentO = new Interessent(Vorname, Nachname, Telefon, Mail);
				interessentO.setMitgliederID(mitgliederid);
				return interessentO;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		});
		return list.isEmpty() ? null : list.get(0);
	}
	
	
	// INSERT
	public int insertMitglieder(Mitglieder m) {
		String sql = "INSERT INTO Mitglieder (MitgliederID, Vorname, Nachname, Geburtsdatum, Aktiv, Straße, Hausnr, OrtID, ZahlungsdatenID, Telefon, Mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { m.getMitgliederID(), m.getVorname(), m.getNachname(), Date.valueOf(m.getGeburtsdatum()),
				m.isAktiv(), m.getStrasse(), m.getHausnr(), m.getOrtID(), m.getZahlungsdatenID(), m.getTelefon(),
				m.getMail() };
		return insert(sql, params);
	}

	// UPDATE
	public int updateMitglied(Mitglieder m) {
		String sql = "UPDATE Mitglieder SET Vorname=?, Nachname=?, Geburtsdatum=?, Aktiv=?, Straße=?, Hausnr=?, OrtID=?, ZahlungsdatenID=?, Telefon=?, Mail=? WHERE MitgliederID=?";
		Object[] params = { m.getVorname(), m.getNachname(), Date.valueOf(m.getGeburtsdatum()), m.isAktiv(),
				m.getStrasse(), m.getHausnr(), m.getOrtID(), m.getZahlungsdatenID(), m.getTelefon(), m.getMail(),
				m.getMitgliederID() };
		return update(sql, params);
	}

	// DELETE
	public int deleteMitglied(int mitgliederid) {
		String sql = "DELETE FROM Mitglieder WHERE MitgliederID=?";
		Object[] params = { mitgliederid };
		return delete(sql, params);
	}
}