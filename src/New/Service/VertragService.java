package New.Service;

import java.sql.Connection;
import java.util.Scanner;

public class VertragService extends BaseService {

    public VertragService(Connection connection, Scanner scanner) {
        super(connection, scanner);
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
                    // Vertragssuche Logik einbauen
                    break;
                case "2":
                    // Neukunden-Logik einbauen
                    break;
                case "3":
                    zurueck = true;
                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }
        }
    }
}
