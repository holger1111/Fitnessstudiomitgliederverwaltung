package New.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import New.Manager.KursManager;
import New.Objekte.Kurs;
import New.Objekte.Kurstermin;
import New.Objekte.Trainer;
import New.Validator.IntValidator;
import New.Validator.BooleanValidator;
import New.Validator.CurrencyValidator;
import New.Validator.DateTimeValidator;
import New.Validator.StringValidator;
import New.Exception.ConnectionException;
import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Exception.BooleanException;
import New.Exception.CurrencyException;
import New.Exception.TooLongException;
import New.Exception.StringException;

public class KursService extends BaseService {

    private KursManager kursManager;
    private static final int MAX_KOMMENTAR_LENGTH = 255;

    public KursService(Connection connection, Scanner scanner) {
        super(connection, scanner);
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
            System.out.println("2. Kurs suchen");
            System.out.println("3. Kurstermine anzeigen");
            System.out.println("4. Für Kurs anmelden");
            System.out.println("5. Von Kurs abmelden");
            System.out.println("6. Teilnehmerliste anzeigen");
            System.out.println("7. Neuen Kurs erstellen");
            System.out.println("8. Neuen Kurstermin erstellen");
            System.out.println("0. Zurück zum Hauptmenü");
            System.out.print("\nBitte wählen Sie: ");
            
            String eingabe = scanner.nextLine();
            
            try {
                switch (eingabe) {
                    case "1":
                        alleKurseAnzeigen();
                        break;
                    case "2":
                        kursSuchen();
                        break;
                    case "3":
                        kurstermineAnzeigen();
                        break;
                    case "4":
                        fuerKursAnmelden();
                        break;
                    case "5":
                        vonKursAbmelden();
                        break;
                    case "6":
                        teilnehmerlisteAnzeigen();
                        break;
                    case "7":
                        neuerKurs();
                        break;
                    case "8":
                        neuerKurstermin();
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

    /**
     * Zeigt alle verfügbaren Kurse an
     */
    private void alleKurseAnzeigen() throws SQLException {
        List<Kurs> kurse = kursManager.getKursDAO().findAll();
        
        if (kurse.isEmpty()) {
            System.out.println("\nKeine Kurse vorhanden.");
            return;
        }
        
        System.out.println("\n=== Alle Kurse ===");
        System.out.printf("%-8s | %-30s | %-8s | %10s%n", "KursID", "Bezeichnung", "Termine", "Preis");
        System.out.println("=".repeat(70));
        
        for (Kurs kurs : kurse) {
            System.out.printf("%-8d | %-30s | %-8d | %9.2f €%n",
                kurs.getKursID(),
                kurs.getBezeichnung(),
                kurs.getAnzahlTermine(),
                kurs.getPreis()
            );
        }
    }

    /**
     * Sucht einen Kurs nach Bezeichnung
     */
    private void kursSuchen() throws SQLException, IntException {
        System.out.print("Suchbegriff eingeben: ");
        String suchbegriff = scanner.nextLine();
        
        List<Kurs> ergebnisse = kursManager.search(suchbegriff);
        
        if (ergebnisse.isEmpty()) {
            System.out.println("\nKeine Kurse gefunden.");
            return;
        }
        
        System.out.println("\n=== Suchergebnisse ===");
        System.out.printf("%-8s | %-30s | %-8s | %10s%n", "KursID", "Bezeichnung", "Termine", "Preis");
        System.out.println("=".repeat(70));
        
        for (Kurs kurs : ergebnisse) {
            System.out.printf("%-8d | %-30s | %-8d | %9.2f €%n",
                kurs.getKursID(),
                kurs.getBezeichnung(),
                kurs.getAnzahlTermine(),
                kurs.getPreis()
            );
        }
    }

    /**
     * Zeigt alle Termine eines Kurses an
     */
    private void kurstermineAnzeigen() throws SQLException, IntException, NotFoundException {
        int kursID = 0;
        
        // KursID validieren
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
        System.out.printf("%-12s | %-20s | %-20s | %-6s%n", "TerminID", "Datum/Zeit", "Trainer", "Aktiv");
        System.out.println("=".repeat(70));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        
        for (Kurstermin termin : termine) {
            Trainer trainer = kursManager.getTrainerDAO().findById(termin.getTrainerID());
            String trainerName = trainer != null ? trainer.getVorname() + " " + trainer.getNachname() : "-";
            
            System.out.printf("%-12d | %-20s | %-20s | %-6s%n",
                termin.getKursterminID(),
                termin.getTermin() != null ? sdf.format(termin.getTermin()) : "-",
                trainerName,
                termin.isAktiv() ? "X" : ""
            );
        }
    }

    /**
     * Meldet ein Mitglied für einen Kurstermin an
     */
    private void fuerKursAnmelden() throws SQLException, IntException, NotFoundException {
        int mitgliederID = 0;
        int kursterminID = 0;
        
        // Mitglieder-ID validieren
        while (true) {
            try {
                System.out.print("MitgliederID eingeben: ");
                String mitgliederIDStr = scanner.nextLine();
                mitgliederID = validateInt(mitgliederIDStr, "MitgliederID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Kurstermin-ID validieren
        while (true) {
            try {
                System.out.print("KursterminID eingeben: ");
                String kursterminIDStr = scanner.nextLine();
                kursterminID = validateInt(kursterminIDStr, "KursterminID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        kursManager.meldeAnFuerKurs(mitgliederID, kursterminID);
        System.out.println("\nErfolgreich für Kurs angemeldet!");
    }

    /**
     * Meldet ein Mitglied von einem Kurstermin ab
     */
    private void vonKursAbmelden() throws SQLException, IntException {
        int mitgliederID = 0;
        int kursterminID = 0;
        
        // Mitglieder-ID validieren
        while (true) {
            try {
                System.out.print("MitgliederID eingeben: ");
                String mitgliederIDStr = scanner.nextLine();
                mitgliederID = validateInt(mitgliederIDStr, "MitgliederID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Kurstermin-ID validieren
        while (true) {
            try {
                System.out.print("KursterminID eingeben: ");
                String kursterminIDStr = scanner.nextLine();
                kursterminID = validateInt(kursterminIDStr, "KursterminID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        kursManager.meldeAbVonKurs(mitgliederID, kursterminID);
        System.out.println("\nErfolgreich von Kurs abgemeldet!");
    }

    /**
     * Zeigt die Teilnehmerliste eines Kurstermins an
     */
    private void teilnehmerlisteAnzeigen() throws SQLException, IntException, NotFoundException {
        int kursterminID = 0;
        
        while (true) {
            try {
                System.out.print("KursterminID eingeben: ");
                String kursterminIDStr = scanner.nextLine();
                kursterminID = validateInt(kursterminIDStr, "KursterminID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        kursManager.zeigeKursteilnehmer(kursterminID);
    }

    /**
     * Erstellt einen neuen Kurs
     */
    private void neuerKurs() throws SQLException {
        System.out.println("\n=== Neuen Kurs erstellen ===");
        
        String bezeichnung = null;
        int anzahlTermine = 0;
        double preis = 0.0;
        String kommentar = null;
        
        // Bezeichnung validieren
        while (true) {
            try {
                System.out.print("Bezeichnung: ");
                bezeichnung = scanner.nextLine();
                validateString(bezeichnung, "Bezeichnung");
                break;
            } catch (StringException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Anzahl Termine validieren
        while (true) {
            try {
                System.out.print("Anzahl Termine: ");
                String anzahlStr = scanner.nextLine();
                anzahlTermine = validateInt(anzahlStr, "Anzahl Termine");
                if (anzahlTermine <= 0) {
                    throw new IntException("Anzahl Termine muss größer als 0 sein!");
                }
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Preis validieren
        while (true) {
            try {
                System.out.print("Preis: ");
                String preisStr = scanner.nextLine();
                preis = validateCurrency(preisStr, "Preis");
                break;
            } catch (CurrencyException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Kommentar validieren (optional)
        while (true) {
            try {
                System.out.print("Kommentar (optional, Enter zum Überspringen): ");
                kommentar = scanner.nextLine();
                if (!kommentar.isBlank()) {
                    validateKommentar(kommentar);
                }
                break;
            } catch (TooLongException e) {
                System.out.println(e.getMessage());
            }
        }
        
        Kurs neuerKurs = new Kurs();
        neuerKurs.setBezeichnung(bezeichnung);
        neuerKurs.setAnzahlTermine(anzahlTermine);
        neuerKurs.setPreis(preis);
        neuerKurs.setKommentar(kommentar.isBlank() ? null : kommentar);
        
        kursManager.getKursDAO().insert(neuerKurs);
        System.out.println("\nKurs erfolgreich erstellt! KursID: " + neuerKurs.getKursID());
    }

    /**
     * Erstellt einen neuen Kurstermin
     */
    private void neuerKurstermin() throws SQLException, IntException, NotFoundException {
        System.out.println("\n=== Neuen Kurstermin erstellen ===");
        
        int kursID = 0;
        int trainerID = 0;
        Timestamp termin = null;
        int trainerfrei = 0;
        boolean aktiv = false;
        String kommentar = null;
        
        // KursID validieren
        while (true) {
            try {
                System.out.print("KursID: ");
                String kursIDStr = scanner.nextLine();
                kursID = validateInt(kursIDStr, "KursID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // TrainerID validieren
        while (true) {
            try {
                System.out.print("TrainerID: ");
                String trainerIDStr = scanner.nextLine();
                trainerID = validateInt(trainerIDStr, "TrainerID");
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Datum/Zeit validieren
        while (true) {
            try {
                System.out.print("Datum/Zeit (dd.MM.yyyy HH:mm:ss): ");
                String terminStr = scanner.nextLine();
                termin = validateDateTime(terminStr);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Trainerfrei validieren
        while (true) {
            try {
                System.out.print("Trainerfrei (0 oder 1): ");
                String trainerfreiStr = scanner.nextLine();
                trainerfrei = validateInt(trainerfreiStr, "Trainerfrei");
                if (trainerfrei != 0 && trainerfrei != 1) {
                    throw new IntException("Trainerfrei muss 0 oder 1 sein!");
                }
                break;
            } catch (IntException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Aktiv validieren
        while (true) {
            try {
                System.out.print("Aktiv (true/false oder 1/0): ");
                String aktivStr = scanner.nextLine();
                aktiv = validateBoolean(aktivStr, "Aktiv");
                break;
            } catch (BooleanException e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Kommentar validieren (optional)
        while (true) {
            try {
                System.out.print("Kommentar (optional, Enter zum Überspringen): ");
                kommentar = scanner.nextLine();
                if (!kommentar.isBlank()) {
                    validateKommentar(kommentar);
                }
                break;
            } catch (TooLongException e) {
                System.out.println(e.getMessage());
            }
        }
        
        Kurstermin neuerTermin = new Kurstermin();
        neuerTermin.setKursID(kursID);
        neuerTermin.setTrainerID(trainerID);
        neuerTermin.setTermin(termin);
        neuerTermin.setTrainerfrei(trainerfrei);
        neuerTermin.setAktiv(aktiv);
        neuerTermin.setKommentar(kommentar.isBlank() ? null : kommentar);
        
        kursManager.getKursterminDAO().insert(neuerTermin);
        System.out.println("\nKurstermin erfolgreich erstellt! TerminID: " + neuerTermin.getKursterminID());
    }

    // ========== VALIDATOR-METHODEN ==========
    
    /**
     * Validiert Integer-Eingabe
     */
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
    
    /**
     * Validiert Boolean-Eingabe
     */
    private boolean validateBoolean(String eingabe, String feldname) throws BooleanException {
        if (eingabe.equalsIgnoreCase("true") || eingabe.equals("1")) {
            BooleanValidator validator = new BooleanValidator();
            try {
                validator.validate(true);
            } catch (BooleanException e) {
                throw new BooleanException(feldname + " konnte nicht validiert werden!");
            }
            return true;
        } else if (eingabe.equalsIgnoreCase("false") || eingabe.equals("0")) {
            BooleanValidator validator = new BooleanValidator();
            try {
                validator.validate(false);
            } catch (BooleanException e) {
                throw new BooleanException(feldname + " konnte nicht validiert werden!");
            }
            return false;
        } else {
            throw new BooleanException(feldname + " muss 'true', 'false', '1' oder '0' sein!");
        }
    }
    
    /**
     * Validiert Currency-Eingabe
     */
    private double validateCurrency(String eingabe, String feldname) throws CurrencyException {
        try {
            CurrencyValidator validator = new CurrencyValidator();
            validator.validate(eingabe);
            return validator.getValidatedValue();
        } catch (Exception e) {
            throw new CurrencyException(feldname + " ist keine gültige Währungsangabe!");
        }
    }
    
    /**
     * Validiert DateTime-Eingabe
     */
    private Timestamp validateDateTime(String eingabe) throws Exception {
        DateTimeValidator validator = new DateTimeValidator();
        validator.validate(eingabe);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        java.util.Date parsed = sdf.parse(eingabe);
        return new Timestamp(parsed.getTime());
    }
    
    /**
     * Validiert String-Eingabe
     */
    private void validateString(String eingabe, String feldname) throws StringException {
        if (eingabe == null || eingabe.trim().isEmpty()) {
            throw new StringException(feldname + " darf nicht leer sein!");
        }
        
        StringValidator validator = new StringValidator();
        try {
            validator.validate(eingabe);
        } catch (Exception e) {
            throw new StringException(feldname + " ist ungültig!");
        }
    }
    
    /**
     * Validiert Kommentar-Länge
     */
    private void validateKommentar(String kommentar) throws TooLongException {
        if (kommentar != null && kommentar.length() > MAX_KOMMENTAR_LENGTH) {
            throw new TooLongException("Kommentar darf maximal " + MAX_KOMMENTAR_LENGTH + " Zeichen lang sein! (Aktuell: " + kommentar.length() + " Zeichen)");
        }
    }
}
