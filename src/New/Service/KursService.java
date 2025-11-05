package New.Service;

import java.sql.Connection;
import java.util.Scanner;

public class KursService extends BaseService {

    public KursService(Connection connection, Scanner scanner) {
        super(connection, scanner);
    }

    public void start() {
        boolean zurueck = false;
        while (!zurueck) {
            System.out.println("==== Kursverwaltung ====");
            System.out.println("1 - Suche");
            System.out.println("2 - Kursplan");
            System.out.println("3 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();
            switch (eingabe) {
                case "1":
                    // Kurssuche Logik einbauen
                    break;
                case "2":
                    // Kursplan Logik einbauen
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
