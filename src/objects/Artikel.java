package objects;

import java.util.Objects;

import daos.ArtikelDAO;

public class Artikel {

	// Attribute

	private int artikelID;
	private String name;
	private double einzelpreis;
	private String kommentar;

	// Konstruktor

	public Artikel(String name, double einzelpreis) {
		this.name = name;
		this.einzelpreis = einzelpreis;
	}

	public Artikel(String name, double einzelpreis, String kommentar) {
		this.name = name;
		this.einzelpreis = einzelpreis;
		this.kommentar = kommentar;
	}
	
	public Artikel(int artikelID, String name, double einzelpreis, String kommentar) {
		this.artikelID = artikelID;
		try {
			this.artikelID = Artikel.getArtikelID(name, einzelpreis);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Artikel neuerArtikel = new Artikel(name, einzelpreis);
			this.artikelID = neuerArtikel.getArtikelID();
		}
		this.name = name;
		this.einzelpreis = einzelpreis;
		this.kommentar = kommentar;
	}
	
	// Setter & Getter
	
	public int getArtikelID() {
		return artikelID;
	}
	
	public static int getArtikelID(String name, double einzelpreis) {
		ArtikelDAO artikelDAO = new ArtikelDAO();
		Artikel artikelO = artikelDAO.findByNameEinzelpreis(name, einzelpreis);
		if (artikelO != null) {
			return artikelO.getArtikelID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + name + "\n" + einzelpreis + "\n");
			// TODO: IDNotFoundException erstellen
		}
		
	}

	public void setArtikelID(int artikelID) {
		this.artikelID = artikelID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getEinzelpreis() {
		return einzelpreis;
	}
		
	public void setEinzelpreis(double einzelpreis) {
		this.einzelpreis = einzelpreis;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}
	
	// Override
	
	@Override
	public String toString() {
		return "ArtikelID: " + artikelID + "\nName: " + name + "\nEinzelpreis: " + einzelpreis + "\nKommentar: "
				+ kommentar + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(artikelID, einzelpreis, kommentar, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artikel other = (Artikel) obj;
		return artikelID == other.artikelID
				&& Double.doubleToLongBits(einzelpreis) == Double.doubleToLongBits(other.einzelpreis)
				&& Objects.equals(kommentar, other.kommentar) && Objects.equals(name, other.name);
	}

}
