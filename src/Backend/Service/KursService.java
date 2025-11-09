package Backend.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import Backend.Exception.ConnectionException;
import Backend.Exception.IntException;
import Backend.Exception.NotFoundException;
import Backend.Manager.KursManager;
import Backend.Manager.RolleManager;
import Backend.Objekte.Kurs;
import Backend.Objekte.Kursleitung;
import Backend.Objekte.Kurstermin;
import Backend.Objekte.Mitarbeiter;
import Backend.Validator.IntValidator;

public class KursService extends BaseService {

    private KursManager kursManager;
    private RolleManager rolleManager;
    private int aktuelleRolleId;

    public KursService(Connection connection, Scanner scanner, RolleManager rolleManager, int aktuelleRolleId) {
        super(connection, scanner);
        this.rolleManager = rolleManager;
        this.aktuelleRolleId = aktuelleRolleId;
        try {
            this.kursManager = new KursManager();
        } catch (ConnectionException | SQLException e) {
            System.out.println("Fehler beim Initialisieren des KursManagers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() {
        boolean running = true;

        while (running && !exitToMainMenu) {
            System.out.println("\n=== Kursverwaltung ===");
            System.out.println("1. Alle Kurse anzeigen");
            System.out.println("2. Alle Kurstermine anzeigen");
            System.out.println("3. Kurstermine eines Kurses anzeigen");
            System.out.println("4. Für Kurs anmelden");
            System.out.println("5. Von Kurs abmelden");
            System.out.println("6. Teilnehmerliste anzeigen");
            System.out.println("0. Zurück zum Hauptmenü");
            System.out.print("\nBitte wählen Sie: ");

            String eingabe = scanner.nextLine();

            try {
                switch (eingabe) {
                    case "1":
                        if (rolleManager.hatRecht(aktuelleRolleId, "KURSE_ANZEIGEN")) {
                            alleKurseAnzeigen();
                        } else {
                            System.out.println("Zugriff verweigert: Keine Rechte zum Anzeigen der Kurse.");
                        }
                        break;
                    case "2":
                        if (rolleManager.hatRecht(aktuelleRolleId, "KURSTERMIN_ANZEIGEN")) {
                            alleKurstermineAnzeigen();
                        } else {
                            System.out.println("Zugriff verweigert: Keine Rechte zum Anzeigen der Kurstermine.");
                        }
                        break;
                    case "3":
                        if (rolleManager.hatRecht(aktuelleRolleId, "KURSTERMIN_ANZEIGEN")) {
                            kurstermineAnzeigen();
                        } else {
                            System.out.println("Zugriff verweigert: Keine Rechte zum Anzeigen der Kurstermine.");
                        }
                        break;
                    case "4":
                        if (rolleManager.hatRecht(aktuelleRolleId, "KURS_ANMELDEN")) {
                            fuerKursAnmelden();
                        } else {
                            System.out.println("Zugriff verweigert: Keine Rechte für Kursanmeldung.");
                        }
                        break;
                    case "5":
                        if (rolleManager.hatRecht(aktuelleRolleId, "KURS_ABMELDEN")) {
                            vonKursAbmelden();
                        } else {
                            System.out.println("Zugriff verweigert: Keine Rechte für Kursabmeldung.");
                        }
                        break;
                    case "6":
                        if (rolleManager.hatRecht(aktuelleRolleId, "TEILNEHMERLISTE_ANZEIGEN")) {
                            teilnehmerlisteAnzeigen();
                        } else {
                            System.out.println("Zugriff verweigert: Keine Rechte zum Anzeigen der Teilnehmerliste.");
                        }
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Ungültige Eingabe!");
                }
            } catch (Exception e) {
                System.out.println("Fehler: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void alleKurseAnzeigen() throws SQLException {
        List<Kurs> kurse = kursManager.getKursDAO().findAll();

        if (kurse.isEmpty()) {
            System.out.println("\nKeine Kurse vorhanden.");
            return;
        }

        System.out.println("\n=== Alle Kurse ===");
        System.out.printf("%-8s | %-30s | %-8s | %10s\n", "KursID", "Bezeichnung", "Termine", "Preis");
        System.out.println("=".repeat(70));

        for (Kurs kurs : kurse) {
            System.out.printf("%-8d | %-30s | %-8d | %9.2f €\n",
                kurs.getKursID(),
                kurs.getBezeichnung(),
                kurs.getAnzahlTermine(),
                kurs.getPreis()
            );
        }
    }

    private void alleKurstermineAnzeigen() throws SQLException, IntException, NotFoundException {
        List<Kurstermin> termine = kursManager.getKursterminDAO().findAll();

        if (termine.isEmpty()) {
            System.out.println("\nKeine Kurstermine vorhanden.");
            return;
        }

        System.out.println("\n=== Alle Kurstermine ===");
        System.out.printf("%-13s | %-16s | %-20s | %-30s\n",
            "TerminID", "Datum/Zeit", "Trainer", "Kurs");
        System.out.println("=".repeat(90));

        for (Kurstermin termin : termine) {
            String trainerName = "-";
            Kursleitung kl = kursManager.getKursleitungDAO().findByKursterminId(termin.getKursterminID());
            if (kl != null) {
                Mitarbeiter trainer = kursManager.getMitarbeiterDAO().findById(kl.getMitarbeiterID());
                if (trainer != null) {
                    trainerName = trainer.getVorname() + " " + trainer.getNachname();
                }
            }

            Kurs kurs = kursManager.getKursDAO().findById(termin.getKursID());
            String kursName = kurs != null ? kurs.getBezeichnung() : "-";

            System.out.printf("%-13d | %-16s | %-20s | %-30s\n",
                termin.getKursterminID(),
                formatiereDatumZeit(termin.getTermin()),
                trainerName,
                kursName
            );
        }
    }

    private void kurstermineAnzeigen() throws SQLException, IntException, NotFoundException {
        int kursID = 0;

        while (true) {
            try {
                System.out.print("KursID eingeben: ");
                String kursIDStr = scanner.nextLine();
                kursID = validateInt(kursIDStr, "KursID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }

        Kurs kurs = kursManager.getKursDAO().findById(kursID);
        if (kurs == null) {
            System.out.println("Kurs nicht gefunden!");
            return;
        }

        List<Kurstermin> termine = kursManager.findTermineByKursId(kursID);

        if (termine.isEmpty()) {
            System.out.println("\nKeine Termine für diesen Kurs vorhanden.");
            return;
        }

        System.out.println("\n=== Kurstermine für: " + kurs.getBezeichnung() + " ===");
        System.out.printf("%-12s | %-16s | %-20s | %-6s\n", "TerminID", "Datum/Zeit", "Trainer", "Aktiv");
        System.out.println("=".repeat(70));

        for (Kurstermin termin : termine) {
            String trainerName = "-";
            Kursleitung kl = kursManager.getKursleitungDAO().findByKursterminId(termin.getKursterminID());
            if (kl != null) {
                Mitarbeiter trainer = kursManager.getMitarbeiterDAO().findById(kl.getMitarbeiterID());
                if (trainer != null) {
                    trainerName = trainer.getVorname() + " " + trainer.getNachname();
                }
            }

            System.out.printf("%-12d | %-16s | %-20s | %-6s\n",
                termin.getKursterminID(),
                formatiereDatumZeit(termin.getTermin()),
                trainerName,
                termin.isAktiv() ? "X" : ""
            );
        }
    }

    private void fuerKursAnmelden() throws SQLException, IntException, NotFoundException {
        System.out.print("MitgliederID eingeben: ");
        int mitgliederID = validateInt(scanner.nextLine(), "MitgliederID");
        System.out.print("KursterminID eingeben: ");
        int kursterminID = validateInt(scanner.nextLine(), "KursterminID");

        try {
            kursManager.meldeAnFuerKurs(mitgliederID, kursterminID);
            System.out.println("Anmeldung erfolgreich.");
        } catch (NotFoundException e) {
            System.out.println("Anmeldung fehlgeschlagen: " + e.getMessage());
        }
    }

    private void teilnehmerlisteAnzeigen() throws SQLException, IntException, NotFoundException {
        System.out.print("KursterminID eingeben: ");
        int kursterminID = validateInt(scanner.nextLine(), "KursterminID");
        kursManager.zeigeKursteilnehmer(kursterminID);
    }

    private void vonKursAbmelden() throws SQLException, IntException, NotFoundException {
        System.out.print("MitgliederID eingeben: ");
        int mitgliederID = validateInt(scanner.nextLine(), "MitgliederID");
        System.out.print("KursterminID eingeben: ");
        int kursterminID = validateInt(scanner.nextLine(), "KursterminID");

        try {
            kursManager.meldeAbVonKurs(mitgliederID, kursterminID);
            System.out.println("Abmeldung erfolgreich.");
        } catch (NotFoundException e) {
            System.out.println("Abmeldung fehlgeschlagen: " + e.getMessage());
        }
    }

    private int validateInt(String eingabe, String feldname) throws IntException {
        try {
            int wert = Integer.parseInt(eingabe);
            IntValidator validator = new IntValidator();
            validator.validate(wert);
            return wert;
        } catch (NumberFormatException e) {
            throw new IntException(feldname + " muss eine ganze Zahl sein!");
        }
    }

    private String formatiereDatumZeit(Timestamp timestamp) {
        if (timestamp == null) {
            return "-";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);

        int tag = cal.get(Calendar.DAY_OF_MONTH);
        int monat = cal.get(Calendar.MONTH) + 1;
        int jahr = cal.get(Calendar.YEAR);
        int stunde = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return String.format("%02d.%02d.%4d %02d:%02d", tag, monat, jahr, stunde, minute);
    }
}
