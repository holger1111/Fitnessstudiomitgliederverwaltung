package Backend.Service;

import java.sql.Connection;
import java.util.Scanner;

import Backend.Manager.LoginManager;
import Backend.Objekte.Benutzer;

public class LoginService extends BaseService {

    private final LoginManager loginManager;
    private Benutzer aktuellerBenutzer;

    public LoginService(Connection connection, Scanner scanner) {
        super(connection, scanner);
        this.loginManager = new LoginManager(connection);
    }

    public Benutzer getAktuellerBenutzer() {
        return aktuellerBenutzer;
    }

    @Override
    public void start() {
        try {
            while (true) {
                System.out.print("Benutzername: ");
                String benutzername = scanner.nextLine();

                System.out.print("Passwort: ");
                String passwort = scanner.nextLine();

                Benutzer benutzer = loginManager.login(benutzername, passwort);
                if (benutzer != null) {
                    aktuellerBenutzer = benutzer;
                    System.out.println("Login erfolgreich! Weiterleitung zum Hauptmenü...");
                    break;
                } else {
                    System.out.println("Benutzername oder Passwort ist falsch. Bitte erneut versuchen.\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler während der Anmeldung: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
