package Backend.Service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

import Backend.Manager.KursManager;
import Backend.Manager.MitgliederManager;
import Backend.Manager.RolleManager;
import Backend.Manager.VerkaufManager;
import Backend.Manager.VertragManager;
import Backend.Objekte.Artikel;
import Backend.Objekte.ArtikelBestellung;
import Backend.Objekte.Bestellung;
import Backend.Objekte.Kurs;
import Backend.Objekte.Kursleitung;
import Backend.Objekte.Kursteilnahme;
import Backend.Objekte.Kurstermin;
import Backend.Objekte.Mitarbeiter;
import Backend.Objekte.Mitglieder;
import Backend.Objekte.MitgliederVertrag;

public class SucheService extends BaseService {

    private RolleManager rolleManager;
    private int aktuelleRolleId;

    public SucheService(Connection connection, Scanner scanner, RolleManager rolleManager, int aktuelleRolleId) {
        super(connection, scanner);
        this.rolleManager = rolleManager;
        this.aktuelleRolleId = aktuelleRolleId;
    }

    public void start() {
        if (!rolleManager.hatRecht(aktuelleRolleId, "SUCHEN")) {
            System.out.println("Zugriff verweigert: Kein Suchrecht.");
            return;
        }

        System.out.print("Bitte Suchbegriff eingeben: ");
        String suchbegriff = scanner.nextLine();
        try {
            MitgliederManager mitgliederManager = new MitgliederManager();
            VertragManager vertragManager = new VertragManager();
            VerkaufManager verkaufManager = new VerkaufManager();
            KursManager kursManager = new KursManager();

            List<Mitglieder> mitgliederErgebnis = mitgliederManager.search(suchbegriff);
            List<MitgliederVertrag> vertragErgebnis = vertragManager.search(suchbegriff);
            List<Bestellung> bestellungErgebnis = verkaufManager.getBestellungDAO().searchAllAttributes(suchbegriff);
            List<Artikel> artikelErgebnis = verkaufManager.getArtikelDAO().searchAllAttributes(suchbegriff);

            List<Kurs> kursErgebnis = kursManager.search(suchbegriff);
            List<Mitarbeiter> trainerErgebnis = kursManager.getMitarbeiterDAO().searchAllAttributes(suchbegriff);
            List<Kurstermin> kursterminErgebnis = kursManager.getKursterminDAO().searchAllAttributes(suchbegriff);

            Set<Integer> mitgliederIDs = new HashSet<>();

            for (Mitglieder m : mitgliederErgebnis) mitgliederIDs.add(m.getMitgliederID());
            for (MitgliederVertrag mv : vertragErgebnis) mitgliederIDs.add(mv.getMitgliederID());
            for (Bestellung b : bestellungErgebnis) mitgliederIDs.add(b.getMitgliederID());
            for (Artikel artikel : artikelErgebnis) {
                List<ArtikelBestellung> artikelBestellungen = verkaufManager.getArtikelBestellungDAO().findByArtikelId(artikel.getArtikelID());
                for (ArtikelBestellung ab : artikelBestellungen) {
                    Bestellung bestellung = verkaufManager.getBestellungDAO().findById(ab.getBestellungID());
                    if (bestellung != null) mitgliederIDs.add(bestellung.getMitgliederID());
                }
            }

            for (Kurs kurs : kursErgebnis) {
                List<Kurstermin> termine = kursManager.findTermineByKursId(kurs.getKursID());
                for (Kurstermin termin : termine) {
                    List<Kursteilnahme> teilnahmen = kursManager.findTeilnahmenByKursterminId(termin.getKursterminID());
                    for (Kursteilnahme teilnahme : teilnahmen)
                        mitgliederIDs.add(teilnahme.getMitgliederID());
                }
            }

            for (Mitarbeiter trainer : trainerErgebnis) {
                List<Kursleitung> leitung = kursManager.getKursleitungDAO().findByMitarbeiterID(trainer.getMitarbeiterID());
                for (Kursleitung kl : leitung) {
                    List<Kursteilnahme> teilnahmen = kursManager.findTeilnahmenByKursterminId(kl.getKursterminID());
                    for (Kursteilnahme teilnahme : teilnahmen)
                        mitgliederIDs.add(teilnahme.getMitgliederID());
                }
            }

            for (Kurstermin termin : kursterminErgebnis) {
                List<Kursteilnahme> teilnahmen = kursManager.findTeilnahmenByKursterminId(termin.getKursterminID());
                for (Kursteilnahme teilnahme : teilnahmen)
                    mitgliederIDs.add(teilnahme.getMitgliederID());
            }

            if (mitgliederIDs.isEmpty()) {
                System.out.println("Keine Einträge gefunden.");
                return;
            }

            List<Integer> sortierteIDs = new ArrayList<>(mitgliederIDs);
            sortierteIDs.sort(Integer::compareTo);

            System.out.printf("%-8s| %-15s| %-15s| %-12s| %-6s%n", "Mitgl.ID", "Vorname", "Nachname", "Geburtsdatum", "Aktiv");
            System.out.println("---------------------------------------------------------------");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date heute = new Date();

            for (Integer mitgliedID : sortierteIDs) {
                Mitglieder mitglied = mitgliederManager.getMitgliederDAO().findById(mitgliedID);
                if (mitglied == null) continue;

                String gebDatum = (mitglied.getGeburtstag() != null) ? sdf.format(mitglied.getGeburtstag()) : "-";

                List<MitgliederVertrag> vertraege = vertragManager.getMitgliederVertragDAO().findByMitgliedId(mitgliedID);
                boolean hatAktivenVertrag = false;
                for (MitgliederVertrag v : vertraege) {
                    if (v.getVertragsbeginn() != null && v.getVertragsende() != null
                            && heute.compareTo(v.getVertragsbeginn()) >= 0
                            && heute.compareTo(v.getVertragsende()) <= 0) {
                        hatAktivenVertrag = true;
                        break;
                    }
                }

                if (hatAktivenVertrag && !mitglied.isAktiv()) {
                    mitglied.setAktiv(true);
                    mitgliederManager.getMitgliederDAO().update(mitglied);
                } else if (!hatAktivenVertrag && mitglied.isAktiv()) {
                    mitglied.setAktiv(false);
                    mitgliederManager.getMitgliederDAO().update(mitglied);
                }

                System.out.printf("%-8d| %-15s| %-15s| %-12s| %-6s%n",
                        mitglied.getMitgliederID(), mitglied.getVorname(), mitglied.getNachname(), gebDatum,
                        hatAktivenVertrag ? "X" : "");
            }

            boolean detailExit = false;

            while (!detailExit) {
                System.out.print("\nMitgliederID eingeben für Detailansicht (0 für Ende): ");
                String input = scanner.nextLine();
                if ("0".equals(input)) {
                    detailExit = true;
                } else {
                    try {
                        int id = Integer.parseInt(input);
                        Mitglieder mitglied = mitgliederManager.getMitgliederDAO().findById(id);
                        if (mitglied == null) {
                            System.out.println("Mitglied nicht gefunden.");
                        } else {
                            zeigeMitgliedTabs(mitglied, vertragManager, kursManager, verkaufManager);
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Bitte eine gültige Zahl eingeben.");
                    } catch (Exception e) {
                        System.out.println("Fehler bei Detailanzeige: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Fehler bei der Suche: " + (e.getMessage() != null ? e.getMessage() : e));
            e.printStackTrace();
        }
    }

    private void zeigeMitgliedTabs(Mitglieder mitglied, VertragManager vertragManager, KursManager kursManager, VerkaufManager verkaufManager) throws Exception {
        boolean zurueck = false;

        while (!zurueck) {
            System.out.println("\n=== Mitglieder-Tabs für " + mitglied.getVorname() + " " + mitglied.getNachname() + " ===");
            System.out.println("1 - Stammdaten");
            System.out.println("2 - Vertragsdaten");
            System.out.println("3 - Zahlungsdaten");
            System.out.println("4 - Verkaufsdaten");
            System.out.println("5 - Kurse");
            System.out.println("0 - Zurück");

            System.out.print("Bitte Tab wählen: ");
            String wahl = scanner.nextLine();

            switch (wahl) {
                case "1":
                    zeigeStammdaten(mitglied);
                    break;
                case "2":
                    zeigeVertragsdaten(mitglied, vertragManager);
                    break;
                case "3":
                    zeigeZahlungsdaten(mitglied, verkaufManager);
                    break;
                case "4":
                    zeigeVerkaufsdaten(mitglied, verkaufManager);
                    break;
                case "5":
                    zeigeKursteilnahmen(mitglied, kursManager);
                    break;
                case "0":
                    zurueck = true;
                    break;
                default:
                    System.out.println("Ungültige Eingabe!");
            }
        }
    }

    private void zeigeStammdaten(Mitglieder mitglied) {
        System.out.println("\n--- Stammdaten ---");
        System.out.println("ID: " + mitglied.getMitgliederID());
        System.out.println("Name: " + mitglied.getVorname() + " " + mitglied.getNachname());
        System.out.println("Geburtstag: " + (mitglied.getGeburtstag() != null ? new SimpleDateFormat("dd.MM.yyyy").format(mitglied.getGeburtstag()) : "-"));
        System.out.println("E-Mail: " + (mitglied.getMail() != null ? mitglied.getMail() : "-"));
        System.out.println("Telefon: " + (mitglied.getTelefon() != null ? mitglied.getTelefon() : "-"));
    }

    private void zeigeVertragsdaten(Mitglieder mitglied, VertragManager vertragManager) throws Exception {
        System.out.println("\n--- Vertragsdaten ---");
        List<MitgliederVertrag> vertraege = vertragManager.getMitgliederVertragDAO().findByMitgliedId(mitglied.getMitgliederID());
        if (vertraege.isEmpty()) {
            System.out.println("Keine Verträge vorhanden.");
        } else {
            for (MitgliederVertrag v : vertraege) {
                System.out.println("Vertrag Nr: " + v.getVertragNr() + ", Beginn: " + (v.getVertragsbeginn() != null ? new SimpleDateFormat("dd.MM.yyyy").format(v.getVertragsbeginn()) : "-") +
                        ", Ende: " + (v.getVertragsende() != null ? new SimpleDateFormat("dd.MM.yyyy").format(v.getVertragsende()) : "-") +
                        ", Aktiv: " + (v.isAktiv() ? "Ja" : "Nein"));
            }
        }
    }

    private void zeigeZahlungsdaten(Mitglieder mitglied, VerkaufManager verkaufManager) {
        System.out.println("\n--- Zahlungsdaten ---");
        try {
            List<Bestellung> bestellungen = verkaufManager.getBestellungDAO().findByMitgliederId(mitglied.getMitgliederID());
            if (bestellungen.isEmpty()) {
                System.out.println("Keine Zahlungen vorhanden.");
            } else {
                for (Bestellung bestellung : bestellungen) {
                    System.out.println("Bestellung ID: " + bestellung.getBestellungID() + ", Gesamtpreis: " + bestellung.getGesamtpreis());
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler bei Zahlungsdaten: " + e.getMessage());
        }
    }

    private void zeigeVerkaufsdaten(Mitglieder mitglied, VerkaufManager verkaufManager) {
        System.out.println("\n--- Verkaufsdaten ---");
        // Beispiel, hier einfach bestehende Zahlungsdaten anzeigen:
        zeigeZahlungsdaten(mitglied, verkaufManager);
    }

    private void zeigeKursteilnahmen(Mitglieder mitglied, KursManager kursManager) {
        System.out.println("\n--- Kursteilnahmen ---");
        try {
            List<Kursteilnahme> teilnahmen = kursManager.findTeilnahmenByMitgliedId(mitglied.getMitgliederID());
            if (teilnahmen.isEmpty()) {
                System.out.println("Keine Kursteilnahmen vorhanden.");
            } else {
                for (Kursteilnahme teilnahme : teilnahmen) {
                    Kurstermin termin = kursManager.getKursterminDAO().findById(teilnahme.getKursterminID());
                    Kurs kurs = kursManager.getKursDAO().findById(termin.getKursID());
                    System.out.println(kurs.getBezeichnung() + ", Termin: " + termin.getTermin() + ", Aktiv: " + (teilnahme.isAktiv() ? "Ja" : "Nein"));
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler bei Kursteilnahmen: " + e.getMessage());
        }
    }
}
