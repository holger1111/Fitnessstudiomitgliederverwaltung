package Backend.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import Backend.DAOs.IntervallDAO;
import Backend.DAOs.MitgliederDAO;
import Backend.DAOs.MitgliederVertragDAO;
import Backend.DAOs.OrtDAO;
import Backend.DAOs.Transaction;
import Backend.DAOs.VertragDAO;
import Backend.DAOs.ZahlungDAO;
import Backend.DAOs.ZahlungsdatenDAO;
import Backend.Exception.DateException;
import Backend.Exception.EMailException;
import Backend.Exception.StringException;
import Backend.Manager.RolleManager;
import Backend.Objekte.Intervall;
import Backend.Objekte.Mitglieder;
import Backend.Objekte.MitgliederVertrag;
import Backend.Objekte.Vertrag;
import Backend.Objekte.Zahlungsdaten;
import Backend.Validator.BICValidator;
import Backend.Validator.DateValidator;
import Backend.Validator.EMailValidator;
import Backend.Validator.IBANValidator;
import Backend.Validator.StringValidator;

public class VertragService extends BaseService {

    private final OrtDAO ortDAO;
    private final ZahlungsdatenDAO zahlungsdatenDAO;
    private final MitgliederDAO mitgliederDAO;
    private final VertragDAO vertragDAO;
    private final IntervallDAO intervallDAO;
    private final MitgliederVertragDAO mitgliederVertragDAO;
    private final ZahlungDAO zahlungDAO;
    private final RolleManager rolleManager;
    private final int aktuelleRolleId;

    public VertragService(Connection connection, Scanner scanner, RolleManager rolleManager, int aktuelleRolleId) {
        super(connection, scanner);
        this.ortDAO = new OrtDAO(connection);
        this.zahlungsdatenDAO = new ZahlungsdatenDAO(connection);
        this.mitgliederDAO = new MitgliederDAO(connection);
        this.vertragDAO = new VertragDAO(connection);
        this.intervallDAO = new IntervallDAO(connection);
        this.mitgliederVertragDAO = new MitgliederVertragDAO(connection);
        this.zahlungDAO = new ZahlungDAO(connection);
        this.rolleManager = rolleManager;
        this.aktuelleRolleId = aktuelleRolleId;
    }

