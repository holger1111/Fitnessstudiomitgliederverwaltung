package objects;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;

import helper.Datum;

public class Mitglieder extends Interessent {
	
//	private Ort ort;
//	private Zahlungsdaten zahlungsdaten;
	
	// Attribute

	private final LocalDate geburtsdatum;
	private boolean aktiv = false;
	private String strasse;
	private String hausnr;
	private int ortid;
	private int zahlungsdatenid;

	// Konstruktor
	
	public Mitglieder(int mitgliederid, String vorname, String nachname, LocalDate geburtsdatum, boolean aktiv, String strasse, String hausnr, int ortid, int zahlungsdatenid, String telefon, String mail) {
		super(mitgliederid, vorname, nachname, telefon, mail);
		this.geburtsdatum = geburtsdatum;
		this.aktiv = aktiv;
		this.strasse = strasse;
		this.hausnr = hausnr;
		this.ortid = ortid;
		this.zahlungsdatenid = zahlungsdatenid;
	}
	
	public Mitglieder(int mitgliederid, String vorname, String nachname, int tag, int monat, int jahr, String strasse, String hausnr, String PLZ, String ort, String name, String IBAN, String BIC, String telefon, String mail) {
		super(mitgliederid, vorname, nachname, telefon, mail);
		if (Datum.testeDatum(tag, monat, jahr)) {
			this.geburtsdatum = LocalDate.of(jahr, monat, tag);
		} else {
				throw new DateTimeException("Ungültiges Datum");
		}
		this.aktiv = true;
		this.strasse = strasse;
		this.hausnr = hausnr;
		try {
			this.ortid = Ort.getOrtID(PLZ, ort);
		} catch (PlaceNotFoundException e) {	// TODO: PlaceNotFoundException erstellen
			Ort neuerOrt = new Ort(PLZ, ort);
			this.ortid = neuerOrt.getOrtID();
		}
		try {
			this.zahlungsdatenid = Zahlungsdaten.getZahlungsdatenID(name, IBAN, BIC);
		} catch (PaymentDetailsException e) {	// TODO: PaymentDetailsException erstellen
			Zahlungsdaten neueZahlungsdaten = new Zahlungsdaten(name, IBAN, BIC);
			this.zahlungsdatenid = neueZahlungsdaten.getZahlungsdatenID();
		}
			
	}
	// Setter & Getter
	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	public LocalDate getGeburtsdatum() {
		return geburtsdatum;
	}
	
	public boolean isAktiv() {
		return aktiv;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnr() {
		return hausnr;
	}

	public void setHausnr(String hausnr) {
		this.hausnr = hausnr;
	}

	public int getOrtID() {
		return ortid;
	}

	public void setOrtID(int ortid) {
		this.ortid = ortid;
	}
	
	public int getZahlungsdatenID() {
		return zahlungsdatenid;
	}

	public void setZahlungsdatenID(int zahlungsdatenid) {
		this.zahlungsdatenid = zahlungsdatenid;
	}

	// Override

//	@Override
//	public String toString() {
//		return "\nName: " + vorname + " " + nachname + "\nTelefon: " + telefon + "\n";
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(vorname, nachname, telefon);
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Mitglieder other = (Mitglieder) obj;
//		return Objects.equals(other.vorname, vorname) && Objects.equals(other.nachname, nachname)
//				&& Objects.equals(other.telefon, telefon);
//	}

}
