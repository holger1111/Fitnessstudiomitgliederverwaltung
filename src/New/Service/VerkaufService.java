package New.Service;

import java.sql.Connection;
import java.util.Scanner;

public class VerkaufService extends BaseService {

    public VerkaufService(Connection connection, Scanner scanner) {
        super(connection, scanner);
    }

    public void start() {
        boolean zurueck = false;
        while (!zurueck) {
            System.out.println("==== Verkaufsverwaltung ====");
            System.out.println("1 - Suche");
            System.out.println("2 - Verkauf starten");
            System.out.println("3 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();
            switch (eingabe) {
                case "1":
                    // Suche-Logik hier einbauen
                    break;
                case "2":
                    // Verkauf starten Logik hier einbauen
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
