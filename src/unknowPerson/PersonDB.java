package unknowPerson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDB {

	public static void addNr(Connection con) throws SQLException {
		String sql = "select term,type from nr";

		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String word = rs.getString("term");
			String type = rs.getString("type");
		}
	}

}
