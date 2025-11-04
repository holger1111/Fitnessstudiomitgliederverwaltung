package New.Objekte;

import java.util.Objects;

public class Ort {
	
	// Attribute
	
    private int ortID;
    private String plz;
    private String ort;
    
    // Konstruktor
    
    public Ort() {
    }

    public Ort(String plz, String ort) {
        this.plz = plz;
        this.ort = ort;
    }

    public Ort(int ortID, String plz, String ort) {
        this.ortID = ortID;
        this.plz = plz;
        this.ort = ort;
    }

    // Setter & Getter
    
    public int getOrtID() {
        return ortID;
    }

    public void setOrtID(int ortID) {
        this.ortID = ortID;
    }

    public String getPLZ() {
        return plz;
    }

    public void setPLZ(String plz) {
        this.plz = plz;
    }

    public String getName() {
        return ort;
    }

    public void setName(String ort) {
        this.ort = ort;
    }
    
    // Override
    
    @Override
    public String toString() {
        return "Ort:\n" +
                "OrtID: " + ortID +
                "\nPLZ: " + plz +
                "\nOrt: '" + ort +
                "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(ortID, plz, ort);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ort)) return false;
        Ort other = (Ort) obj;
        return ortID == other.ortID &&
               Objects.equals(plz, other.plz) &&
               Objects.equals(ort, other.ort);
    }
}
