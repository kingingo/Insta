package net.insta.server.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.insta.base.utils.Callback;
import net.insta.base.utils.TimeSpan;

public class MySQL {
	private static String user = "";
	private static String pass = "";
	private static String host = "";
	private static String db = "";
	private static Connection connection; 
	private static long lastRequest;

	public static boolean connect(String user, String pass, String host, String db) {
		if (connection == null) {
			MySQL.user = user;
			MySQL.pass = pass;
			MySQL.host = host;
			MySQL.db = db;
			connect();
			return true;
		}
		return false;
	}

	public static void close() {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void delete(String table, String where) {
		Update("DELETE FROM `" + table + "`" + (where != null ? " WHERE " + where : ""));
	}

	public static void insert(String table, String content, String... values) {
		String value = "";
		for(String v : values) value+="'"+v+"',";
		value = value.substring(0, value.length()-1);
		insert(table, content, value);
	}
	
	public static void insert(String table, String content, String values) {
		Update("INSERT INTO " + table + " (" + content + ") VALUES (" + values + ");");
	}

	public static String select(String table, String select, String where) {
		return "SELECT " + select + " FROM `" + table + "`" + (where != null ? " WHERE " + where : "")+";";
	}

	public static void update(String table, String set, String where) {
		Update("UPDATE " + table + " SET " + set + "" + (where != null ? " WHERE " + where : ""));
	}

	public static void createTable(String table, String content) {
		Update("CREATE TABLE IF NOT EXISTS " + table + " (" + content + ");");
	}

	public static void connect() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db
						+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
						user, pass);
				lastRequest=System.currentTimeMillis();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Statement getStatement() throws SQLException {
		if( (System.currentTimeMillis() - lastRequest) > TimeSpan.HOUR*4 ) {
			close();
			connect();
		}else lastRequest=System.currentTimeMillis();
		
		return connection.createStatement();
	}

	public static boolean Update(String qry) {
		try {
			Statement stmt = getStatement();
			stmt.executeUpdate(qry);
			stmt.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static Double getDouble(String qry) {
		ResultSet rs = null;
		double o = 0.0;
		try {
			Statement stmt = getStatement();
			rs = stmt.executeQuery(qry);
			while (rs.next()) {
				o = rs.getDouble(1);
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return o;
	}

	public static Integer getInt(String qry) {
		ResultSet rs = null;
		Integer o = null;
		try {
			Statement stmt = getStatement();
			rs = stmt.executeQuery(qry);
			while (rs.next()) {
				o = rs.getInt(1);
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return o;
	}

	public static boolean getBoolean(String qry) {
		ResultSet rs = null;
		boolean o = false;
		try {
			Statement stmt = getStatement();
			rs = stmt.executeQuery(qry);
			while (rs.next()) {
				o = rs.getBoolean(1);
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return o;
	}

	public static String getString(String qry) {
		ResultSet rs = null;
		String o = "null";
		try {
			Statement stmt = getStatement();
			rs = stmt.executeQuery(qry);
			while (rs.next()) {
				o = rs.getString(1);
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return o;
	}

	public static Long getLong(String qry) {
		ResultSet rs = null;
		Long o = null;
		try {
			Statement stmt = getStatement();
			rs = stmt.executeQuery(qry);
			while (rs.next()) {
				o = rs.getLong(1);
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return o;
	}

	public static Object getObject(String qry) {
		ResultSet rs = null;
		Object o = null;
		try {
			Statement stmt = getStatement();
			rs = stmt.executeQuery(qry);
			while (rs.next()) {
				o = rs.getObject(1);
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return o;
	}

	public static void asyncQuery(final String qry, Callback<ResultSet> callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Statement stmt = getStatement();
					callback.run(stmt.executeQuery(qry));
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}

	public static ResultSet Query(String qry) {
		ResultSet rs = null;
		try {
			Statement stmt = getStatement();
			rs = stmt.executeQuery(qry);
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rs;
	}
}
