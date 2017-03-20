package comp421_project3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

//this class provides functionality to find room number by entering guest name
public class Option7 {
	public Option7(){}
	
	//establish connection
	public Connection connect() throws SQLException{
		DriverManager.registerDriver(new org.postgresql.Driver());
		Connection conn = DriverManager.getConnection(
			"jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421",
			"cs421g45","Comp421g45"
		);
		return conn;
	}
	
	public String run(String first_name,String last_name) throws SQLException{
		String return_int = "";
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		
		String find_name_query = "SELECT room_number FROM guestroom WHERE guest_id in (SELECT guest_id FROM guest WHERE first_name = '"+first_name+"' AND last_name = '"+last_name+"' );";
		ResultSet result = stmt.executeQuery(find_name_query);
		while(result.next()){
			return_int += String.valueOf(result.getInt("room_number"));
			return_int += "\n";
		}
		
		stmt.close();
		conn.close();
		return return_int;
	}
	
	
	
}

