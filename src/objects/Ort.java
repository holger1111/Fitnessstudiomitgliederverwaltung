package objects;

import daos.OrtDAO;

public class Ort {

	// Attribute
	
	private int ortid;
	private String PLZ;
	private String ort;
	
	// Konstruktor
	
	public Ort(String PLZ, String ort) {
		this.PLZ = PLZ;
		this.ort = ort;
	}
	
	public Ort(int ortid, String PLZ, String ort) {
		this.ortid = ortid;
		this.PLZ = PLZ;
		this.ort = ort;
	}
	
	// Setter & Getter

	public int getOrtID() {
		return ortid;
	}

	public static int getOrtID(String PLZ, String ort) {
		OrtDAO ortDAO = new OrtDAO();
		Ort ortO = ortDAO.findByPLZOrt(PLZ, ort);
		if (ortO != null) {
			return ortO.getOrtID();
		} else {
			throw new PlaceNotFoundException("Ort nicht gefunden: " + PLZ + " " + ort);
			// TODO: PlaceNotFoundException erstellen
		}
	}

	public void setOrtID(int ortid) {
		this.ortid = ortid;
	}

	public String getPLZ() {
		return PLZ;
	}

	public void setPLZ(String pLZ) {
		PLZ = pLZ;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
