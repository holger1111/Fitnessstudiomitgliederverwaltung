package New.DAOs;

import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Objekte.Kursteilnahme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KursteilnahmeDAO extends BaseDAO<Kursteilnahme> {

    public KursteilnahmeDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Kursteilnahme findById(int id) throws SQLException, IntException, NotFoundException {
        // Kursteilnahme hat keinen einzelnen Primary Key, daher nicht direkt umsetzbar
        // Falls du nach MitgliederID suchen willst, nutze findByMitgliederId()
        throw new UnsupportedOperationException("Kursteilnahme hat zusammengesetzten Schlüssel. Nutze spezifische Methoden.");
    }

    @Override
    public void insert(Kursteilnahme entity) throws SQLException {
        String sql = "INSERT INTO Kursteilnahme (MitgliederID, KursterminID, Anmeldbar, Anmeldezeit, Aktiv) " +
                     "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getMitgliederID());
            ps.setInt(2, entity.getKursterminID());
            ps.setBoolean(3, entity.isAnmeldbar());
            ps.setTimestamp(4, entity.getAnmeldezeit());
            ps.setBoolean(5, entity.isAktiv());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void update(Kursteilnahme entity) throws SQLException {
        String sql = "UPDATE Kursteilnahme SET Anmeldbar = ?, Anmeldezeit = ?, Aktiv = ? " +
                     "WHERE MitgliederID = ? AND KursterminID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setBoolean(1, entity.isAnmeldbar());
            ps.setTimestamp(2, entity.getAnmeldezeit());
            ps.setBoolean(3, entity.isAktiv());
            ps.setInt(4, entity.getMitgliederID());
            ps.setInt(5, entity.getKursterminID());
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Kursteilnahme hat zusammengesetzten Schlüssel. Nutze delete(mitgliederID, kursterminID).");
    }

    public void delete(int mitgliederID, int kursterminID) throws SQLException {
        String sql = "DELETE FROM Kursteilnahme WHERE MitgliederID = ? AND KursterminID = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mitgliederID);
            ps.setInt(2, kursterminID);
            ps.executeUpdate();
        } finally {
            closeResources(null, ps);
        }
    }

    public List<Kursteilnahme> findByMitgliederId(int mitgliederID) throws SQLException {
        List<Kursteilnahme> teilnahmen = new ArrayList<>();
        String sql = "SELECT MitgliederID, KursterminID, Anmeldbar, Anmeldezeit, Aktiv " +
                     "FROM Kursteilnahme WHERE MitgliederID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, mitgliederID);
            rs = ps.executeQuery();
            while (rs.next()) {
                teilnahmen.add(mapRowToKursteilnahme(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return teilnahmen;
    }

    public List<Kursteilnahme> findByKursterminId(int kursterminID) throws SQLException {
        List<Kursteilnahme> teilnahmen = new ArrayList<>();
        String sql = "SELECT MitgliederID, KursterminID, Anmeldbar, Anmeldezeit, Aktiv " +
                     "FROM Kursteilnahme WHERE KursterminID = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, kursterminID);
            rs = ps.executeQuery();
            while (rs.next()) {
                teilnahmen.add(mapRowToKursteilnahme(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return teilnahmen;
    }

    public List<Kursteilnahme> findAll() throws SQLException {
        List<Kursteilnahme> teilnahmen = new ArrayList<>();
        String sql = "SELECT MitgliederID, KursterminID, Anmeldbar, Anmeldezeit, Aktiv FROM Kursteilnahme";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                teilnahmen.add(mapRowToKursteilnahme(rs));
            }
        } finally {
            closeResources(rs, ps);
        }
        return teilnahmen;
    }

    private Kursteilnahme mapRowToKursteilnahme(ResultSet rs) throws SQLException {
        Kursteilnahme teilnahme = new Kursteilnahme();
        teilnahme.setMitgliederID(rs.getInt("MitgliederID"));
        teilnahme.setKursterminID(rs.getInt("KursterminID"));
        teilnahme.setAnmeldbar(rs.getBoolean("Anmeldbar"));
        teilnahme.setAnmeldezeit(rs.getTimestamp("Anmeldezeit"));
        teilnahme.setAktiv(rs.getBoolean("Aktiv"));
        return teilnahme;
    }
}
