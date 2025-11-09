package Backend.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Backend.DAOs.ConnectionDB;
import Backend.DAOs.IntervallDAO;
import Backend.DAOs.MitgliederDAO;
import Backend.DAOs.MitgliederVertragDAO;
import Backend.DAOs.VertragDAO;
import Backend.DAOs.ZahlungDAO;
import Backend.Exception.ConnectionException;
import Backend.Exception.IntException;
import Backend.Exception.NotFoundException;
import Backend.Objekte.Intervall;
import Backend.Objekte.MitgliederVertrag;
import Backend.Objekte.Vertrag;
import Backend.Objekte.Zahlung;

public class VertragManager extends BaseManager<MitgliederVertrag> {

    private final MitgliederVertragDAO mitgliederVertragDAO;
    private final VertragDAO vertragDAO;
    private final ZahlungDAO zahlungDAO;
    private final IntervallDAO intervallDAO;
    private final MitgliederDAO mitgliederDAO;

    public VertragManager() throws ConnectionException, SQLException {
        Connection connection = ConnectionDB.getConnection();
        mitgliederVertragDAO = new MitgliederVertragDAO(connection);
        vertragDAO = new VertragDAO(connection);
        zahlungDAO = new ZahlungDAO(connection);
        intervallDAO = new IntervallDAO(connection);
        mitgliederDAO = new MitgliederDAO(connection);
    }

    public MitgliederVertragDAO getMitgliederVertragDAO() {
        return mitgliederVertragDAO;
    }

    public VertragDAO getVertragDAO() {
        return vertragDAO;
    }

    public ZahlungDAO getZahlungDAO() {
        return zahlungDAO;
    }

    public IntervallDAO getIntervallDAO() {
        return intervallDAO;
    }

    public MitgliederDAO getMitgliederDAO() {
        return mitgliederDAO;
    }
    
    @Override
    public void add(MitgliederVertrag item) {
        super.add(item);
    }

    @Override
    public void remove(MitgliederVertrag item) {
        super.remove(item);
    }

    @Override
    public List<MitgliederVertrag> getAll() {
        return super.getAll();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void process() {
    }

    public MitgliederVertrag findById(int vertragNr) throws NotFoundException, IntException, SQLException {
        MitgliederVertrag mv = mitgliederVertragDAO.findById(vertragNr);
        if (mv == null) {
            throw new NotFoundException("MitgliederVertrag mit Nummer " + vertragNr + " nicht gefunden.");
        }
        return mv;
    }

    /**
     * Gemeinsame Suche nach einem Suchbegriff über MitgliederVertrag, Vertrag, Zahlung, Intervall.
     * @param searchTerm Suchbegriff
     * @return Liste gefundener MitgliederVerträge
     */
    public List<MitgliederVertrag> search(String searchTerm) throws SQLException, IntException {
        List<MitgliederVertrag> result = new ArrayList<>();

        List<MitgliederVertrag> vertragsListe = mitgliederVertragDAO.searchAllAttributes(searchTerm);
        if (vertragsListe != null) {
            result.addAll(vertragsListe);
        }

        List<Vertrag> vertragResult = vertragDAO.searchAllAttributes(searchTerm);
        if (vertragResult != null) {
            for (Vertrag v : vertragResult) {
                List<MitgliederVertrag> mvByVertrag = mitgliederVertragDAO.findByVertragId(v.getVertragID());
                for (MitgliederVertrag mv : mvByVertrag) {
                    if (!result.contains(mv)) {
                        result.add(mv);
                    }
                }
            }
        }

        List<Zahlung> zahlungResult = zahlungDAO.searchAllAttributes(searchTerm);
        if (zahlungResult != null) {
            for (Zahlung z : zahlungResult) {
                List<MitgliederVertrag> mvByZahlung = mitgliederVertragDAO.findByZahlungId(z.getZahlungID());
                for (MitgliederVertrag mv : mvByZahlung) {
                    if (!result.contains(mv)) {
                        result.add(mv);
                    }
                }
            }
        }

        List<Intervall> intervallResult = intervallDAO.searchAllAttributes(searchTerm);
        if (intervallResult != null) {
            for (Intervall i : intervallResult) {
                List<MitgliederVertrag> mvByIntervall = mitgliederVertragDAO.findByIntervallId(i.getIntervallID());
                for (MitgliederVertrag mv : mvByIntervall) {
                    if (!result.contains(mv)) {
                        result.add(mv);
                    }
                }
            }
        }

        return result;
    }
}
