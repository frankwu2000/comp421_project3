package comp421_project3;

import java.sql.Date;
import java.sql.SQLException;

public class tester {

	public static void main(String[] args) throws SQLException{
		String first_name = "OPTION1_CHECK2";
		String last_name = "option1_check";
		String phone = "111-111-1111";
		String email = "frankwu2000@hotmail.com";
		boolean membership = true;
		String personal_id = "G22345432555"; //unique
		Date start_date = Date.valueOf("2017-02-08");
		Date end_date = Date.valueOf("2017-02-11");
		String room_type = "Double";
		boolean online_reserved = true;
		String payment_type = "Debit";
		
		Option1 op1 = new Option1();
		
		op1.run(first_name,last_name,phone,email,membership,
					personal_id,start_date,end_date,room_type,
					online_reserved,payment_type);
		
		
	}

}
