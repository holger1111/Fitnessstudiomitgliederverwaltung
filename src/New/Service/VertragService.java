package New.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import New.Helper.Datum;
import New.Helper.DatumHelper;
import New.Manager.VertragManager;
import New.Objekte.Intervall;
import New.Objekte.Mitglieder;
import New.Objekte.MitgliederVertrag;
import New.Objekte.Ort;
import New.Objekte.Vertrag;

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
                    vertragSuche();
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

    private void vertragSuche() {
        System.out.print("Bitte Suchbegriff eingeben: ");
        String suchbegriff = scanner.nextLine();
        try {
            VertragManager manager = new VertragManager();
            List<MitgliederVertrag> ergebnis = manager.search(suchbegriff);

            if (!ergebnis.isEmpty()) {
                System.out.println("Keine Einträge gefunden.");
            } else {
                System.out.printf("%-8s| %-15s| %-15s| %-12s| %-6s%n", "Mitgl.ID", "Vorname", "Nachname",
                        "Geburtsdatum", "Aktiv");
                System.out.println("---------------------------------------------------------------");
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Date heute = new Date();

                for (MitgliederVertrag mv : ergebnis) {
                    Mitglieder mitglied = manager.getMitgliederDAO().findById(mv.getMitgliederID());
                    if (mitglied == null)
                        continue;
                    String gebDatum = "-";
                    if (mitglied.getGeburtstag() != null)
                        gebDatum = sdf.format(mitglied.getGeburtstag());

                    List<MitgliederVertrag> vertraegeFuerMitglied = manager.getMitgliederVertragDAO()
                        .findByMitgliedId(mv.getMitgliederID());
                    boolean hatAktivenVertrag = false;
                    for (MitgliederVertrag v : vertraegeFuerMitglied) {
                        if (v.getVertragsbeginn() != null && v.getVertragsende() != null
                                && heute.compareTo(v.getVertragsbeginn()) >= 0
                                && heute.compareTo(v.getVertragsende()) <= 0) {
                            hatAktivenVertrag = true;
                            break;
                        }
                    }

                    if (hatAktivenVertrag && !mitglied.isAktiv()) {
                        mitglied.setAktiv(true);
                        manager.getMitgliederDAO().update(mitglied);
                    } else if (!hatAktivenVertrag && mitglied.isAktiv()) {
                        mitglied.setAktiv(false);
                        manager.getMitgliederDAO().update(mitglied);
                    }

                    System.out.printf("%-8d| %-15s| %-15s| %-12s| %-6s%n", mitglied.getMitgliederID(),
                            mitglied.getVorname(), mitglied.getNachname(), gebDatum, hatAktivenVertrag ? "X" : "");
                }

                System.out.println(
                        "\nBitte die MitgliederID des gewünschten Eintrags eingeben (oder Enter zum Abbrechen):");
                    String auswahl = scanner.nextLine();
                    if (!auswahl.isBlank()) {
                        try {
                            int mitgliederID = Integer.parseInt(auswahl);
                            Mitglieder ausgewaehlt = null;
                            for (MitgliederVertrag mv : ergebnis) {
                                if (mv.getMitgliederID() == mitgliederID) {
                                    ausgewaehlt = manager.getMitgliederDAO().findById(mitgliederID);
                                    break;
                                }
                            }
                            if (ausgewaehlt != null) {
                                boolean exitDetail = false;
                                int tab = 2;
                                while (!exitDetail) {
                                    System.out.println("\n1 Stammdaten | 2 Mitgliedschaft | 3 Zahlungsdaten | 4 Kurse | 5 Verkauf");
                                    switch (tab) {
                                        case 1: showStammdaten(ausgewaehlt); break;
                                        case 2: showMitgliedschaft(ausgewaehlt, manager); break;
                                        default: System.out.println("(Tab nicht belegt)");
                                    }
                                    System.out.print("\nTab auswählen (1-5), 6: Zurück, 7: Hauptmenü\n");
                                    String tabEingabe = scanner.nextLine();
                                    if (tabEingabe.isBlank()) continue;
                                    if (tabEingabe.equals("6")) {
                                        exitDetail = true;
                                    } else if (tabEingabe.equals("7")) {
                                        return;
                                    } else {
                                        try {
                                            int tabWahl = Integer.parseInt(tabEingabe);
                                            if (tabWahl >= 1 && tabWahl <= 5) {
                                                tab = tabWahl;
                                            }
                                        } catch (NumberFormatException ex) {
                                            System.out.println("Ungültige Tab-Nummer!");
                                        }
                                    }
                                }
                            } else {
                                System.out.println("Kein Mitglied mit der eingegebenen MitgliederID gefunden.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Bitte eine gültige MitgliederID eingeben!");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Fehler bei der Vertragssuche: " + (e.getMessage() != null ? e.getMessage() : e));
                e.printStackTrace();
            }
        }

    public void showMitgliedschaft(Mitglieder ausgewaehlt, VertragManager manager) throws Exception {
        List<MitgliederVertrag> vertraege = manager.getMitgliederVertragDAO().findByMitgliedId(ausgewaehlt.getMitgliederID());
        if (vertraege == null || vertraege.isEmpty()) {
            System.out.println("\nDas Mitglied hat keine Verträge.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date heute = new Date();
        for (MitgliederVertrag mv : vertraege) {
            Vertrag v = manager.getVertragDAO().findById(mv.getVertragID());
            Intervall intervall = manager.getIntervallDAO().findById(mv.getIntervallID());
            double grundpreis = v.getGrundpreis();
            double preisrabatt = mv.getPreisrabatt();
            double wochenpreis = grundpreis - preisrabatt;
            int laufzeit = v.getLaufzeit();
            double gesamtwert = laufzeit * grundpreis;
            int wochenSeitVertragsstart = (int) ((heute.getTime() - mv.getVertragsbeginn().getTime()) / (1000 * 60 * 60 * 24 * 7));
            double gezahlt = 0;
            switch (mv.getIntervallID()) {
                case 1:
                    gezahlt = getWochenBisErster(heute, mv) * wochenpreis;
                    break;
                case 2:
                    gezahlt = getWochenBisLetzterVormonat(mv) * wochenpreis;
                    break;
                case 3:
                    gezahlt = wochenSeitVertragsstart * wochenpreis;
                    break;
                case 4:
                    gezahlt = ((wochenSeitVertragsstart % 2 == 0) ? wochenSeitVertragsstart : (wochenSeitVertragsstart - 1)) * wochenpreis;
                    break;
            }
            double restwert = gesamtwert - gezahlt;
            double jeZahlungsintervall = 0;
            switch (mv.getIntervallID()) {
                case 1:
                case 2:
                    jeZahlungsintervall = wochenpreis * 52.14 / 12.0;
                    break;
                case 3:
                    jeZahlungsintervall = wochenpreis;
                    break;
                case 4:
                    jeZahlungsintervall = wochenpreis * 2;
                    break;
            }
            Date kuendbarBis = new Date(mv.getVertragsende().getTime() - 5L * 7L * 24L * 60L * 60L * 1000L);

            System.out.printf("\nVertragNr.:\t%d\t%s\n", mv.getVertragID(), v.getBezeichnung());
            System.out.printf("Laufzeit:\t%d Wochen Zahlungsintervall: %s\n", laufzeit, intervall.getBezeichnung());
            System.out.println("Trainingsbeginn\t| Vertragsbeginn| Vertragsende \t| Aktiv");
            System.out.printf("%s\t| %s \t| %s\t| %s\n",
                mv.getTrainingsbeginn() != null ? sdf.format(mv.getTrainingsbeginn()) : "-",
                mv.getVertragsbeginn() != null ? sdf.format(mv.getVertragsbeginn()) : "-",
                mv.getVertragsende() != null ? sdf.format(mv.getVertragsende()) : "-",
                (heute.compareTo(mv.getVertragsbeginn()) >= 0 && heute.compareTo(mv.getVertragsende()) <= 0) ? "X" : ""
            );
            System.out.printf("Grundpreis:\t\t%5.2f €\t| Gesamtwert:\t%6.2f €\n", grundpreis, gesamtwert);
            System.out.printf("Sonder-Rabatt:\t\t%5.2f €\t| Gezahlt:\t%6.2f €\n", preisrabatt, gezahlt);
            System.out.printf("Wochenpreis:\t\t%5.2f €\t| Restwert:\t%6.2f €\n", wochenpreis, restwert);
            System.out.printf("Je Zahlungsintervall: \t%5.2f €\t| Kündbar bis: %s %s %s\n",
                jeZahlungsintervall,
                sdf.format(kuendbarBis),
                mv.isGekündigt() ? "Künd.: X" : "",
                mv.isVerlängerung() ? "Verl.: X" : ""
            );
        }
    }

    public int getWochenBisErster(Date today, MitgliederVertrag mv) {
        Datum beginn = new Datum(mv.getVertragsbeginn());
        Datum bis = new Datum(today);
        Datum erster = new Datum(bis.getJahr(), bis.getMonat(), 1);

        if (beginn.isBefore(erster)) {
            long millisekunden = ersterZuDate(erster).getTime() - mv.getVertragsbeginn().getTime();
            int tage = (int) (millisekunden / (1000 * 60 * 60 * 24));
            return Math.max(0, tage / 7);
        } else {
            return 0;
        }
    }

    public int getWochenBisLetzterVormonat(MitgliederVertrag mv) {
        Datum beginn = new Datum(mv.getVertragsbeginn());
        Datum heute = DatumHelper.getAktuellesDatum();
        int jahr = heute.getMonat() == 1 ? heute.getJahr() - 1 : heute.getJahr();
        int monat = heute.getMonat() == 1 ? 12 : heute.getMonat() - 1;
        Calendar cal = Calendar.getInstance();
        cal.set(jahr, monat - 1, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Datum letzter = new Datum(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

        if (beginn.isBefore(letzter)) {
            long millisekunden = letzterZuDate(letzter).getTime() - mv.getVertragsbeginn().getTime();
            int tage = (int) (millisekunden / (1000 * 60 * 60 * 24));
            return Math.max(0, tage / 7);
        } else {
            return 0;
        }
    }

    private Date ersterZuDate(Datum d) {
        Calendar cal = Calendar.getInstance();
        cal.set(d.getJahr(), d.getMonat() - 1, d.getTag(), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date letzterZuDate(Datum d) {
        Calendar cal = Calendar.getInstance();
        cal.set(d.getJahr(), d.getMonat() - 1, d.getTag(), 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
 // ---- Gemeinsame Hilfsmethoden ----
    public void showStammdaten(Mitglieder ausgewaehlt) {
        String vorname = ausgewaehlt.getVorname() != null ? ausgewaehlt.getVorname() : "-";
        String nachname = ausgewaehlt.getNachname() != null ? ausgewaehlt.getNachname() : "-";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String geburtsdatum = (ausgewaehlt.getGeburtstag() != null) ? sdf.format(ausgewaehlt.getGeburtstag()) : "-";
        int alterJahre = ausgewaehlt.getGeburtstag() != null ? ausgewaehlt.berechneAlter() : 0;
        boolean istGeburtstag = false;
        if (ausgewaehlt.getGeburtstag() != null) {
            java.time.LocalDate birthDate;
            if (ausgewaehlt.getGeburtstag() instanceof java.sql.Date) {
                birthDate = ((java.sql.Date)ausgewaehlt.getGeburtstag()).toLocalDate();
            } else {
                birthDate = new java.sql.Date(ausgewaehlt.getGeburtstag().getTime()).toLocalDate();
            }
            java.time.LocalDate today = java.time.LocalDate.now();
            istGeburtstag = today.getMonthValue() == birthDate.getMonthValue()
                && today.getDayOfMonth() == birthDate.getDayOfMonth();
        }
        String geburtstagInfo = istGeburtstag ? "Geburtstag!" : "";
        String strasse = ausgewaehlt.getStrasse() != null ? ausgewaehlt.getStrasse() : "-";
        String hausnr = ausgewaehlt.getHausnr() != null ? ausgewaehlt.getHausnr() : "-";
        Ort ortObj = ausgewaehlt.getOrt();
        String plz = ortObj != null && ortObj.getPLZ() != null ? ortObj.getPLZ() : "-";
        String ort = ortObj != null && ortObj.getOrt() != null ? ortObj.getOrt() : "-";
        String tel = ausgewaehlt.getTelefon() != null ? ausgewaehlt.getTelefon() : "-";
        String mail = ausgewaehlt.getMail() != null ? ausgewaehlt.getMail() : "-";
        System.out.printf(
            "\nName:\t\t%s %s\nGeburtsdatum:\t%s\tAlter: %d Jahre %s\nAdresse:\t%s %s\n\t\t%s %s\nTelefon:\t%s\nMail:\t\t%s\n",
            vorname, nachname, geburtsdatum, alterJahre, geburtstagInfo, strasse, hausnr, plz, ort, tel, mail);
    }

  
}
