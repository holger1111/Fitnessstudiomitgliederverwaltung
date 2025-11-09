package Backend.Service;

import java.sql.Connection;
import java.util.Scanner;

import Backend.DAOs.OrtDAO;
import Backend.DAOs.ZahlungsdatenDAO;
import Backend.Exception.StringException;
import Backend.Helper.IO;
import Backend.Manager.MitgliederManager;
import Backend.Manager.RolleManager;
import Backend.Objekte.Mitglieder;
import Backend.Objekte.Ort;
import Backend.Objekte.Zahlungsdaten;
import Backend.Validator.BICValidator;
import Backend.Validator.EMailValidator;
import Backend.Validator.IBANValidator;
import Backend.Validator.StringValidator;

public class MitgliederService extends BaseService {

    private final RolleManager rolleManager;
    private final int aktuelleRolleId;

    public MitgliederService(Connection connection, Scanner scanner, RolleManager rolleManager, int aktuelleRolleId) {
        super(connection, scanner);
        this.rolleManager = rolleManager;
        this.aktuelleRolleId = aktuelleRolleId;
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
                	SucheService sucheService = new SucheService(connection, scanner, rolleManager, aktuelleRolleId);
                    sucheService.start();
                    if (sucheService.shouldExitToMainMenu()) {
                        return;
                    }
                    break;

                case "2":
                    if (rolleManager.hatRecht(aktuelleRolleId, "INTERESSENTEN_ANLEGEN")) {
                        interessentErstellen();
                    } else {
                        System.out.println("Zugriff verweigert: Kein Recht Interesse zu erstellen.");
                    }
                    break;

                case "3":
                    if (rolleManager.hatRecht(aktuelleRolleId, "MITGLIEDER_ANLEGEN")) {
                        mitgliedErstellen();
                    } else {
                        System.out.println("Zugriff verweigert: Kein Recht Mitglieder zu erstellen.");
                    }
                    break;

                case "4":
                    zurueck = true;
                    break;

                default:
                    System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }
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

            boolean aktiv = false;
            String strasse = IO.readString("Straße: ");
            String hausnr = IO.readString("Hausnummer: ");

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

            EMailValidator mailValidator = new EMailValidator();
            String mail = "";
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

            int ortID = 0;
            Ort ortObj = null;
            if (!plz.isEmpty() && !ortname.isEmpty()) {
                OrtDAO ortDAO = manager.getOrtDAO();
                ortID = ortDAO.findOrCreateOrt(plz, ortname);
                ortObj = ortDAO.findById(ortID);
            }

            String zahlungsName = IO.readString("Zahlungsdaten - Name: ");
            String iban = "";
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

            String bic = "";
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
