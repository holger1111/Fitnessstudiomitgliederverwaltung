package objects;

import daos.MitgliederDAO;
import java.util.Objects;

public class Interessent {

	// Attribute

	private int mitgliederid;
	private String vorname = null;
	private String nachname = null;
	private String telefon = null;
	private String mail = null;

	// Konstruktor
	
	public Interessent(String vorname, String nachname, String telefon, String mail) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.telefon = telefon;
		this.mail = mail;
	}

	public Interessent(int mitgliederid, String vorname, String nachname, String telefon, String mail) {
		this.mitgliederid = mitgliederid;
		try {
			this.mitgliederid = Interessent.getMitgliederID(vorname, nachname, telefon, mail);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Interessent neuerInteressent = new Interessent(vorname, nachname, telefon, mail);
			this.mitgliederid = neuerInteressent.getMitgliederID();
		}
		this.vorname = vorname;
		this.nachname = nachname;
		this.telefon = telefon;
		this.mail = mail;
	}

	// Setter & Getter

	public int getMitgliederID() {
		return mitgliederid;
	}
	
	public static int getMitgliederID(String vorname, String nachname, String telefon, String mail) {
		MitgliederDAO mitgliederDAO = new MitgliederDAO();
		Interessent interessentO = mitgliederDAO.findByVornameNachnameTelefonMail(vorname, nachname, telefon, mail);
		if (interessentO != null) {
			return interessentO.getMitgliederID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + vorname + "\n" + nachname + "\n" + telefon + "\n" + mail + "\n");
			// TODO: IDNotFoundException erstellen
		}
		
	}
	
	public void setMitgliederID(int mitgliederid) {
		this.mitgliederid = mitgliederid;
	}
	
	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	// Override

	@Override
	public String toString() {
		return "\nName: " + vorname + " " + nachname + "\nTelefon: " + telefon + "\nMail: " + mail + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(vorname, nachname, telefon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interessent other = (Interessent) obj;
		return Objects.equals(other.vorname, vorname) && Objects.equals(other.nachname, nachname)
				&& Objects.equals(other.telefon, telefon);
	}

}
