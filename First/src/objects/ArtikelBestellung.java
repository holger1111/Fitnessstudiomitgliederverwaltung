package objects;

import java.util.Objects;

public class ArtikelBestellung {
	
	// Attribute
	
	private int bestellungID;
	private int artikelID;
	private int menge;
	private double aufaddiert;
	
	// Konstruktor
	
	public ArtikelBestellung(int bestellungID, int artikelID, int menge) {
		this.bestellungID = bestellungID;
		this.artikelID = artikelID;
		this.menge = menge;
//		this.aufaddiert = ;  // Soll direkt aus DB geholt oder von Artikel * Menge errechnet werden
		}
	

	public ArtikelBestellung(int bestellungID, int artikelID, int menge, double aufaddiert) {
		this.bestellungID = bestellungID;
		this.artikelID = artikelID;
		this.menge = menge;
//		this.aufaddiert = ;  // Soll direkt aus DB geholt oder von Artikel * Menge errechnet werden
		}
	
	// Setter & Getter
	
	public int getBestellungID() {
		return bestellungID;
	}

	public void setBestellungID(int bestellungID) {
		this.bestellungID = bestellungID;
	}

	public int getArtikelID() {
		return artikelID;
	}

	public void setArtikelID(int artikelID) {
		this.artikelID = artikelID;
	}

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public double getAufaddiert() {
		return aufaddiert;
	}

	public void setAufaddiert(double aufaddiert) {
		aufaddiert = aufaddiert;
	}
	
	// Override
	
	@Override
	public String toString() {
		return "BestellungID: " + bestellungID + "\nArtikelID: " + artikelID + "\nMenge: " + menge
				+ "\nAufaddiert: " + aufaddiert + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(aufaddiert, artikelID, bestellungID, menge);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArtikelBestellung other = (ArtikelBestellung) obj;
		return Double.doubleToLongBits(aufaddiert) == Double.doubleToLongBits(other.aufaddiert)
				&& artikelID == other.artikelID && bestellungID == other.bestellungID && menge == other.menge;
	}

	
	
}
