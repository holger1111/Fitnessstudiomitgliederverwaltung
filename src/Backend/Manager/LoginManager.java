package Backend.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Backend.Objekte.Benutzer;

public class LoginManager {
    private final Connection connection;

    public LoginManager(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gibt bei erfolgreichem Login ein Benutzer-Objekt zurück mit RolleID.
     * Null bei Fehler oder falschem Login.
     */
    public Benutzer login(String benutzername, String passwort) throws SQLException {
        String sql = "SELECT BenutzerID, Passwort, RolleID FROM Benutzer WHERE Benutzername = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, benutzername);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String gespeichertesPasswort = rs.getString("Passwort");
                    if (passwort != null && passwort.equals(gespeichertesPasswort)) {
                        Benutzer user = new Benutzer();
                        user.setBenutzerID(rs.getInt("BenutzerID"));
                        user.setBenutzername(benutzername);
                        user.setPasswort(gespeichertesPasswort);
                        user.setRolleID(rs.getInt("RolleID"));
                        return user;
                    }
                }
            }
        }
        return null;
    }
}
