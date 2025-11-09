package Backend.Objekte;

public class Benutzer {
    private int benutzerID;
    private String benutzername;
    private String passwort;
    private int rolleID;       // Fremdschlüssel zu Rolle
    private Integer mitarbeiterID; // Optional: Verbindung zu Mitarbeiter

    public Benutzer() {}

    public Benutzer(int benutzerID, String benutzername, String passwort, int rolleID, Integer mitarbeiterID) {
        this.benutzerID = benutzerID;
        this.benutzername = benutzername;
        this.passwort = passwort;
        this.rolleID = rolleID;
        this.mitarbeiterID = mitarbeiterID;
    }

    public int getBenutzerID() { return benutzerID; }
    public void setBenutzerID(int benutzerID) { this.benutzerID = benutzerID; }

    public String getBenutzername() { return benutzername; }
    public void setBenutzername(String benutzername) { this.benutzername = benutzername; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public int getRolleID() { return rolleID; }
    public void setRolleID(int rolleID) { this.rolleID = rolleID; }

    public Integer getMitarbeiterID() { return mitarbeiterID; }
    public void setMitarbeiterID(Integer mitarbeiterID) { this.mitarbeiterID = mitarbeiterID; }
}
