package DataBase;
import java.sql.DriverManager;
import java.sql.SQLException;

// A class to establish connection with database
public class Connection {
		
	public java.sql.Connection establishConnection(){
		try {
			java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Lineups_Game", "root", "");
			return conn;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			ex.printStackTrace();
		}
            return null;
		
	}
	
}
