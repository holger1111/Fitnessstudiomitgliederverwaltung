package Backend.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Backend.DAOs.ConnectionDB;
import Backend.DAOs.KursDAO;
import Backend.DAOs.KursleitungDAO;
import Backend.DAOs.KursteilnahmeDAO;
import Backend.DAOs.KursterminDAO;
import Backend.DAOs.MitarbeiterDAO;
import Backend.DAOs.MitgliederDAO;
import Backend.Exception.ConnectionException;
import Backend.Exception.IntException;
import Backend.Exception.NotFoundException;
import Backend.Objekte.Kurs;
import Backend.Objekte.Kursleitung;
import Backend.Objekte.Kursteilnahme;
import Backend.Objekte.Kurstermin;
import Backend.Objekte.Mitarbeiter;
import Backend.Objekte.Mitglieder;

public class KursManager extends BaseManager<Kurs> {
    private final KursDAO kursDAO;
    private final KursterminDAO kursterminDAO;
    private final KursleitungDAO kursleitungDAO;
    private final KursteilnahmeDAO kursteilnahmeDAO;
    private final MitarbeiterDAO mitarbeiterDAO;
    private final MitgliederDAO mitgliederDAO;

    public KursManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
        kursDAO = new KursDAO(connection);
        kursterminDAO = new KursterminDAO(connection);
        kursleitungDAO = new KursleitungDAO(connection);
        kursteilnahmeDAO = new KursteilnahmeDAO(connection);
        mitarbeiterDAO = new MitarbeiterDAO(connection);
        mitgliederDAO = new MitgliederDAO(connection);
    }

    public KursDAO getKursDAO() {
        return kursDAO;
    }

    public KursterminDAO getKursterminDAO() {
        return kursterminDAO;
    }

    public KursleitungDAO getKursleitungDAO() {
        return kursleitungDAO;
    }

    public KursteilnahmeDAO getKursteilnahmeDAO() {
        return kursteilnahmeDAO;
    }

    public MitarbeiterDAO getMitarbeiterDAO() {
        return mitarbeiterDAO;
    }

    public MitgliederDAO getMitgliederDAO() {
        return mitgliederDAO;
    }

    @Override
    public void process() {
        // Implementiere hier spezifische Verarbeitung für Kurse (falls benötigt)
    }

    public Kurs findById(int kursID) throws NotFoundException, IntException, SQLException {
        Kurs kurs = kursDAO.findById(kursID);
        if (kurs == null) {
            throw new NotFoundException("Kurs mit ID " + kursID + " nicht gefunden.");
        }
        return kurs;
    }

    /**
     * Suchfunktion für Kurse, Trainer, Termine
     */
    public List<Kurs> search(String searchTerm) throws SQLException, IntException, NotFoundException {
        List<Kurs> result = kursDAO.searchAllAttributes(searchTerm);

        // Suche auch in Kursterminen und Kursleitungen nach zugehörigen Kursen
        List<Kurstermin> termine = kursterminDAO.searchAllAttributes(searchTerm);
        for (Kurstermin termin : termine) {
            Kurs kurs = kursDAO.findById(termin.getKursID());
            if (kurs != null && !result.contains(kurs)) {
                result.add(kurs);
            }
        }
        List<Kursleitung> leitungen = kursleitungDAO.searchAllAttributes(searchTerm);
        for (Kursleitung kl : leitungen) {
            Kurstermin termin = kursterminDAO.findById(kl.getKursterminID());
            if (termin != null) {
                Kurs kurs = kursDAO.findById(termin.getKursID());
                if (kurs != null && !result.contains(kurs)) {
                    result.add(kurs);
                }
            }
        }
        return result;
    }

    /**
     * Findet alle Kurstermine eines Kurses
     */
    public List<Kurstermin> findTermineByKursId(int kursID) throws SQLException {
        return kursterminDAO.findByKursId(kursID);
    }

    /**
     * Findet alle Kursleitungen für einen Mitarbeiter (Trainer)
     */
    public List<Kursleitung> findLeitungenByMitarbeiterId(int mitarbeiterID) throws SQLException {
        return kursleitungDAO.findByMitarbeiterID(mitarbeiterID);
    }

    /**
     * Findet alle Kursteilnahmen eines Mitglieds
     */
    public List<Kursteilnahme> findTeilnahmenByMitgliedId(int mitgliederID) throws SQLException {
        return kursteilnahmeDAO.findByMitgliederId(mitgliederID);
    }

    /**
     * Findet alle Kursteilnahmen eines Kurstermins
     */
    public List<Kursteilnahme> findTeilnahmenByKursterminId(int kursterminID) throws SQLException {
        return kursteilnahmeDAO.findByKursterminId(kursterminID);
    }

    /**
     * Meldet ein Mitglied für einen Kurstermin an
     */
    public void meldeAnFuerKurs(int mitgliederID, int kursterminID)
            throws SQLException, IntException, NotFoundException {
        Mitglieder mitglied = mitgliederDAO.findById(mitgliederID);
        if (mitglied == null) {
            throw new NotFoundException("Mitglied mit ID " + mitgliederID + " nicht gefunden.");
        }
        Kurstermin termin = kursterminDAO.findById(kursterminID);
        if (termin == null) {
            throw new NotFoundException("Kurstermin mit ID " + kursterminID + " nicht gefunden.");
        }
        try {
            Kursteilnahme existingTeilnahme = kursteilnahmeDAO.findByCompositeKey(mitgliederID, kursterminID);
            if (existingTeilnahme != null) {
                System.out.println("Mitglied ist bereits für diesen Kurstermin angemeldet.");
                return;
            }
        } catch (NotFoundException e) {
            // Teilnahme existiert nicht - das ist OK
        }
        Kursteilnahme teilnahme = new Kursteilnahme();
        teilnahme.setMitgliederID(mitgliederID);
        teilnahme.setKursterminID(kursterminID);
        teilnahme.setAngemeldet(true);
        teilnahme.setAnmeldezeit(new java.sql.Timestamp(System.currentTimeMillis()));
        teilnahme.setAbgemeldet(false);
        teilnahme.setAbmeldezeit(null);
        teilnahme.setAktiv(true);
        teilnahme.setKommentar(null);
        kursteilnahmeDAO.insert(teilnahme);
    }

    /**
     * Meldet ein Mitglied von einem Kurstermin ab
     */
    public void meldeAbVonKurs(int mitgliederID, int kursterminID) throws SQLException, NotFoundException {
        Kursteilnahme teilnahme = kursteilnahmeDAO.findByCompositeKey(mitgliederID, kursterminID);
        if (teilnahme == null) {
            throw new NotFoundException("Keine Anmeldung für diesen Kurstermin gefunden.");
        }
        kursteilnahmeDAO.deleteByCompositeKey(mitgliederID, kursterminID);
    }

    /**
     * Zeigt alle Kursteilnehmer eines Kurstermins an
     * Holt den Trainernamen über die Kursleitung und Mitarbeiter-DAO
     */
    public void zeigeKursteilnehmer(int kursterminID) throws SQLException, IntException, NotFoundException {
        Kurstermin termin = kursterminDAO.findById(kursterminID);
        if (termin == null) {
            System.out.println("Kurstermin nicht gefunden!");
            return;
        }
        Kurs kurs = kursDAO.findById(termin.getKursID());
        Kursleitung kl = kursleitungDAO.findByKursterminId(kursterminID);
        Mitarbeiter trainer = (kl != null) ? mitarbeiterDAO.findById(kl.getMitarbeiterID()) : null;

        // Formatiere Datum/Zeit
        String terminDatumZeit = "-";
        if (termin.getTermin() != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(termin.getTermin());
            int tag = cal.get(java.util.Calendar.DAY_OF_MONTH);
            int monat = cal.get(java.util.Calendar.MONTH) + 1;
            int jahr = cal.get(java.util.Calendar.YEAR);
            int stunde = cal.get(java.util.Calendar.HOUR_OF_DAY);
            int minute = cal.get(java.util.Calendar.MINUTE);
            terminDatumZeit = String.format("%02d.%02d.%4d %02d:%02d", tag, monat, jahr, stunde, minute);
        }

        System.out.println("\n=== Kursteilnehmer ===");
        System.out.println("Kurs: " + (kurs != null ? kurs.getBezeichnung() : "-"));
        System.out.println("Trainer: " + (trainer != null ? trainer.getVorname() + " " + trainer.getNachname() : "-"));
        System.out.println("Termin: " + terminDatumZeit);
        System.out.println();

        List<Kursteilnahme> teilnahmen = kursteilnahmeDAO.findByKursterminId(kursterminID);

        if (teilnahmen.isEmpty()) {
            System.out.println("Keine Teilnehmer angemeldet.");
            return;
        }
        System.out.printf("%-12s | %-20s | %-20s%n", "MitgliederID", "Vorname", "Nachname");
        System.out.println("-".repeat(60));
        for (Kursteilnahme teilnahme : teilnahmen) {
            Mitglieder mitglied = mitgliederDAO.findById(teilnahme.getMitgliederID());
            if (mitglied != null) {
                System.out.printf("%-12d | %-20s | %-20s%n",
                        mitglied.getMitgliederID(), mitglied.getVorname(), mitglied.getNachname());
            }
        }
    }
}
