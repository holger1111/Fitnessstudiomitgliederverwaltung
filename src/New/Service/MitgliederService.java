package New.Service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import New.Service.VertragService;
import New.Objekte.Intervall;
import New.Objekte.Mitglieder;
import New.Objekte.MitgliederVertrag;
import New.Objekte.Ort;
import New.Objekte.Vertrag;
import New.Objekte.Zahlungsdaten;
import New.Validator.BICValidator;
import New.Validator.EMailValidator;
import New.Validator.IBANValidator;
import New.Validator.StringValidator;
import New.DAOs.OrtDAO;
import New.DAOs.ZahlungsdatenDAO;
import New.Exception.StringException;
import New.Helper.Datum;
import New.Helper.DatumHelper;
import New.Helper.IO;
import New.Manager.MitgliederManager;
import New.Manager.VertragManager;

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
	            System.out.println("\nBitte die MitgliederID des gewünschten Eintrags eingeben (oder Enter zum Abbrechen):");
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
	                        int tab = 1;
	                        VertragManager vertragManager = new VertragManager();
	                        while (!exitDetail) {
	                            System.out.println("\n1 Stammdaten | 2 Mitgliedschaft | 3 Zahlungsdaten | 4 Kurse | 5 Verkauf");
	                            switch (tab) {
	                            case 1:
	                                showStammdaten(ausgewählt);
	                                break;
	                            case 2:
	                                showMitgliedschaft(ausgewählt, vertragManager);
	                                break;
	                            default:
	                                System.out.println("(Tab nicht belegt)");
	                            }
	                            System.out.print("\nTab auswählen (1-5), 6: Zurück, 7: Hauptmenü\n");
	                            String tabEingabe = scanner.nextLine();
	                            if (tabEingabe.isBlank())
	                                continue;
	                            if (tabEingabe.equals("6")) {
	                                exitDetail = true;
	                            } else if (tabEingabe.equals("7")) {
	                                return true;
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

	public void showStammdaten(Mitglieder ausgewählt) {
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

	public void showMitgliedschaft(Mitglieder ausgewaehlt, VertragManager manager) throws Exception {
	    List<MitgliederVertrag> vertraege = manager.getMitgliederVertragDAO().findByMitgliedId(ausgewaehlt.getMitgliederID());
	    if (vertraege == null || vertraege.isEmpty()) {
	        System.out.println("\nDas Mitglied hat keine Verträge.");
	        return;
	    }
	    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	    Date heute = new Date();
	    for (MitgliederVertrag mv : vertraege) {
	        Vertrag v = manager.getVertragDAO().findById(mv.getVertragID());
	        Intervall intervall = manager.getIntervallDAO().findById(mv.getIntervallID());
	        double grundpreis = v.getGrundpreis();
	        double preisrabatt = mv.getPreisrabatt();
	        double wochenpreis = grundpreis - preisrabatt;
	        int laufzeit = v.getLaufzeit();
	        double gesamtwert = laufzeit * grundpreis;
	        int wochenSeitVertragsstart = (int)((heute.getTime() - mv.getVertragsbeginn().getTime()) / (1000 * 60 * 60 * 24 * 7));
	        double gezahlt = 0;
	        switch (mv.getIntervallID()) {
	            case 1:
	                gezahlt = getWochenBisErster(heute, mv) * wochenpreis; break;
	            case 2:
	                gezahlt = getWochenBisLetzterVormonat(mv) * wochenpreis; break;
	            case 3:
	                gezahlt = wochenSeitVertragsstart * wochenpreis; break;
	            case 4:
	                gezahlt = ((wochenSeitVertragsstart % 2 == 0) ? wochenSeitVertragsstart : (wochenSeitVertragsstart - 1)) * wochenpreis; break;
	        }
	        double restwert = gesamtwert - gezahlt;
	        double jeZahlungsintervall = 0;
	        switch (mv.getIntervallID()) {
	            case 1: case 2:
	                jeZahlungsintervall = wochenpreis * 52.14 / 12.0; break;
	            case 3: jeZahlungsintervall = wochenpreis; break;
	            case 4: jeZahlungsintervall = wochenpreis * 2; break;
	        }
	        Date kuendbarBis = new Date(mv.getVertragsende().getTime() - 5L * 7L * 24L * 60L * 60L * 1000L);
	        System.out.printf("\nVertragNr.: %d %s\n", mv.getVertragID(), v.getBezeichnung());
	        System.out.printf("Laufzeit: %d Wochen Zahlungsintervall: %s\n", laufzeit, intervall.getBezeichnung());
	        System.out.println("Trainingsbeginn | Vertragsbeginn | Vertragsende| Aktiv");
	        System.out.printf("%-15s | %-15s | %-13s | %-5s\n",
	                mv.getTrainingsbeginn() != null ? sdf.format(mv.getTrainingsbeginn()) : "-",
	                mv.getVertragsbeginn() != null ? sdf.format(mv.getVertragsbeginn()) : "-",
	                mv.getVertragsende() != null ? sdf.format(mv.getVertragsende()) : "-",
	                (heute.compareTo(mv.getVertragsbeginn()) >= 0 && heute.compareTo(mv.getVertragsende()) <= 0) ? "X" : ""
	        );
	        System.out.printf("Grundpreis: %.2f € | Gesamtwert: %.2f €\n", grundpreis, gesamtwert);
	        System.out.printf("Sonder-Rabatt: %.2f € | Gezahlt: %.2f €\n", preisrabatt, gezahlt);
	        System.out.printf("Wochenpreis: %.2f € | Restwert: %.2f €\n", wochenpreis, restwert);
	        System.out.printf("Je Zahlungsintervall: %.2f € | Kündbar bis: %s %s %s\n",
	                jeZahlungsintervall,
	                sdf.format(kuendbarBis),
	                mv.isGekündigt() ? "Künd.X" : "",
	                mv.isVerlängerung() ? "Verl.X" : ""
	        );
	    }
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
	

	// Wochen vom Vertragsbeginn bis zum 1. des aktuellen Monats (wie im VertragService)
	public int getWochenBisErster(Date today, MitgliederVertrag mv) {
	    Datum beginn = new Datum(mv.getVertragsbeginn());
	    Datum bis = new Datum(today);
	    Datum erster = new Datum(bis.getJahr(), bis.getMonat(), 1);
	    if (beginn.isBefore(erster)) {
	        long millisekunden = ersterZuDate(erster).getTime() - mv.getVertragsbeginn().getTime();
	        int tage = (int) (millisekunden / (1000 * 60 * 60 * 24));
	        return Math.max(0, tage / 7);
	    } else {
	        return 0;
	    }
	}

	// Wochen vom Vertragsbeginn bis zum letzten Tag des Vormonats (wie im VertragService)
	public int getWochenBisLetzterVormonat(MitgliederVertrag mv) {
	    Datum beginn = new Datum(mv.getVertragsbeginn());
	    Datum heute = DatumHelper.getAktuellesDatum();
	    int jahr = heute.getMonat() == 1 ? heute.getJahr() - 1 : heute.getJahr();
	    int monat = heute.getMonat() == 1 ? 12 : heute.getMonat() - 1;
	    java.util.Calendar cal = java.util.Calendar.getInstance();
	    cal.set(jahr, monat - 1, 1);
	    cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
	    Datum letzter = new Datum(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.DAY_OF_MONTH));
	    if (beginn.isBefore(letzter)) {
	        long millisekunden = letzterZuDate(letzter).getTime() - mv.getVertragsbeginn().getTime();
	        int tage = (int) (millisekunden / (1000 * 60 * 60 * 24));
	        return Math.max(0, tage / 7);
	    } else {
	        return 0;
	    }
	}

	private Date ersterZuDate(Datum d) {
	    java.util.Calendar cal = java.util.Calendar.getInstance();
	    cal.set(d.getJahr(), d.getMonat() - 1, d.getTag(), 0, 0, 0);
	    cal.set(java.util.Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	private Date letzterZuDate(Datum d) {
	    java.util.Calendar cal = java.util.Calendar.getInstance();
	    cal.set(d.getJahr(), d.getMonat() - 1, d.getTag(), 23, 59, 59);
	    cal.set(java.util.Calendar.MILLISECOND, 999);
	    return cal.getTime();
	}

}
