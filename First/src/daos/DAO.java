package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Function;


public class DAO {
	// SELECT
	public static <T> ArrayList<T> getResults(String sql, Object[] params, Function<ResultSet, T> mapper) {
		ArrayList<T> results = new ArrayList<>();
		try (Connection con = ConnectDB.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					T obj = mapper.apply(rs);
					results.add(obj);
				}
			}
		} catch (SQLException e) {
			System.out.println("Keine Abfrage möglich!");
			e.printStackTrace();
		}
		return results;
	}

// INSERT
	public static int insert(String sql, Object[] params) {
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
			try (PreparedStatement pstmt = con.prepareStatement(sql)) {
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						pstmt.setObject(i + 1, params[i]);
					}
				}
				int result = pstmt.executeUpdate();
				con.commit();
				return result;
			}
			
		} catch (SQLException e) {
			System.out.println("Kein Insert möglich!");
			e.printStackTrace();
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return 0;
			
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// UPDATE
	public static int update(String sql, Object[] params) {
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
			try (PreparedStatement pstmt = con.prepareStatement(sql)) {
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						pstmt.setObject(i + 1, params[i]);
					}
				}
				int result = pstmt.executeUpdate();
				con.commit();
				return result;
			}
			
		} catch (SQLException e) {
			System.out.println("Kein Update möglich!");
			e.printStackTrace();
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return 0;
			
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// DELETE
	public static int delete(String sql, Object[] params) {
		Connection con = null;
		try {
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
			try (PreparedStatement pstmt = con.prepareStatement(sql)) {
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						pstmt.setObject(i + 1, params[i]);
					}
				}
				int result = pstmt.executeUpdate();
				con.commit();
				return result;
			}
			
		} catch (SQLException e) {
			System.out.println("Kein Delete möglich!");
			e.printStackTrace();
			if (con != null) {
				try {
					if (con != null) {
						con.rollback();
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return 0;
			
		} finally {
			if ( con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
