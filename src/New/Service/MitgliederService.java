package New.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import New.Objekte.Mitglieder;
import New.Objekte.Ort;
import New.Objekte.Zahlungsdaten;
import New.Validator.BICValidator;
import New.Validator.EMailValidator;
import New.Validator.IBANValidator;
import New.Validator.StringValidator;
import New.DAOs.OrtDAO;
import New.DAOs.ZahlungsdatenDAO;
import New.Exception.StringException;
import New.Helper.IO;
import New.Manager.MitgliederManager;

public class MitgliederService extends BaseService {

	public MitgliederService(Connection connection, Scanner scanner) {
		super(connection, scanner);
	}

	public void start() {
	    boolean zurueck = false;
	    while (!zurueck) {
	        System.out.println("==== Mitgliederverwaltung ====");
	        System.out.println("1 - Suche");
	        System.out.println("2 - Interessenten erstellen");
	        System.out.println("3 - Mitglieder erstellen");
	        System.out.println("4 - Zurück zum Hauptmenü");
	        System.out.print("Bitte wählen: ");
	        String eingabe = scanner.nextLine();

	        switch (eingabe) {
	            case "1":
	                if (mitgliederSuche()) return;
	                break;
	            case "2": interessentErstellen(); break;
	            case "3": mitgliedErstellen(); break;
	            case "4": zurueck = true; break;
	            default: System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
	        }
	    }
	}

