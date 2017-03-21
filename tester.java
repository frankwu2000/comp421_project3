package comp421_project3;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

//author : Frank Wu


public class tester {

	public static void main(String[] args) throws SQLException{
		
//------------------------test frank code------------------------------------------------------
		String first_name = "OPTION1_CHECK2";
		String last_name = "option1_check333";
		String phone = "111-111-1111";
		String email = "frankwu2000@hotmail.com";
		boolean membership = true;
		String personal_id = "G55555533"; //unique
		String start_date_str = "2017-03-03";
		String end_date_str = "2017-03-05";
		String room_type = "Twin";
		boolean online_reserved = true;
		String payment_type = "Debit";
		
		
//		System.out.println(Option1.run(first_name,last_name,phone,email,membership,
//				personal_id,start_date_str,end_date_str,room_type,
//				online_reserved,payment_type));
		

		Option7 op7 = new Option7();
		System.out.println(Option7.run(first_name, last_name));
		
//------------------------test Mia code-------------------------------------------------------
		
//		//Class.forName("com.ibm.db2.jcc.")
//		Date today = Date.valueOf("2017-02-09");
//		System.out.println(Mia.checkIn("L9656-6442049-59", today));
//		System.out.println(Mia.cancelReservation("00000002"));
		//Date originalDep = Date.valueOf("2017-02-04");
		//System.out.println(Mia.extendDate(206, originalDep, 4));
		
//------------------------test Chelsea code-------------------------------------------------- 
//		Date d = Date.valueOf("2017-01-31");
//		System.out.println(Chelsea.checkOut(207, d));
		
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
