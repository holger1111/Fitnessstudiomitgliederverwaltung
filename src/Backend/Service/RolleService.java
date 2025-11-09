package Backend.Service;

import java.sql.Connection;
import java.util.Scanner;

import Backend.Manager.RolleManager;

public class RolleService extends BaseService {

    private final RolleManager rolleManager;
    private int aktuelleRolleID;

    public RolleService(Connection connection, Scanner scanner, RolleManager rolleManager, int aktuelleRolleID) {
        super(connection, scanner);
        this.rolleManager = rolleManager;
        this.aktuelleRolleID = aktuelleRolleID;
    }

    @Override
    public void start() {
        System.out.println("Rollenbasierte Aktionen-Kontrolle.");
        // Beispielnutzung
        if (rolleManager.hatRecht(aktuelleRolleID, "ADMIN_SETTINGS")) {
            System.out.println("Admin-Einstellungen erlaubt.");
            // admin-spezifische Aktionen/Menüs anzeigen
        } else {
            System.out.println("Kein Zugriff auf Admin-Einstellungen.");
        }
    }

    public boolean hatZugriff(String aktion) {
        return rolleManager.hatRecht(aktuelleRolleID, aktion);
    }

    // Setter, falls die Rolle nach Login neu gesetzt werden soll
    public void setAktuelleRolleID(int neueRolleID) {
        this.aktuelleRolleID = neueRolleID;
    }
}
