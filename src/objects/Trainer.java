package objects;

import java.util.Objects;

import daos.TrainerDAO;

public class Trainer {
	
	// Attribute
	
	private int trainerID;
	private String vorname;
	private String nachname;
	private String kommentar = "";
	
	// Konstruktor
	
	public Trainer (String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
	}
	
	public Trainer (String vorname, String nachname, String kommentar) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.kommentar = kommentar;
	}

	public Trainer (int trainerID, String vorname, String nachname, String kommentar) {
		this.trainerID = trainerID;
		try {
			this.trainerID = Trainer.getTrainerID(vorname, nachname);
		} catch (IDNotFoundException e)	{		// TODO: IDNotFoundException erstellen
			Trainer neuerTrainer = new Trainer(vorname, nachname);
			this.trainerID = neuerTrainer.getTrainerID();
		}
		this.vorname = vorname;
		this.nachname = nachname;
		this.kommentar = kommentar;
	}
	// Setter & Getter
	
	public int getTrainerID() {
		return trainerID;
	}
	
	public static int getTrainerID(String vorname, String nachname) {
		TrainerDAO trainerDAO = new TrainerDAO();
		Trainer trainerO = trainerDAO.findByVornameNachname(vorname, nachname);
		if (trainerO != null) {
			return trainerO.getTrainerID();
		} else {
			throw new IDNotFoundException("ID nicht gefunden:\n" + vorname + "\n" + nachname + "\n");
			// TODO: IDNotFoundException erstellen
		}
		
	}

	public void setTrainerID(int trainerID) {
		this.trainerID = trainerID;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
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
		return "TrainerID: " + trainerID + "\nVorname: " + vorname + "\nNachname: " + nachname + "\nKommentar: "
				+ kommentar + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(kommentar, nachname, trainerID, vorname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trainer other = (Trainer) obj;
		return Objects.equals(kommentar, other.kommentar) && Objects.equals(nachname, other.nachname)
				&& trainerID == other.trainerID && Objects.equals(vorname, other.vorname);
	}
	
	
	
}
