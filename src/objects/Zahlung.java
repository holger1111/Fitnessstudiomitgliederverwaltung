package objects;

import java.util.Objects;
import daos.ZahlungDAO;

public class Zahlung {

	// Attribute
	
	private int zahlungID;
	private String zahlungsart;
	
	// Konstruktor
	
	public Zahlung(String zahlungsart) {
		this.zahlungsart = zahlungsart;
	}
	
	public Zahlung(int zahlungID, String zahlungsart) {
		this.zahlungID = zahlungID;
		try {
			this.zahlungID = Zahlung.getZahlungID(zahlungsart);
		} catch (IDNotFoundException e) {	// TODO: IDNotFoundException erstellen
			Zahlung neueZahlung = new Zahlung(zahlungsart);
			this.zahlungID = neueZahlung.getZahlungID();
		}
		this.zahlungsart = zahlungsart;
	}
	
	// Setter & Getter
	
	public int getZahlungID() {
		return zahlungID;
	}
	
	public static int getZahlungID(String zahlungsart) {
		ZahlungDAO zahlungDAO = new ZahlungDAO();
		Zahlung zahlungO = zahlungDAO.findByZahlungsart(zahlungsart);
		if (zahlungO != null) {
			return zahlungO.getZahlungID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + zahlungsart + "\n");
			// TODO: IDNotFoundException erstellen			
		}
	}

	public void setZahlungID(int zahlungID) {
		this.zahlungID = zahlungID;
	}

	public String getZahlungsart() {
		return zahlungsart;
	}

	public void setZahlungsart(String zahlungsart) {
		this.zahlungsart = zahlungsart;
	}
	
	// Override
	
	@Override
	public String toString() {
		return "ZahlungID: " + zahlungID + "\nZahlungsart: " + zahlungsart + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(zahlungID, zahlungsart);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Zahlung other = (Zahlung) obj;
		return zahlungID == other.zahlungID && Objects.equals(zahlungsart, other.zahlungsart);
	}
	
	
	
	
}
