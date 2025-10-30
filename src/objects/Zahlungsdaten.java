package objects;

import daos.ZahlungsdatenDAO;

public class Zahlungsdaten {

	// Attribute
	
	private int zahlungsdatenID;
	private String name;
	private String IBAN;
	private String BIC;
	
	// Konstruktor
	
	public Zahlungsdaten(String name, String IBAN, String BIC) {
		this.name = name;
		this.IBAN = IBAN;
		this.BIC = BIC;
	}
	
	public Zahlungsdaten(int zahlungsdatenID, String name, String IBAN, String BIC) {
		this.zahlungsdatenID = zahlungsdatenID;
		this.name = name;
		this.IBAN = IBAN;
		this.BIC = BIC;
	}

	// Setter & Getter
	
	public int getZahlungsdatenID() {
		return zahlungsdatenID;
	}
	
	public static int getZahlungsdatenID(String name, String IBAN, String BIC) {
		ZahlungsdatenDAO zahlungsdatenDAO = new ZahlungsdatenDAO();
		Zahlungsdaten zahlungsdatenO = zahlungsdatenDAO.findByNameIBANBIC(name, IBAN, BIC);
		if (zahlungsdatenO != null) {
			return zahlungsdatenO.getZahlungsdatenID();
		} else {
			throw new PaymentDetailsException("Zahlungsdaten nicht gefunden:\n" + name + "\n" + IBAN + "\n" + BIC + "\n");
			// TODO: PaymentDetailsException erstellen
		}
	}
	
	public void setZahlungsdatenid(int zahlungsdatenID) {
		this.zahlungsdatenID = zahlungsdatenID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public String getBIC() {
		return BIC;
	}

	public void setBIC(String bIC) {
		BIC = bIC;
	}

	
	
	
	
	
	
	
	
	
	
	
}
