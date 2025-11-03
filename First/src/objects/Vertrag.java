package objects;

import java.util.Objects;

import daos.VertragDAO;

public class Vertrag {

	// Attribute
	
	private int vertragID;
	private String bezeichnung;
	private int laufzeit;
	private double grundpreis;
	
	// Konstruktor
	
	public Vertrag(String bezeichnung, int laufzeit, double grundpreis) {
		this.bezeichnung = bezeichnung;
		this.laufzeit = laufzeit;
		this.grundpreis = grundpreis;
	}

	public Vertrag(int vertragID, String bezeichnung, int laufzeit, double grundpreis) {
		this.vertragID = vertragID;
		try {
			this.vertragID = Vertrag.getVertragID(bezeichnung, laufzeit);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Vertrag neuerVertrag = new Vertrag(bezeichnung, laufzeit, grundpreis);
			this.vertragID = neuerVertrag.getVertragID();
		}
		this.bezeichnung = bezeichnung;
		this.laufzeit = laufzeit;
		this.grundpreis = grundpreis;
	}
	
	// Setter & Getter

	public int getVertragID() {
		return vertragID;
	}
	
	public static int getVertragID(String bezeichnung, int laufzeit) {
		VertragDAO vertragDAO = new VertragDAO();
		Vertrag vertragO = vertragDAO.findByBezeichnungLaufzeit(bezeichnung, laufzeit);
		if (vertragO != null) {
			return vertragO.getVertragID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + bezeichnung + "\n" + laufzeit + "\n");
			// TODO: IDNotFoundException erstellen
		}
		
	}

	public void setVertragID(int vertragid) {
		this.vertragID = vertragid;
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

	// Override

	@Override
	public String toString() {
		return "VertragID: " + vertragID + "\nBezeichnung: " + bezeichnung + "\nLaufzeit: " + laufzeit
				+ "\nGrundpreis: " + grundpreis + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bezeichnung, grundpreis, laufzeit, vertragID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertrag other = (Vertrag) obj;
		return Objects.equals(bezeichnung, other.bezeichnung)
				&& Double.doubleToLongBits(grundpreis) == Double.doubleToLongBits(other.grundpreis)
				&& laufzeit == other.laufzeit && vertragID == other.vertragID;
	}
	
	
	
	
}