    public void start() {
        boolean zurueck = false;
        while (!zurueck) {
            System.out.println("==== Vertragsverwaltung ====");
            System.out.println("1 - Suche");
            System.out.println("2 - Neukunde anlegen");
            System.out.println("3 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();

            switch (eingabe) {
                case "1":
                    if (rolleManager.hatRecht(aktuelleRolleId, "VERTRAG_SUCHEN")) {
                        SucheService sucheService = new SucheService(connection, scanner, rolleManager, aktuelleRolleId);
                        sucheService.start();
                        if (sucheService.shouldExitToMainMenu()) {
                            return;
                        }
                    } else {
                        System.out.println("Keine Rechte für Vertragssuche.");
                    }
                    break;
                case "2":
                    if (rolleManager.hatRecht(aktuelleRolleId, "VERTRAG_NEUKUNDE")) {
                        neukundeAnlegen();
                    } else {
                        System.out.println("Keine Rechte für Neukunde anlegen.");
                    }
                    break;
                case "3":
                    zurueck = true;
                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }
        }
    }
    // ========== HAUPTLOGIK: NEUKUNDE ANLEGEN ==========
    private void neukundeAnlegen() {
        Transaction tx = null;
        try {
            tx = new Transaction(connection);
            System.out.println("\n==== Neukunde anlegen ====\n");

            // Seite 1: Kundendaten erfassen
            String vorname = erfasseVorname();
            String nachname = erfasseNachname();
            String strasse = erfasseStrasse();
            String hausnr = erfasseHausnr();
            String plz = erfassePLZ();
            String ort = erfasseOrt();
            String geburtsdatum = erfasseGeburtsdatum();
            String telefon = erfasseTelefon();
            String mail = erfasseMail();

            int ortID = ortDAO.findOrCreateOrt(plz, ort);

            // Seite 2: Vertrag erfassen
            System.out.println("\n✓ Kundendaten erfasst. Weiter zu Vertragsdaten...\n");

            Vertrag vertrag = waehleVertragsart();
            int laufzeit = vertrag.getLaufzeit();
            double grundpreis = vertrag.getGrundpreis();

            Intervall intervall = waehleZahlungsintervall();
            String intervallBezeichnung = intervall.getBezeichnung();
            int zahlungsintervall = Integer.parseInt(intervall.getZahlungsintervall());

            Date vertragsbeginn = erfasseVertragsbeginn();
            Date vertragsende = berechneVertragsende(vertragsbeginn, laufzeit);

            double preisrabatt = erfasseSonderRabatt(grundpreis);

            String kommentar = "";
            if (preisrabatt > 0.0) kommentar = erfasseKommentar();

            Date trainingsbeginn = erfasseTrainingsbeginn(vertragsbeginn);
            double wochenpreis = grundpreis - preisrabatt;
            double betragJeIntervall = berechneIntervallbetrag(wochenpreis, zahlungsintervall);
            Date zahlungsbeginn = berechneZahlungsbeginn(vertragsbeginn, intervall);

            // Seite 3: Zahlungsdaten
            System.out.println("\n✓ Vertragsdaten erfasst. Weiter zu Zahlungsdaten...\n");
            String kontoinhaber = erfasseKontoinhaber();
            String iban = erfasseIBAN();
            String bic = erfasseBIC();

            int zahlungsdatenID = zahlungsdatenDAO.findOrCreateZahlungsdaten(kontoinhaber, iban, bic);

            // Finale Überblick
            System.out.println("\n=== Übersicht aller Angaben ===");
            System.out.println("Name: " + vorname + " " + nachname);
            System.out.println("Adresse: " + strasse + " " + hausnr + ", " + plz + " " + ort);
            System.out.println("Geburtsdatum: " + geburtsdatum);
            System.out.println("Telefon: " + telefon);
            System.out.println("E-Mail: " + mail);
            System.out.println("Vertrag: " + vertrag.getBezeichnung());
            System.out.println("Laufzeit: " + laufzeit + " Monate");
            System.out.println("Zahlungsintervall: " + intervallBezeichnung);
            System.out.println("Vertragsbeginn: " + new SimpleDateFormat("dd.MM.yyyy").format(vertragsbeginn));
            System.out.println("Vertragsende: " + new SimpleDateFormat("dd.MM.yyyy").format(vertragsende));
            System.out.println("Wochenpreis: " + wochenpreis);
            System.out.println("Betrag je Intervall: " + betragJeIntervall);
            System.out.println("Zahlungsbeginn: " + new SimpleDateFormat("dd.MM.yyyy").format(zahlungsbeginn));
            System.out.println("Kontoinhaber: " + kontoinhaber);
            System.out.println("IBAN: " + iban);
            System.out.println("BIC: " + bic);
            if (!kommentar.isEmpty()) System.out.println("Kommentar: " + kommentar);

            System.out.print("\nSpeichern? (ja/nein): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("ja")) {
                System.out.println("Abgebrochen!");
                tx.rollback();
                return;
            }

            // Datums-Konvertierung
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date geburtstagDate = sdf.parse(geburtsdatum);

            Mitglieder neuesMitglied = new Mitglieder();
            neuesMitglied.setVorname(vorname);
            neuesMitglied.setNachname(nachname);
            neuesMitglied.setTelefon(telefon);
            neuesMitglied.setGeburtstag(geburtstagDate);
            neuesMitglied.setAktiv(true);
            neuesMitglied.setStrasse(strasse);
            neuesMitglied.setHausnr(hausnr);
            neuesMitglied.setOrtID(ortID);
            neuesMitglied.setZahlungsdatenID(zahlungsdatenID);
            neuesMitglied.setMail(mail);

            mitgliederDAO.insert(neuesMitglied);
            int mitgliedID = neuesMitglied.getMitgliederID();

            Backend.Objekte.Zahlung zahlung = new Backend.Objekte.Zahlung();
            zahlung.setZahlungsart("SEPA-Lastschrift");
            zahlungDAO.insert(zahlung);
            int zahlungID = zahlung.getZahlungID();

            MitgliederVertrag neuerVertrag = new MitgliederVertrag();
            neuerVertrag.setMitgliederID(mitgliedID);
            neuerVertrag.setVertragID(vertrag.getVertragID());
            neuerVertrag.setVertragsbeginn(vertragsbeginn);
            neuerVertrag.setVertragsende(vertragsende);
            neuerVertrag.setPreisrabatt(preisrabatt);
            neuerVertrag.setIntervallID(intervall.getIntervallID());
            neuerVertrag.setZahlungID(zahlungID);
            neuerVertrag.setTrainingsbeginn(trainingsbeginn);
            neuerVertrag.setKommentar(kommentar.isEmpty() ? null : kommentar);
            neuerVertrag.setAktiv(true);
            neuerVertrag.setGekündigt(false);
            neuerVertrag.setVerlängerung(false);

            Zahlungsdaten vollstaendigeZahlungsdaten = zahlungsdatenDAO.findById(zahlungsdatenID);
            neuesMitglied.setZahlungsdaten(vollstaendigeZahlungsdaten);

            mitgliederVertragDAO.insert(neuerVertrag, vertrag, zahlung, neuesMitglied, laufzeit);

            System.out.println("\n✓✓✓ ERFOLG! Neukunde wurde angelegt! ✓✓✓\n" +
                    "MitgliedID: " + mitgliedID + "\nVertragID: " + neuerVertrag.getVertragNr() +
                    "\nZahlungID: " + zahlungID + "\nOrtID: " + ortID + "\nZahlungsdatenID: " + zahlungsdatenID);

            tx.commit();

        } catch (Exception e) {
            System.out.println("Fehler beim Erstellen des Vertrags: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (tx != null) try { tx.close(); } catch (SQLException e) {}
        }
    }

    private String erfasseVorname() throws StringException {
        while (true) {
            System.out.print("Vorname: ");
            String input = scanner.nextLine();
            try {
                new StringValidator().validate(input);
                return input.trim();
            } catch (StringException e) {
                System.out.println("Ungültiger Vorname: " + e.getMessage());
            }
        }
    }

    private String erfasseNachname() throws StringException {
        while (true) {
            System.out.print("Nachname: ");
            String input = scanner.nextLine();
            try {
                new StringValidator().validate(input);
                return input.trim();
            } catch (StringException e) {
                System.out.println("Ungültiger Nachname: " + e.getMessage());
            }
        }
    }

    private String erfasseStrasse() throws StringException {
        while (true) {
            System.out.print("Straße: ");
            String input = scanner.nextLine();
            try {
                new StringValidator().validate(input);
                return input.trim();
            } catch (StringException e) {
                System.out.println("Ungültige Straße: " + e.getMessage());
            }
        }
    }

    private String erfasseHausnr() throws StringException {
        while (true) {
            System.out.print("Hausnummer: ");
            String input = scanner.nextLine();
            try {
                new StringValidator().validate(input);
                return input.trim();
            } catch (StringException e) {
                System.out.println("Ungültige Hausnummer: " + e.getMessage());
            }
        }
    }

    private String erfassePLZ() throws StringException {
        while (true) {
            System.out.print("PLZ: ");
            String input = scanner.nextLine();
            try {
                if (input.length() != 5 || !input.matches("\\d{5}")) {
                    throw new StringException("PLZ muss genau 5 Ziffern haben.");
                }
                return input.trim();
            } catch (StringException e) {
                System.out.println("Ungültige PLZ: " + e.getMessage());
            }
        }
    }

    private String erfasseOrt() throws StringException {
        while (true) {
            System.out.print("Ort: ");
            String input = scanner.nextLine();
            try {
                new StringValidator().validate(input);
                return input.trim();
            } catch (StringException e) {
                System.out.println("Ungültiger Ort: " + e.getMessage());
            }
        }
    }

    private String erfasseGeburtsdatum() throws Exception {
        while (true) {
            System.out.print("Geburtsdatum (dd.MM.yyyy): ");
            String input = scanner.nextLine();
            try {
                new DateValidator().validate(input);
                return input.trim();
            } catch (DateException e) {
                System.out.println("Ungültiges Geburtsdatum: " + e.getMessage());
            }
        }
    }

    private String erfasseTelefon() {
        System.out.print("Telefon: ");
        return scanner.nextLine();
    }

    private String erfasseMail() throws EMailException, StringException {
        while (true) {
            System.out.print("E-Mail: ");
            String input = scanner.nextLine();
            try {
                new EMailValidator().validate(input);
                return input.trim();
            } catch (EMailException e) {
                System.out.println("Ungültige Mailadresse: " + e.getMessage());
            }
        }
    }

    private Vertrag waehleVertragsart() throws SQLException {
        while (true) {
            List<Vertrag> alleVertraege = vertragDAO.findAll();
            System.out.println("Vertragsarten:");
            for (int i = 0; i < alleVertraege.size(); i++) {
                Vertrag v = alleVertraege.get(i);
                System.out.printf("%d: %s (Laufzeit: %d, Grundpreis: %.2f)\n",
                        (i + 1), v.getBezeichnung(), v.getLaufzeit(), v.getGrundpreis());
            }
            System.out.print("Vertragsart wählen (Nummer): ");
            try {
                int auswahl = Integer.parseInt(scanner.nextLine());
                if (auswahl > 0 && auswahl <= alleVertraege.size()) {
                    return alleVertraege.get(auswahl - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Eingabe ungültig.");
            }
        }
    }

    private Intervall waehleZahlungsintervall() throws SQLException {
        while (true) {
            List<Intervall> alleIntervalle = intervallDAO.findAll();
            System.out.println("Zahlungsintervall:");
            for (int i = 0; i < alleIntervalle.size(); i++) {
                Intervall intv = alleIntervalle.get(i);
                System.out.printf("%d: %s (%s Wochen)\n",
                        (i + 1), intv.getBezeichnung(), intv.getZahlungsintervall());
            }
            System.out.print("Intervall wählen (Nummer): ");
            try {
                int auswahl = Integer.parseInt(scanner.nextLine());
                if (auswahl > 0 && auswahl <= alleIntervalle.size()) {
                    return alleIntervalle.get(auswahl - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Eingabe ungültig.");
            }
        }
    }

    private Date erfasseVertragsbeginn() throws DateException, Exception {
        while (true) {
            System.out.print("Vertragsbeginn (dd.MM.yyyy): ");
            String input = scanner.nextLine();
            try {
                new DateValidator().validate(input);
                return new SimpleDateFormat("dd.MM.yyyy").parse(input.trim());
            } catch (DateException | java.text.ParseException e) {
                System.out.println("Ungültiger Vertragsbeginn: " + e.getMessage());
            }
        }
    }

    private Date berechneVertragsende(Date vertragsbeginn, int laufzeit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(vertragsbeginn);
        cal.add(Calendar.MONTH, laufzeit);
        return cal.getTime();
    }

    private double erfasseSonderRabatt(double grundpreis) {
        System.out.print("Sonderrabatt in EUR (optional, Enter = 0): ");
        String input = scanner.nextLine();
        if (input.isEmpty()) return 0.0;
        try {
            double rabatt = Double.parseDouble(input.replace(',', '.'));
            if (rabatt < 0.0 || rabatt >= grundpreis) {
                System.out.println("Rabatt ungültig.");
                return 0.0;
            }
            return rabatt;
        } catch (NumberFormatException e) {
            System.out.println("Rabatt ungültig.");
            return 0.0;
        }
    }

    private String erfasseKommentar() {
        System.out.print("Kommentar zum Rabatt: ");
        return scanner.nextLine();
    }

    private Date erfasseTrainingsbeginn(Date vertragsbeginn) {
        System.out.print("Trainingsbeginn (optional, Enter = Vertragsbeginn): ");
        String input = scanner.nextLine();
        if (input.isEmpty()) return vertragsbeginn;
        try {
            new DateValidator().validate(input);
            return new SimpleDateFormat("dd.MM.yyyy").parse(input.trim());
        } catch (Exception e) {
            return vertragsbeginn;
        }
    }

    private double berechneIntervallbetrag(double wochenpreis, int zahlungsintervall) {
        return wochenpreis * zahlungsintervall;
    }

    private Date berechneZahlungsbeginn(Date vertragsbeginn, Intervall intervall) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(vertragsbeginn);
        // ggf. anpassen je nach Vertragsbedingungen
        return cal.getTime();
    }

    private String erfasseKontoinhaber() throws StringException {
        System.out.print("Kontoinhaber: ");
        String input = scanner.nextLine();
        new StringValidator().validate(input);
        return input;
    }

    private String erfasseIBAN() {
        while (true) {
            System.out.print("IBAN: ");
            String input = scanner.nextLine();
            try {
                new IBANValidator().validate(input);
                return input.trim();
            } catch (Exception e) {
                System.out.println("Ungültige IBAN: " + e.getMessage());
            }
        }
    }

    private String erfasseBIC() {
        while (true) {
            System.out.print("BIC: ");
            String input = scanner.nextLine();
            try {
                new BICValidator().validate(input);
                return input.trim();
            } catch (Exception e) {
                System.out.println("Ungültige BIC: " + e.getMessage());
            }
        }
    }
}

