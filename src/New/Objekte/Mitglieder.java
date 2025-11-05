package New.Objekte;

import java.util.Date;
import java.util.Objects;

import New.Helper.Datum;
import New.Helper.DatumHelper;

public class Mitglieder extends Interessenten {

	// Attribute

	private Date geburtstag;
	private boolean aktiv;
	private String strasse;
	private String hausnr;
	private Ort ort;
	private int ortID;
	private Zahlungsdaten zahlungsdaten;
	private int zahlungsdatenID;
	private String mail;

	// Konstruktor

	public Mitglieder() {
		super();
	}

	public Mitglieder(String vorname, String nachname, String telefon, Date geburtstag, boolean aktiv, String strasse,
			String hausnr, int ortID, int zahlungsdatenID, String mail) {
		super(vorname, nachname, telefon);
		this.geburtstag = geburtstag;
		this.aktiv = aktiv;
		this.strasse = strasse;
		this.hausnr = hausnr;
		this.ortID = ortID;
		this.zahlungsdatenID = zahlungsdatenID;
		this.mail = mail;
	}

	// Neuer Konstruktor, der direkt das Zahlungsdaten-Objekt akzeptiert
	public Mitglieder(String vorname, String nachname, String telefon, Date geburtstag, boolean aktiv, String strasse,
			String hausnr, Ort ort, Zahlungsdaten zahlungsdaten, String mail) {
		super(vorname, nachname, telefon);
		this.geburtstag = geburtstag;
		this.aktiv = aktiv;
		this.strasse = strasse;
		this.hausnr = hausnr;
		this.ort = ort;
		this.zahlungsdaten = zahlungsdaten;
		this.ortID = ort != null ? ort.getOrtID() : 0;
		this.zahlungsdatenID = zahlungsdaten != null ? zahlungsdaten.getZahlungsdatenID() : 0;
		this.mail = mail;
	}

	public Mitglieder(int mitgliederID, String vorname, String nachname, String telefon, Date geburtstag, boolean aktiv,
			String strasse, String hausnr, Ort ort, Zahlungsdaten zahlungsdaten, String mail) {
		super(mitgliederID, vorname, nachname, telefon);
		this.geburtstag = geburtstag;
		this.aktiv = aktiv;
		this.strasse = strasse;
		this.hausnr = hausnr;
		this.ort = ort;
		this.zahlungsdaten = zahlungsdaten;
		this.ortID = ort != null ? ort.getOrtID() : 0;
		this.zahlungsdatenID = zahlungsdaten != null ? zahlungsdaten.getZahlungsdatenID() : 0;
		this.mail = mail;
	}

	// Setter & Getter

	public Date getGeburtstag() {
		return geburtstag;
	}

	public void setGeburtstag(Date geburtstag) {
		this.geburtstag = geburtstag;
	}

	public int berechneAlter() {
		if (geburtstag == null) return 0;
		Datum heute = DatumHelper.getAktuellesDatum();
		Datum geburt = new Datum(getGeburtstag());

		int alter = heute.getJahr() - geburt.getJahr();

		// Prüfe, ob aktuelles Datum vor dem Geburtstag in diesem Jahr liegt
		Datum geburtAktuellesJahr = new Datum(heute.getJahr(), geburt.getMonat(), geburt.getTag());
		if (heute.isBefore(geburtAktuellesJahr)) {
			alter--;
		}
		return alter;
	}

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
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

	public String getPLZ() {
		return ort != null ? ort.getPLZ() : null;
	}

	public Ort getOrt() {
		return ort;
	}

	public void setOrt(Ort ort) {
		this.ort = ort;
		this.ortID = ort != null ? ort.getOrtID() : 0;
	}

	public int getOrtID() {
		return ortID;
	}

	public void setOrtID(int ortID) {
		this.ortID = ortID;
	}

	public Zahlungsdaten getZahlungsdaten() {
		return zahlungsdaten;
	}

	public void setZahlungsdaten(Zahlungsdaten zahlungsdaten) {
		this.zahlungsdaten = zahlungsdaten;
		this.zahlungsdatenID = zahlungsdaten != null ? zahlungsdaten.getZahlungsdatenID() : 0;
	}

	public int getZahlungsdatenID() {
		return zahlungsdatenID;
	}

	public void setZahlungsdatenID(int zahlungsdatenID) {
		this.zahlungsdatenID = zahlungsdatenID;
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
		String geburtsStr = geburtstag != null ? new java.text.SimpleDateFormat("dd.MM.yyyy").format(geburtstag) : "";
	    String strasseStr = strasse != null ? strasse : "";
	    String hausnrStr = hausnr != null ? hausnr : "";
	    String ortIDStr = (ort != null && ort.getOrtID() != 0) ? String.valueOf(ort.getOrtID()) : "";
	    String plzStr = ort != null ? ort.getPLZ() : "";
	    String ortStr = ort != null ? ort.getOrt() : "";
	    String zahlungsdatenIDstr = zahlungsdaten != null ? String.valueOf(zahlungsdaten.getZahlungsdatenID()) : "";
	    String mailStr = mail != null ? mail : "";
		return String.format(
				"Mitglied:\nMitgliederID: \t%d\nName: \t\t%s %s\nGeburtstag: \t%s\nAlter: \t\t%d\nAktiv: \t\t%b"
						+ "\nAddresse:\n\t\t%s %s\n\t\t%s %s\nTelefon: \t%s\nMail: \t\t%s\n",
				getMitgliederID(), getVorname(), getNachname(),
				geburtstag != null ? new java.text.SimpleDateFormat("dd.MM.yyyy").format(geburtstag) : 0,
						geburtstag != null ? berechneAlter() : 0, aktiv, strasse, hausnr, ort != null ? ort.getPLZ() : "",
				ort != null ? ort.getOrt() : "", getTelefon(), mail);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getMitgliederID());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Mitglieder))
			return false;
		Mitglieder other = (Mitglieder) obj;
		return getMitgliederID() == other.getMitgliederID();
	}
}
