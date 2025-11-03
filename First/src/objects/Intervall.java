package objects;

import java.util.Objects;

import daos.IntervallDAO;

public class Intervall {
	
	// Attribute
	
	private int intervallID;
	private String zahlungsintervall;
	private String bezeichnung;
	
	// Konstruktor
	
	public Intervall(String zahlungsintervall, String bezeichnung) {
		this.zahlungsintervall = zahlungsintervall;
		this.bezeichnung = bezeichnung;
	}
	
	public Intervall(int intervallID, String zahlungsintervall, String bezeichnung) {
		this.intervallID = intervallID;
		try {
			this.intervallID = Intervall.getIntervallID(zahlungsintervall, bezeichnung);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Intervall neuesIntervall = new Intervall(zahlungsintervall, bezeichnung);
			this.intervallID = neuesIntervall.getIntervallID();
		}
		this.zahlungsintervall = zahlungsintervall;
		this.bezeichnung = bezeichnung;
	}
	
	// Setter & Getter

	public int getIntervallID() {
		return intervallID;
	}
	
	public static int getIntervallID(String zahlungsintervall, String bezeichnung) {
		IntervallDAO intervallDAO = new IntervallDAO();
		Intervall intervallO = intervallDAO.findByZahlungsintervallBezeichnung(zahlungsintervall, bezeichnung);
		if (intervallO != null) {
			return intervallO.getIntervallID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + zahlungsintervall + "\n" + bezeichnung + "\n");
			// TODO: IDNotFoundException erstellen
		}
		
	}

	public void setIntervallID(int intervallID) {
		this.intervallID = intervallID;
	}

	public String getZahlungsintervall() {
		return zahlungsintervall;
	}

	public void setZahlungsintervall(String zahlungsintervall) {
		this.zahlungsintervall = zahlungsintervall;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	// Override
	
	@Override
	public String toString() {
		return "Intervall [intervallID=" + intervallID + ", zahlungsintervall=" + zahlungsintervall + ", bezeichnung="
				+ bezeichnung + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bezeichnung, intervallID, zahlungsintervall);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intervall other = (Intervall) obj;
		return Objects.equals(bezeichnung, other.bezeichnung) && intervallID == other.intervallID
				&& Objects.equals(zahlungsintervall, other.zahlungsintervall);
	}
	
	

}
