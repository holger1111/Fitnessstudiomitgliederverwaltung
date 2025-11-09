package Backend.Manager;

import java.sql.Connection;
import java.util.Scanner;

import Backend.Exception.ConnectionException;
import Backend.Objekte.*;
import Backend.Service.*;


public class Main {

    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);
        ConnectionManager connectionManager = new ConnectionManager();

        try {
            // Datenbankverbindung herstellen
            connection = connectionManager.getConnection();
            System.out.println("✓ Datenbankverbindung erfolgreich!\n");

            // RolleManager zentral erzeugen
            RolleManager rolleManager = new RolleManager();

            // LoginService initialisieren
            LoginService loginService = new LoginService(connection, scanner);

            // Login starten (schleife intern)
            loginService.start();

            // Benutzer mit Rolle aus LoginService holen
            Benutzer currentBenutzer = loginService.getAktuellerBenutzer();
            int aktuelleRolleId = currentBenutzer.getRolleID();

            // Services mit Verbindung, Scanner, RolleManager und aktuelle Rolle erzeugen
            MitgliederService mitgliederService = new MitgliederService(connection, scanner, rolleManager, aktuelleRolleId);
            VerkaufService verkaufService = new VerkaufService(connection, scanner, rolleManager, aktuelleRolleId);
            KursService kursService = new KursService(connection, scanner, rolleManager, aktuelleRolleId);
            VertragService vertragService = new VertragService(connection, scanner, rolleManager, aktuelleRolleId);
            ÜbersichtService übersichtService = new ÜbersichtService(connection, scanner, rolleManager, aktuelleRolleId);
            SucheService sucheService = new SucheService(connection, scanner, rolleManager, aktuelleRolleId);
            AdminService adminService = new AdminService(connection, scanner, rolleManager, aktuelleRolleId);

            // Hauptmenü mit allen Services starten
            HauptmenüService hauptmenüService = new HauptmenüService(
                connection, scanner,
                mitgliederService,
                verkaufService,
                kursService,
                vertragService,
                übersichtService,
                sucheService,
                adminService
            );

            hauptmenüService.start();

        } catch (ConnectionException e) {
            System.err.println("✗ Fehler beim Verbinden mit der Datenbank:");
            System.err.println("  " + e.getMessage());
            System.err.println("\nBitte überprüfen Sie:");
            System.err.println("  - Ist der MySQL-Server gestartet?");
            System.err.println("  - Sind die Verbindungsdaten korrekt?");
            System.err.println("  - Existiert die Datenbank 'Mitgliederverwaltung'?");
        } finally {
            scanner.close();
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("\n✓ Datenbankverbindung geschlossen.");
                } catch (Exception e) {
                    System.err.println("✗ Fehler beim Schließen der Verbindung: " + e.getMessage());
                }
            }
        }
    }
}
