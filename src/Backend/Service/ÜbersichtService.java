package Backend.Service;

import java.sql.Connection;
import java.util.Scanner;

import Backend.Manager.RolleManager;

public class ÜbersichtService extends BaseService {

    private RolleManager rolleManager;
    private int aktuelleRolleId;

    public ÜbersichtService(Connection connection, Scanner scanner, RolleManager rolleManager, int aktuelleRolleId) {
        super(connection, scanner);
        this.rolleManager = rolleManager;
        this.aktuelleRolleId = aktuelleRolleId;
    }

    public void start() {
        boolean zurueck = false;

        // Beispiel Rechtecheck vor Nutzung der Übersicht
        if (!rolleManager.hatRecht(aktuelleRolleId, "ÜBERSICHT_ANZEIGEN")) {
            System.out.println("Zugriff verweigert: Keine Rechte für Übersicht.");
            return;
        }

        while (!zurueck) {
            System.out.println("==== Übersicht ====");
            System.out.println("1 - Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");
            String eingabe = scanner.nextLine();
            if ("1".equals(eingabe)) {
                zurueck = true;
            } else {
                System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }
        }
    }
}
