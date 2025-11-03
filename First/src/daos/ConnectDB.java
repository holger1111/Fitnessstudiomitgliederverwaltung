package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	
	private static String url = "jdbc:mysql://localhost:3306/mitgliederverwaltung";
	private static String user = "root";
	private static String password  = "meinPasswort";

	public static Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("Keine Verbindung möglich!");
			e.printStackTrace();
		}
		return con;	// Verbindung bleibt offen, muss später geschlossen werden close()
	}
}