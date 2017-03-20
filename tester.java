package comp421_project3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

public class tester {

	public static void main(String[] args) throws SQLException{
		String first_name = "OPTION1_CHECK2";
		String last_name = "option1_check";
		String phone = "111-111-1111";
		String email = "frankwu2000@hotmail.com";
		boolean membership = true;
		String personal_id = "G22345432555"; //unique
		String start_date_str = "2017-02-08";
		String end_date_str = "2017-02-11";
		String room_type = "Double";
		boolean online_reserved = true;
		String payment_type = "Debit";
		
		Option1 op1 = new Option1();
		
		op1.run(first_name,last_name,phone,email,membership,
					personal_id,start_date_str,end_date_str,room_type,
					online_reserved,payment_type);
		
		Connection conn = op1.connect();
		Statement stmt = conn.createStatement();
		
		//generate parking lot
//		for(int i =1 ; i<=30 ; i++){
//			String insert_query = "INSERT INTO parking_lot VALUES ("+i+",'disable');";
//			//System.out.println(insert_query);
//			stmt.executeUpdate(insert_query);
//		}
//		
//		for(int i =31 ; i<=100 ; i++){
//			String insert_query = "INSERT INTO parking_lot VALUES ("+i+",'regular');";
//			stmt.executeUpdate(insert_query);
//		}
		
		//delete parking lot
//		for(int i = 1; i < 100 ; i++){
//			String delete_query = "DELETE FROM parking_lot WHERE parking_number = "+i+";";
//			stmt.executeUpdate(delete_query);
//		}

		stmt.close();
		conn.close();
		
		
		
	}

}
