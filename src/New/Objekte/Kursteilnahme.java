package New.Objekte;

import java.sql.Timestamp;

public class Kursteilnahme {
    private int mitgliederID;
    private int kursterminID;
    private boolean anmeldbar;
    private Timestamp anmeldezeit;
    private boolean aktiv;

    // Konstruktoren
    public Kursteilnahme() {
    }

    public Kursteilnahme(int mitgliederID, int kursterminID, boolean anmeldbar, 
                         Timestamp anmeldezeit, boolean aktiv) {
        this.mitgliederID = mitgliederID;
        this.kursterminID = kursterminID;
        this.anmeldbar = anmeldbar;
        this.anmeldezeit = anmeldezeit;
        this.aktiv = aktiv;
    }

    // Getter und Setter
    public int getMitgliederID() {
        return mitgliederID;
    }

    public void setMitgliederID(int mitgliederID) {
        this.mitgliederID = mitgliederID;
    }

    public int getKursterminID() {
        return kursterminID;
    }

    public void setKursterminID(int kursterminID) {
        this.kursterminID = kursterminID;
    }

    public boolean isAnmeldbar() {
        return anmeldbar;
    }

    public void setAnmeldbar(boolean anmeldbar) {
        this.anmeldbar = anmeldbar;
    }

    public Timestamp getAnmeldezeit() {
        return anmeldezeit;
    }

    public void setAnmeldezeit(Timestamp anmeldezeit) {
        this.anmeldezeit = anmeldezeit;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    @Override
    public String toString() {
        return "Kursteilnahme{" +
                "mitgliederID=" + mitgliederID +
                ", kursterminID=" + kursterminID +
                ", anmeldbar=" + anmeldbar +
                ", anmeldezeit=" + anmeldezeit +
                ", aktiv=" + aktiv +
                '}';
    }
}
