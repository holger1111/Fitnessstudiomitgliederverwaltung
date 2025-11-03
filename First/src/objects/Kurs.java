package objects;

import daos.KursDAO;

import java.util.Objects;

public class Kurs {

	// Attribute

	private int kursID;
	private String bezeichnung;
	private boolean kostenfrei;
	private boolean aktiv;
	private int teilnehmerzahl;
	private double preis;
	private int anzahltermine;
	private String kommentar = "";

	// Konstruktor
	
	public Kurs(String bezeichnung, int teilnehmerzahl, double preis, int anzahltermine) {
		this.bezeichnung = bezeichnung;
		this.teilnehmerzahl = teilnehmerzahl;
		this.preis = preis;
		this.anzahltermine = anzahltermine;
	}
	
	public Kurs(String bezeichnung, boolean kostenfrei, boolean aktiv, int teilnehmerzahl, double preis) {
		this.bezeichnung = bezeichnung;
		this.kostenfrei = kostenfrei;
		this.aktiv = aktiv;
		this.teilnehmerzahl = teilnehmerzahl;
		this.preis = preis;
	}
	
	public Kurs(String bezeichnung, boolean kostenfrei, boolean aktiv, int teilnehmerzahl, double preis, int anzahltermine,
			String kommentar) {
		this.bezeichnung = bezeichnung;
		this.kostenfrei = kostenfrei;
		this.aktiv = aktiv;
		this.teilnehmerzahl = teilnehmerzahl;
		this.preis = preis;
		this.kommentar = kommentar;
	}
	
	public Kurs(int kursID, String bezeichnung, boolean kostenfrei, boolean aktiv, int teilnehmerzahl, double preis,
			int anzahltermine, String kommentar) {
		this.kursID = kursID;
		try {
			this.kursID = Kurs.getKursID(bezeichnung, kostenfrei, aktiv, teilnehmerzahl, preis, anzahltermine, kommentar);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Kurs neuerKurs = new Kurs(bezeichnung, kostenfrei, aktiv, teilnehmerzahl, preis, anzahltermine, kommentar);
			this.kursID = neuerKurs.getKursID();
		}
		this.bezeichnung = bezeichnung;
		this.kostenfrei = kostenfrei;
		this.aktiv = aktiv;
		this.teilnehmerzahl = teilnehmerzahl;
		this.preis = preis;
		this.anzahltermine = anzahltermine;
		this.kommentar = kommentar;
	}
	
	// Setter & Getter
	
	public int getKursID() {
		return kursID;
	}
	
	public static int getKursID(String bezeichnung, boolean kostenfrei, boolean aktiv, int teilnehmerzahl, double preis, int anzahltermine, String kommentar) {
		KursDAO kursDAO = new KursDAO();
		Kurs kursO = kursDAO.findByBezeichnungTeilnehmerzahlPreisAnzahlTermine(bezeichnung, teilnehmerzahl, preis, anzahltermine);
		if (kursO != null) {
			return kursO.getKursID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + bezeichnung + "\n" + kostenfrei + "\n" + aktiv + "\n" + teilnehmerzahl + "\n" + preis + "\n" + kommentar + "\n");
			// TODO: IDNotFoundException erstellen
		}
		
	}

	public void setKursID(int kursID) {
		this.kursID = kursID;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public boolean isKostenfrei() {
		return kostenfrei;
	}

	public void setKostenfrei(boolean kostenfrei) {
		this.kostenfrei = kostenfrei;
	}

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	public int getTeilnehmerzahl() {
		return teilnehmerzahl;
	}

	public void setTeilnehmerzahl(int teilnehmerzahl) {
		this.teilnehmerzahl = teilnehmerzahl;
	}

	public double getPreis() {
		return preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
	}
	
	public int getAnzahlTermine() {
		return anzahltermine;
	}

	public void setAnzahlTermine(int anzahltermine) {
		this.anzahltermine = anzahltermine;
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
		return "KursID: " + kursID + "\nBezeichnung: " + bezeichnung + "\nKostenfrei: " + kostenfrei + "\nAktiv: "
				+ aktiv + "\nTeilnehmerzahl: " + teilnehmerzahl + "\nPreis: " + preis + "\nKommentar: " + kommentar + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(aktiv, bezeichnung, kommentar, kostenfrei, kursID, preis, teilnehmerzahl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kurs other = (Kurs) obj;
		return aktiv == other.aktiv && Objects.equals(bezeichnung, other.bezeichnung)
				&& Objects.equals(kommentar, other.kommentar) && kostenfrei == other.kostenfrei
				&& kursID == other.kursID && Double.doubleToLongBits(preis) == Double.doubleToLongBits(other.preis)
				&& teilnehmerzahl == other.teilnehmerzahl;
	}
	
		
}
