package comp421_project3;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

//author : Frank Wu


public class tester {

	public static void main(String[] args) throws SQLException{
		
//------------------------test frank code------------------------------------
		String first_name = "Chelsea";
		String last_name = "Mia";
		String phone = "111-111-1111";
		String email = "frankwu2000@hotmail.com";
		boolean membership = true;
		String personal_id = "G22345432115"; //unique
		String start_date_str = "2017-02-07";
		String end_date_str = "2017-02-10";
		String room_type = "Double";
		boolean online_reserved = true;
		String payment_type = "Debit";
		
		
		
		
//		Option1.run(first_name,last_name,phone,email,membership,
//		personal_id,start_date_str,end_date_str,room_type,
//		online_reserved,payment_type);
		

//		Option7 op7 = new Option7();
//		System.out.println(Option7.run(first_name, last_name));
		
//------------------------test Mia code------------------------------------
		
//		//Class.forName("com.ibm.db2.jcc.")
//		//Date today = Date.valueOf("2017-02-07");
//		//System.out.println(checkIn("W2138-7750741-38", today));
//		//cancelReservation("00000002");
//		Date originalDep = Date.valueOf("2017-02-06");
//		System.out.println(extendDate(111, originalDep, 2));
		
//------------------------test Chelsea code------------------------------------ 
	
		
		//generate parking lot		
//		Connection conn = op1.connect();
//		Statement stmt = conn.createStatement();
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
//		stmt.close();
//		conn.close();
		
	}

}
