package net.insta.server.connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.insta.base.InstaBase;
import net.insta.server.mysql.MySQL;

/**
 * Speichert alle Aktionen die auf Instagram ausgef�hrt werden damit diese nicht
 * unn�tig wiederholt werden!
 * 
 */

public class UserJournal {
	private static boolean createTable = false;

	private static void init() {
		if (!createTable) {
			createTable = true;
			MySQL.createTable("journal", "user_id INT" + ", action INT" + ", mediaCode varchar(30), stamp TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP");
		}
	}

	public static boolean checkAction(User user, JournalAction action, String mediaCode) {
		init();
		try {
			boolean exist = false;
			ResultSet rs = null;
			Statement stmt = MySQL.getStatement();
			rs = stmt.executeQuery(MySQL.select("journal", "*", "user_id='" + user.getID() + "' AND action='"
					+ action.ordinal() + "' AND mediaCode='" + mediaCode + "'"));
			while (rs.next()) {
				exist = true;
			}
			stmt.close();
			
			return exist;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public static void addAction(User user, JournalAction action, String mediaCode) {
		init();
		MySQL.insert("journal", "user_id, action, mediaCode",
				new String[] { String.valueOf(user.getID()), String.valueOf(action.ordinal()), mediaCode });
	}

	public static void deleteAction(User user) {
		init();
		InstaBase.printf("Funktion noch nicht geschrieben UserJournal.deleteAction(); !!!!");
		// Vlt wird diese Funktion nicht ben�tigt also schreibe ich sie falls ich sie
		// brauch
	}

	public enum JournalAction {
		LIKE, COMMENT;
	}
}