	public boolean mitgliederSuche() {
		System.out.print("Bitte Suchbegriff eingeben: ");
		String suchbegriff = scanner.nextLine();
		try {
			MitgliederManager manager = new MitgliederManager();
			List<Mitglieder> ergebnis = manager.search(suchbegriff);
			if (ergebnis.isEmpty()) {
				System.out.println("Keine Mitglieder gefunden.");
			} else {
				System.out.printf("%-8s| %-15s| %-15s| %-12s%n", "Mitgl.ID", "Vorname", "Nachname", "Geburtsdatum");
				System.out.println("--------------------------------------------------------------");
				for (Mitglieder m : ergebnis) {
					String gebDatum = "-";
					if (m.getGeburtstag() != null) {
						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
						gebDatum = sdf.format(m.getGeburtstag());
					}
					System.out.printf("%-8d| %-15s| %-15s| %-12s%n", m.getMitgliederID(), m.getVorname(),
							m.getNachname(), gebDatum);
				}

				System.out.println(
						"\nBitte die MitgliederID des gewünschten Eintrags eingeben (oder Enter zum Abbrechen):");
				String auswahl = scanner.nextLine();
				if (!auswahl.isBlank()) {
					try {
						int mitgliederID = Integer.parseInt(auswahl);
						Mitglieder ausgewählt = null;
						for (Mitglieder m : ergebnis) {
							if (m.getMitgliederID() == mitgliederID) {
								ausgewählt = m;
								break;
							}
						}
						if (ausgewählt != null) {
							boolean exitDetail = false;
							int tab = 1; // Start immer mit Stammdaten

							while (!exitDetail) {
								System.out.println(
										"\n1 Stammdaten | 2 Mitgliedschaft | 3 Zahlungsdaten | 4 Kurse | 5 Verkauf");
								switch (tab) {
								case 1: // Stammdaten
									showStammdaten(ausgewählt);
									break;
								case 2:
									System.out.println("(Hier Ansicht für Mitgliedschaft einblenden)");
									break;
								case 3:
									System.out.println("(Hier Ansicht für Zahlungsdaten einblenden)");
									break;
								case 4:
									System.out.println("(Hier Ansicht für Kurse einblenden)");
									break;
								case 5:
									System.out.println("(Hier Ansicht für Verkauf einblenden)");
									break;
								default:
									// Nichts, bleibt leer
									break;
								}
								System.out.print("\nTab auswählen (1-5), 6: Zurück, 7: Hauptmenü\n");
								String tabEingabe = scanner.nextLine();
								if (tabEingabe.isBlank())
									continue;
								if (tabEingabe.equals("6")) {
									exitDetail = true;
								} else if (tabEingabe.equals("7")) {
									return true;
//									System.exit(0); // Oder setze eine entsprechende Rückkehr-Flag!
								} else {
									try {
										int tabWahl = Integer.parseInt(tabEingabe);
										if (tabWahl >= 1 && tabWahl <= 5) {
											tab = tabWahl;
										}
									} catch (NumberFormatException ex) {
										System.out.println("Ungültige Tab-Nummer!");
									}
								}
							}
						} else {
							System.out.println("Kein Mitglied mit der eingegebenen MitgliederID gefunden.");
						}
					} catch (NumberFormatException e) {
						System.out.println("Bitte eine gültige MitgliederID eingeben!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fehler bei der Mitgliedersuche: " + (e.getMessage() != null ? e.getMessage() : e));
		}
		return false;
	}

	private void showStammdaten(Mitglieder ausgewählt) {
		String vorname = ausgewählt.getVorname() != null ? ausgewählt.getVorname() : "-";
		String nachname = ausgewählt.getNachname() != null ? ausgewählt.getNachname() : "-";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
		String geburtsdatum = (ausgewählt.getGeburtstag() != null) ? sdf.format(ausgewählt.getGeburtstag()) : "-";
		int alterJahre = ausgewählt.getGeburtstag() != null ? ausgewählt.berechneAlter() : 0;
		boolean istGeburtstag = false;
		if (ausgewählt.getGeburtstag() != null) {
			java.time.LocalDate birthDate;
			if (ausgewählt.getGeburtstag() instanceof java.sql.Date) {
				birthDate = ((java.sql.Date) ausgewählt.getGeburtstag()).toLocalDate();
			} else {
				birthDate = new java.sql.Date(ausgewählt.getGeburtstag().getTime()).toLocalDate();
			}
			java.time.LocalDate today = java.time.LocalDate.now();
			istGeburtstag = today.getMonthValue() == birthDate.getMonthValue()
					&& today.getDayOfMonth() == birthDate.getDayOfMonth();
		}
		String geburtstagInfo = istGeburtstag ? "Geburtstag!" : "";
		String strasse = ausgewählt.getStrasse() != null ? ausgewählt.getStrasse() : "-";
		String hausnr = ausgewählt.getHausnr() != null ? ausgewählt.getHausnr() : "-";
		Ort ortObj = ausgewählt.getOrt();
		String plz = ortObj != null && ortObj.getPLZ() != null ? ortObj.getPLZ() : "-";
		String ort = ortObj != null && ortObj.getOrt() != null ? ortObj.getOrt() : "-";
		String tel = ausgewählt.getTelefon() != null ? ausgewählt.getTelefon() : "-";
		String mail = ausgewählt.getMail() != null ? ausgewählt.getMail() : "-";

		System.out.printf(
				"\nName:\t\t%s %s\nGeburtsdatum:\t%s\tAlter: %d Jahre %s\nAdresse:\t%s %s\n\t\t%s %s\nTelefon:\t%s\nMail:\t\t%s\n",
				vorname, nachname, geburtsdatum, alterJahre, geburtstagInfo, strasse, hausnr, plz, ort, tel, mail);
	}

	private void interessentErstellen() {
		System.out.println("Interessentenerstellung ausgewählt");
		try {
			MitgliederManager manager = new MitgliederManager();
			String vorname = IO.readString("Vorname: ");
			String nachname = IO.readString("Nachname: ");
			String telefon = IO.readString("Telefon: ");
			int id = manager.createInteressent(vorname, nachname, telefon);
			System.out.println("Interessent erfolgreich erstellt mit ID: " + id);
		} catch (Exception e) {
			System.out.println("Fehler bei der Interessentenerstellung: " + e.getMessage());
		}
	}

	private void mitgliedErstellen() {
		System.out.println("Mitgliedererstellung ausgewählt");
		try {
			MitgliederManager manager = new MitgliederManager();

			String vorname = IO.readString("Vorname: ");
			String nachname = IO.readString("Nachname: ");
			String telefon = IO.readString("Telefon: ");

			// Geburtsdatum mit Validierungs-Schleife
			java.util.Date geburtstag = null;
			while (true) {
				String geburtsdatumStr = IO.readString("Geburtsdatum (dd.MM.yyyy): ");
				if (geburtsdatumStr.isEmpty())
					break;
				try {
					geburtstag = new java.text.SimpleDateFormat("dd.MM.yyyy").parse(geburtsdatumStr);
					break;
				} catch (Exception e) {
					System.out.println("Ungültiges Datum: " + e.getMessage());
				}
			}

			boolean aktiv = false; // Immer automatisch auf "false"

			String strasse = IO.readString("Straße: ");
			String hausnr = IO.readString("Hausnummer: ");

			// PLZ mit Validator und Schleife
			String plz = "";
			StringValidator plzValidator = new StringValidator() {
				@Override
				public void validate(Object obj) throws StringException {
					super.validate(obj);
					String input = (String) obj;
					if (input.length() != 5 || !input.matches("\\d{5}")) {
						errors.add("PLZ muss genau 5 Ziffern haben.");
						throw new StringException("PLZ muss genau 5 Ziffern haben.");
					}
				}
			};
			while (true) {
				plz = IO.readString("PLZ: ");
				if (plz.isEmpty())
					break;
				try {
					plzValidator.validate(plz);
					break;
				} catch (Exception e) {
					System.out.println("Ungültige PLZ: " + e.getMessage());
				}
			}

			String ortname = IO.readString("Ort: ");

			// Mail mit Validator und Schleife
			String mail = "";
			EMailValidator mailValidator = new EMailValidator();
			while (true) {
				mail = IO.readString("Mail: ");
				if (mail.isEmpty())
					break;
				try {
					mailValidator.validate(mail);
					break;
				} catch (Exception e) {
					System.out.println("Ungültige Mailadresse: " + e.getMessage());
				}
			}

			// Ort ggf. in DB finden/erstellen
			int ortID = 0;
			Ort ortObj = null;
			if (!plz.isEmpty() && !ortname.isEmpty()) {
				OrtDAO ortDAO = manager.getOrtDAO();
				ortID = ortDAO.findOrCreateOrt(plz, ortname);
				ortObj = ortDAO.findById(ortID);
			}

			// Zahlungsdateneingabe mit Validatoren
			String zahlungsName = IO.readString("Zahlungsdaten - Name: ");
			String iban = "";
			String bic = "";

			IBANValidator ibanValidator = new IBANValidator();
			while (true) {
				iban = IO.readString("IBAN: ");
				if (iban.isEmpty())
					break;
				try {
					ibanValidator.validate(iban);
					break;
				} catch (Exception e) {
					System.out.println("Ungültige IBAN: " + e.getMessage());
				}
			}

			BICValidator bicValidator = new BICValidator();
			while (true) {
				bic = IO.readString("BIC: ");
				if (bic.isEmpty())
					break;
				try {
					bicValidator.validate(bic);
					break;
				} catch (Exception e) {
					System.out.println("Ungültiger BIC: " + e.getMessage());
				}
			}

			int zahlungsdatenID = 0;
			Zahlungsdaten zahlungsdaten = null;
			if (!iban.isEmpty() && !bic.isEmpty()) {
				ZahlungsdatenDAO zahlungsdatenDAO = manager.getZahlungsdatenDAO();
				zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(zahlungsName, iban, bic);
				zahlungsdaten = zahlungsdatenDAO.findById(zahlungsdatenID);
			}
			// Mitglied als Objekt erstellen für Java und DB
			Mitglieder mitglied = new Mitglieder(vorname, nachname, telefon, geburtstag, aktiv, strasse, hausnr, ortObj,
					zahlungsdaten, mail);
			manager.getMitgliederDAO().insert(mitglied);

			System.out.println("Mitglied erfolgreich erstellt:\n" + mitglied);

		} catch (Exception e) {
			System.out.println("Fehler bei der Mitgliedererstellung: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
