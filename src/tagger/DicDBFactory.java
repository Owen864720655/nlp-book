package tagger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DicDBFactory implements DicFactory {

	public static Connection getConnect() {
		try {
			Class.forName("org.sqlite.JDBC");
			String absolute_path_to_sqlite_db = "./dic/words.db";
			//"/usr/local/resin-3.0.25/bin/dic/words.db";
			return DriverManager.getConnection("jdbc:sqlite:"+absolute_path_to_sqlite_db);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public TernarySearchTrie create() {
		TernarySearchTrie dic = new TernarySearchTrie();

		Connection conn = getConnect();

		String sql = "SELECT WORD,PARTSPEECH,FRQ from AI_BASEWORD";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String word = rs.getString(1);
				String pos = rs.getString(2);
				int frq = rs.getInt(3);
				dic.addWord(word, pos,frq);
				dic.n +=frq;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dic;
	}

}
