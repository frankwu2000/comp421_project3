package comp421_project3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

public class Option1 {
	
		//constructor
		Option1(){}

		//establish connection
		public  Connection connect() throws SQLException{
			DriverManager.registerDriver(new org.postgresql.Driver());
			Connection conn = DriverManager.getConnection(
					"jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421",
					"cs421g45","Comp421g45"
			);
			return conn;
		}
		
		
		//option1 - add new guest
		public void run(String first_name,String last_name,String phone, String email, boolean membership,String personal_id,String start_date_input_str, String end_date_input_str, String room_type, boolean online_reserved, String payment_type )throws SQLException{
			//check if the person was guest or not
			Connection conn = connect();
			Statement stmt = conn.createStatement();
			String sqlString = "SELECT guest_id FROM guest WHERE personal_id = '" + personal_id + "';";
			ResultSet result = stmt.executeQuery(sqlString);
			String new_guest_id = "";
			//insert guest table
			if(!result.next() )
			{
				//only insert if this person is a new guest
				//get new guest_id based on current number of guests
				ResultSet guestid_result = stmt.executeQuery("SELECT COUNT(*) FROM guest;");
				if(guestid_result.next()){
					new_guest_id = convert_guest_id_op1(guestid_result.getInt("count") + 1);
				}
				//convert boolean
				String member_ship = "TRUE";
				if(!membership){
					member_ship = "FALSE";
				}
				//insert guest info
				String insert_guest = "INSERT INTO guest VALUES ('" + new_guest_id + "','" + first_name +
						"','" + last_name + "','" + phone + "','" + email + "','" + member_ship + 
						"','"+ personal_id + "');";
				stmt.executeUpdate(insert_guest);
				System.out.println("Insert new guest with guest_id " +new_guest_id+ "!" );
				guestid_result.close();
				
			}else{
				//this person is already a guest!
				System.out.println("this person is a guest already!");
			}
			
			//update room table
			Date start_date = Date.valueOf(start_date_input_str);
			Date end_date = Date.valueOf(end_date_input_str);
			String avail_room = check_calendar_op1(start_date,end_date,room_type,"");
			//if no available room, terminate
			if(avail_room.equals("no available room")){
				System.out.println("no available room!");
				return;
			}
			String room_update_query = "UPDATE guestroom SET guest_id = '"+new_guest_id+"' WHERE room_number = "+avail_room+";";
			stmt.executeUpdate(room_update_query);
			System.out.println("Update room table, room number " +avail_room+ "!" );
			
			//insert reservation table
			//get new reservation_number based on largest reservation number
			ResultSet reserv_num_result = stmt.executeQuery("SELECT reservation_number FROM reservation WHERE reservation_number >= ALL (SELECT reservation_number FROM reservation );");
			String new_reserv_num = "";
			if(reserv_num_result.next()){
				new_reserv_num = convert_reserv_num_op1(Integer.valueOf(reserv_num_result.getString("reservation_number")) + 1);
			}
			String start_date_str = start_date.toString().replaceAll("-", "");
			String end_date_str = end_date.toString().replaceAll("-", "");
			String online_reserv_str = "TRUE";
			if(!online_reserved){
				online_reserv_str = "FALSE";
			}
			String reserv_update_query = "INSERT INTO reservation VALUES('"+new_reserv_num+"', date '"+
			start_date_str+"', date '"+end_date_str+"', '"+new_guest_id+
			"', "+avail_room+", "+online_reserv_str+", '"+payment_type+"');";
//			System.out.println(reserv_update_query);
			stmt.executeUpdate(reserv_update_query);
			System.out.println("Insert new reservation with reservation number "+new_reserv_num+"!");
			reserv_num_result.close();
			
			//update calendar
			LocalDate start = start_date.toLocalDate();
			LocalDate end = end_date.toLocalDate();
			LocalDate staydate = start;
			while(staydate.isBefore(end)){
				//update calendar
				String staydate_str = staydate.toString().replaceAll("-", "");
				String calendar_query = "UPDATE calendar SET reservation_number = '"+new_reserv_num+
						"' WHERE room_number = "+avail_room+" AND date = date'"+staydate_str+"';";
				stmt.executeUpdate(calendar_query);
				
				staydate = staydate.plusDays(1);
			}
			System.out.println("Update calendar for room "+avail_room+"!");
			
//			//insert bills and room bills
//			//generate new bill id based on count of bills
//			ResultSet bill_num_result = stmt.executeQuery("SELECT COUNT(*) FROM bill;");
//			String new_bill_id = "";
//			if(bill_num_result.next()){
//				new_bill_id = convert_guest_id_op1(bill_num_result.getInt("count") + 1);
//			}
//			bill_num_result.close();
//			String bill_insert_query = "INSERT INTO bill VALUES ('"+new_bill_id+"', 0, date'"+start_date_str+"'+time '00:00', '"+new_reserv_num+"'); ";
//			stmt.executeUpdate(bill_insert_query);
//			String room_bill_insert_query = "INSERT INTO room_bill VALUES ('"+new_bill_id+"', "+avail_room+"); ";
//			stmt.executeUpdate(room_bill_insert_query);
//			System.out.println("Insert new bill and room bill with bill id "+new_bill_id+"!");
//			
			result.close();
			stmt.close();
			conn.close();
		}
		
