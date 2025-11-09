package Backend.Objekte;

import java.util.Date;

public class Mitarbeiter {
    private int mitarbeiterID;
    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private boolean aktiv;
    private String strasse;
    private String hausnr;
    private int ortID;
    private int zahlungsdatenID;
    private String telefon;
    private String mail;
    private Integer benutzerID;

    public Mitarbeiter() {}

    public Mitarbeiter(int mitarbeiterID, String vorname, String nachname, Date geburtsdatum, boolean aktiv,
                       String strasse, String hausnr, int ortID, int zahlungsdatenID,
                       String telefon, String mail, Integer benutzerID) {
        this.mitarbeiterID = mitarbeiterID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.aktiv = aktiv;
        this.strasse = strasse;
        this.hausnr = hausnr;
        this.ortID = ortID;
        this.zahlungsdatenID = zahlungsdatenID;
        this.telefon = telefon;
        this.mail = mail;
        this.benutzerID = benutzerID;
    }

    public int getMitarbeiterID() { return mitarbeiterID; }
    public void setMitarbeiterID(int mitarbeiterID) { this.mitarbeiterID = mitarbeiterID; }
    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }
    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
    public Date getGeburtsdatum() { return geburtsdatum; }
    public void setGeburtsdatum(Date geburtsdatum) { this.geburtsdatum = geburtsdatum; }
    public boolean isAktiv() { return aktiv; }
    public void setAktiv(boolean aktiv) { this.aktiv = aktiv; }
    public String getStrasse() { return strasse; }
    public void setStrasse(String strasse) { this.strasse = strasse; }
    public String getHausnr() { return hausnr; }
    public void setHausnr(String hausnr) { this.hausnr = hausnr; }
    public int getOrtID() { return ortID; }
    public void setOrtID(int ortID) { this.ortID = ortID; }
    public int getZahlungsdatenID() { return zahlungsdatenID; }
    public void setZahlungsdatenID(int zahlungsdatenID) { this.zahlungsdatenID = zahlungsdatenID; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public Integer getBenutzerID() { return benutzerID; }
    public void setBenutzerID(Integer benutzerID) { this.benutzerID = benutzerID; }
}
