package Backend.Service;

import java.sql.Connection;
import java.util.Scanner;

import Backend.Manager.RolleManager;

public class AdminService extends BaseService {

    private RolleManager rolleManager;
    private int aktuelleRolleId;

    public AdminService(Connection connection, Scanner scanner, RolleManager rolleManager, int aktuelleRolleId) {
        super(connection, scanner);
        this.rolleManager = rolleManager;
        this.aktuelleRolleId = aktuelleRolleId;
    }

    @Override
    public void start() {
        if (!rolleManager.hatRecht(aktuelleRolleId, "ADMIN")) {
            System.out.println("Zugriff verweigert: Keine Adminrechte.");
            return;
        }

        boolean running = true;

        while (running && !exitToMainMenu) {
            System.out.println("\n=== Admin Menü ===");
            System.out.println("1. Benutzer verwalten");
            System.out.println("2. Rechte verwalten");
            System.out.println("0. Zurück zum Hauptmenü");
            System.out.print("Bitte wählen: ");

            String eingabe = scanner.nextLine();

            switch (eingabe) {
                case "1":
                    benutzerVerwalten();
                    break;
                case "2":
                    rechteVerwalten();
                    break;
                case "0":
                    running = false;
                    exitToMainMenu = true;
                    break;
                default:
                    System.out.println("Ungültige Eingabe");
            }
        }
    }

    private void benutzerVerwalten() {
        System.out.println("Benutzerverwaltung ist noch nicht implementiert.");
    }

    private void rechteVerwalten() {
        System.out.println("Rechteverwaltung ist noch nicht implementiert.");
    }
}