		//helper method to convert format of guest_id
		public  String convert_guest_id_op1(int guest_id){
			String str = "";
			String id = String.valueOf(guest_id);
			if(guest_id < 10 ){
				str = "000000" + id;
			}else if(guest_id < 100){
				str = "00000" + id;
			}else if(guest_id < 1000){
				str = "0000" + id;
			}else if(guest_id < 10000){
				str = "000" + id;
			}else if(guest_id < 100000){
				str = "00" + id;
			}else if(guest_id < 1000000){
				str = "0" + id;
			}else if(guest_id < 10000000){
				str = id;
			}
			return str;
		}
		
		
		
		//helper method to convert format of reservation number 
		public  String convert_reserv_num_op1(int reserv_num){
			String str = "";
			String id = String.valueOf(reserv_num);
			if(reserv_num < 10 ){
				str = "0000000" + id;
			}else if(reserv_num < 100){
				str = "000000" + id;
			}else if(reserv_num < 1000){
				str = "00000" + id;
			}else if(reserv_num < 10000){
				str = "0000" + id;
			}else if(reserv_num < 100000){
				str = "000" + id;
			}else if(reserv_num < 1000000){
				str = "00" + id;
			}else if(reserv_num < 10000000){
				str = "0"+id;
			}else if(reserv_num < 100000000){
				str = id;
			}
			return str;
		}
		
		//check room calendar to find empty room with certain type within certain date interval
		public  String check_calendar_op1(Date start_date , Date end_date , String room_type, String old_room_number) throws SQLException{
			//build connect and execute query
			Connection conn = connect();
			Statement stmt = conn.createStatement();
			String empty_room_num = "";
			//find all dates the guest need to stay
			LocalDate start = start_date.toLocalDate();
			LocalDate end = end_date.toLocalDate();

			LocalDate stay_date = start;
			ArrayList<Vector<String>> overall_date = new ArrayList<Vector<String>>();
			
			while(stay_date.isBefore(end)){
				Vector<String> rooms_avail_on_certain_date = new Vector<String>();
				Date sql_stay_date = java.sql.Date.valueOf( stay_date );
				String stay_date_string = sql_stay_date.toString();
				stay_date_string += " 00:00:00";
				
				String find_room_query = "SELECT room_number FROM calendar WHERE room_number in (SELECT room_number FROM  guestroom WHERE type = '"+
						room_type+"' )AND date = '"+stay_date_string+"' AND reservation_number ISNULL;" ;
				ResultSet room_result = stmt.executeQuery(find_room_query);
				while(room_result.next()){
					empty_room_num = room_result.getString("room_number");
					if(empty_room_num!=""){
						rooms_avail_on_certain_date.add(empty_room_num);
					}
				}
				overall_date.add(rooms_avail_on_certain_date);	
				
				stay_date = stay_date.plusDays(1);
				room_result.close();
			}
			
			//now check overall_Date to find intersection of room
		
			
			stmt.close();
			conn.close();
			
			return find_intersection_op1(overall_date, old_room_number);
		}
		
		
		//helper method for check_calendar
		public  String find_intersection_op1(ArrayList<Vector<String>> overall_date, String old_room_number){
			
			for(int i = 0 ; i<overall_date.get(0).size();i++){
					int days = 0;
					String room = new String(overall_date.get(0).get(i));
					for(int j = 0 ; j < overall_date.size();j++){
						//no old room, the string is ""
						if(old_room_number.compareTo("")==0){
							old_room_number = room+"1"; 
						}
						if(overall_date.get(j).contains(room) && room.compareTo(old_room_number)!=0){
							days++;
						}
					}
					if(days == overall_date.size()){
						return room;
					}
					
				}
			return "no available room";
		}
}
