package objects;

import java.sql.Timestamp;
import java.util.Objects;

import daos.BestellungDAO;

public class Bestellung {
	
	// Attribute
	
	private int bestellungID;
	private int mitgliederID;
	private double gesamtpreis;
	private Timestamp bestelldatum = new Timestamp(System.currentTimeMillis());
	private int zahlungID;
	
	// Konstruktor
	
	public Bestellung(int mitgliederID, int zahlungID) {
		this.mitgliederID = mitgliederID;
		this.zahlungID = zahlungID;
	}
	
	public Bestellung(int bestellungID, int mitgliederID, int zahlungID) {
		this.bestellungID = bestellungID;
		try {
			this.bestellungID = Bestellung.getBestellungID(mitgliederID, zahlungID);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Bestellung neueBestellung = new Bestellung(mitgliederID, zahlungID);
			this.bestellungID = neueBestellung.getBestellungID();
		}
		
		this.mitgliederID = mitgliederID;
//		this.gesamtpreis;	// TODO: Soll direkt den Gesamtpreis von DB holen oder von ArtikelBestellung errechnen

		this.bestelldatum = new Timestamp(System.currentTimeMillis());
		this.zahlungID = zahlungID;
	}
	
	public Bestellung(int bestellungID, int mitgliederID, double gesamtpreis, Timestamp bestelldatum, int zahlungID) {
		this.bestellungID = bestellungID;
		try {
			this.bestellungID = Bestellung.getBestellungID(mitgliederID, zahlungID);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Bestellung neueBestellung = new Bestellung(mitgliederID, zahlungID);
			this.bestellungID = neueBestellung.getBestellungID();
		}
		this.mitgliederID = mitgliederID;
		this.gesamtpreis = gesamtpreis;
		this.bestelldatum = new Timestamp(System.currentTimeMillis());
		this.zahlungID = zahlungID;
	}
	
	// Setter & Getter
	
	public int getBestellungID() {
		return bestellungID;
	}
	
	public static int getBestellungID(int mitgliederID, int zahlungID) {
		BestellungDAO bestellungDAO = new BestellungDAO();
		Bestellung bestellungO = bestellungDAO.findByMitgliederIDZahlungID(mitgliederID, zahlungID);
		if (bestellungO != null) {
			return bestellungO.getMitgliederID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + mitgliederID + "\n" + zahlungID + "\n");
			// TODO: IDNotFoundException erstellen
		}
		
	}

	public void setBestellungID(int bestellungID) {
		this.bestellungID = bestellungID;
	}

	public int getMitgliederID() {
		return mitgliederID;
	}

	public void setMitgliederID(int mitgliederID) {
		this.mitgliederID = mitgliederID;
	}

	public double getGesamtpreis() {
		return gesamtpreis;
	}

	public void setGesamtpreis(double gesamtpreis) {
		this.gesamtpreis = gesamtpreis;
	}

	public Timestamp getBestelldatum() {
		return bestelldatum;
	}

	public void setBestelldatum(Timestamp bestelldatum) {
		this.bestelldatum = bestelldatum;
	}

	public int getZahlungID() {
		return zahlungID;
	}

	public void setZahlungID(int zahlungID) {
		this.zahlungID = zahlungID;
	}
	
	// Override
	
	@Override
	public String toString() {
		return "BestellungID: " + bestellungID + "\nMitgliederID: " + mitgliederID + "\nGesamtpreis: "
				+ gesamtpreis + "\nBestelldatum: " + bestelldatum + "\nZahlungID: " + zahlungID + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bestelldatum, bestellungID, gesamtpreis, mitgliederID, zahlungID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bestellung other = (Bestellung) obj;
		return Objects.equals(bestelldatum, other.bestelldatum) && bestellungID == other.bestellungID
				&& Double.doubleToLongBits(gesamtpreis) == Double.doubleToLongBits(other.gesamtpreis)
				&& mitgliederID == other.mitgliederID && zahlungID == other.zahlungID;
	}
	
	
	
}
