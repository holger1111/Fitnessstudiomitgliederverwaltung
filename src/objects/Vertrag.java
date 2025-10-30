package objects;

public class Vertrag {

	// Attribute
	
	private int vertragid;
	private String bezeichnung;
	private int laufzeit;
	private double grundpreis;
	
	// Konstruktor
	
	public Vertrag() {
		
	}
		
	
	
	
	// Setter & Getter

	public int getVertragid() {
		return vertragid;
	}

	public void setVertragid(int vertragid) {
		this.vertragid = vertragid;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public int getLaufzeit() {
		return laufzeit;
	}

	public void setLaufzeit(int laufzeit) {
		this.laufzeit = laufzeit;
	}

	public double getGrundpreis() {
		return grundpreis;
	}

	public void setGrundpreis(double grundpreis) {
		this.grundpreis = grundpreis;
	}
	
	
	
	
}
