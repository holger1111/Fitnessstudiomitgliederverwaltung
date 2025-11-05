package New.Manager;

import java.util.Scanner;
import java.util.List;
import New.Objekte.Mitglieder;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean weiter = true;

        while (weiter) {
            System.out.println("==== Hauptmenü ====");
            System.out.println("1 - Nach Mitgliedern suchen");
            System.out.println("2 - Interessenten erstellen");
            System.out.println("3 - Mitglieder erstellen");
            System.out.println("0 - Programm beenden");
            System.out.print("Bitte eine Zahl eingeben: ");

            String eingabe = scanner.nextLine();

            switch (eingabe) {
            case "1":
                System.out.println("Mitgliedersuche ausgewählt");
                System.out.print("Bitte Suchbegriff eingeben: ");
                String suchbegriff = scanner.nextLine();
                try {
                    MitgliederManager manager = new MitgliederManager(); // ggf. als Attribut, nicht in jeder Schleife neu!
                    List<Mitglieder> ergebnis = manager.search(suchbegriff);
                    if (ergebnis.isEmpty()) {
                        System.out.println("Keine Mitglieder gefunden.");
                    } else {
                        System.out.println("Gefundene Mitglieder:");
                        for (Mitglieder m : ergebnis) {
                            // Einfachausgabe – passe gerne an
                            System.out.println(m.toString());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Fehler bei der Mitgliedersuche: " + e.getMessage());
                    e.printStackTrace();
                }
                break;

                case "2":
                    System.out.println("Interessentenerstellung ausgewählt");
                    // Hier Aufruf Erstellung Interessenten
                    break;
                case "3":
                    System.out.println("Mitgliedererstellung ausgewählt");
                    // Hier Aufruf Erstellung Mitglieder
                    break;
                case "0":
                    System.out.println("Programm wird beendet.");
                    weiter = false;
                    break;
                default:
                    System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
            }

            System.out.println();
        }

        scanner.close();
    }
}
