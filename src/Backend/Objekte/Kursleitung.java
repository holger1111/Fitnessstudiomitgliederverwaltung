package Backend.Objekte;

import java.sql.Timestamp;

public class Kursleitung {
    // Zusammengesetzter Primärschlüssel
    private int kursterminID;
    private int mitarbeiterID;

    // Weitere Felder
    private boolean bestätigt;
    private Timestamp bestätigungszeit;
    private boolean abgemeldet;
    private Timestamp abmeldezeit;
    private String kommentar;

    // Konstruktoren
    public Kursleitung() {}

    public Kursleitung(int kursterminID, int mitarbeiterID) {
        this.kursterminID = kursterminID;
        this.mitarbeiterID = mitarbeiterID;
    }

    // Getter und Setter
    public int getKursterminID() { return kursterminID; }
    public void setKursterminID(int kursterminID) { this.kursterminID = kursterminID; }

    public int getMitarbeiterID() { return mitarbeiterID; }
    public void setMitarbeiterID(int mitarbeiterID) { this.mitarbeiterID = mitarbeiterID; }

    public boolean isBestätigt() { return bestätigt; }
    public void setBestätigt(boolean bestätigt) { this.bestätigt = bestätigt; }

    public Timestamp getBestätigungszeit() { return bestätigungszeit; }
    public void setBestätigungszeit(Timestamp bestätigungszeit) { this.bestätigungszeit = bestätigungszeit; }

    public boolean isAbgemeldet() { return abgemeldet; }
    public void setAbgemeldet(boolean abgemeldet) { this.abgemeldet = abgemeldet; }

    public Timestamp getAbmeldezeit() { return abmeldezeit; }
    public void setAbmeldezeit(Timestamp abmeldezeit) { this.abmeldezeit = abmeldezeit; }

    public String getKommentar() { return kommentar; }
    public void setKommentar(String kommentar) { this.kommentar = kommentar; }
}
