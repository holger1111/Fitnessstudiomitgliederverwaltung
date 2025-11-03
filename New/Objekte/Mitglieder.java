package Objekte;

import java.util.Date;
import java.util.Objects;

import Helper.Datum;
import Helper.DatumHelper;

public class Mitglieder extends Interessenten {
	
	// Attribute
	
    private Date geburtstag;
    private boolean aktiv;
    private String strasse;
    private String hausnr;
    private Ort ort;
    private int ortID;
    private int zahlungsdatenID;
    private String mail;
    
    // Konstruktor
    
    public Mitglieder() {
        super();
    }

    public Mitglieder(int mitgliederID, String vorname, String nachname, String telefon,
                     Date geburtstag, boolean aktiv, String strasse, String hausnr,
                     int ortID, int zahlungsdatenID, String mail) {
        super(mitgliederID, vorname, nachname, telefon);
        this.geburtstag = geburtstag;
        this.aktiv = aktiv;
        this.strasse = strasse;
        this.hausnr = hausnr;
        this.ortID = ortID;
        this.zahlungsdatenID = zahlungsdatenID;
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
        Datum heute = DatumHelper.getAktuellesDatum();
        Datum geburt = new Datum(getGeburtstag());
        
        int alter = heute.getJahr() - geburt.getJahr();

        if (heute.isBefore(new Datum(geburt.getJahr(), geburt.getMonat(), geburt.getTag()))) {
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
    
    public String getOrt() {
    	return ort!= null ? ort.getName() : null;
    }
    public int getOrtID() {
        return ortID;
    }

    public void setOrtID(int ortID) {
        this.ortID = ortID;
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
        return "Mitglied:\n" +
                "MitgliederID: " + getMitgliederID() +
                "\nVorname: " + getVorname() +
                " " + getNachname() +
                "\nGeburtstag: " + geburtstag +
                "\nAlter: " + 
                "\nAktiv: " + aktiv +
                "\nAddresse:\n'" + strasse +
                " " + hausnr +
                "\n" + getPLZ() + " " + getOrt() +
                "\nTelefon: " + getTelefon() +
                "\nMail: " + mail +
                "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMitgliederID());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mitglieder)) return false;
        Mitglieder other = (Mitglieder) obj;
        return getMitgliederID() == other.getMitgliederID();
    }
}
