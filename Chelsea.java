package comp421_project3;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

public class Chelsea {
//	public static void main(String args[]) throws SQLException{
//		//Class.forName("com.ibm.db2.jcc.")
//		/*DriverManager.registerDriver(new org.postgresql.Driver());
//		Connection conn = DriverManager.getConnection(
//				"jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421",
//				"cs421g45","Comp421g45"
//				);
//		Statement stmt = conn.createStatement();
//		
//		//String sqlString = "INSERT INTO cs421g45.guest VALUES ('15', 'auggi2', 'cat2', '514-560-9226', 'auggi@hello2', FALSE , 'jelkj22222')" ;
//		//stmt.executeUpdate(sqlString);
//		System.out.println("connected");
//		conn.close();*/
//		
//		//java.sql.Timestamp t = java.sql.Timestamp.valueOf("2017-02-11 17:10:59.598000");
//		Date d1 = Date.valueOf("2017-02-04");
//		Date d2 = Date.valueOf("2017-02-09");
//		//System.out.println(d1);
//		//System.out.println(checkOut(207, d2));
//		System.out.println(changeRoom(211,d1,d2));
//		//test(d1,d2);
//	}
	
	public static Connection connect() throws SQLException{
		DriverManager.registerDriver(new org.postgresql.Driver());
		Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421",
				"cs421g45","Comp421g45"
				);
		return conn;
	}
	
	public static String checkOut(int roomNo, Date date) throws SQLException{
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		String string="";
		
		String check = "SELECT *  FROM reservation WHERE room_number =" + roomNo+"AND depature_date::date = date '"+ date.toString() + "'";
		ResultSet rs = stmt.executeQuery(check);
		int c=0;
		while (rs.next()){
			c++;
		}
		if (c==0)
			string="Invalid combination of room number and departure date!";
		else{
		
		String sql_rnum = "SELECT * FROM reservation WHERE room_number =" + roomNo;
		rs = stmt.executeQuery(sql_rnum);
		String r_num="";
		
		while (rs.next()){
			Date d = new Date(rs.getTimestamp("depature_date").getTime());
			if(d.toString().compareTo(date.toString())==0)
				r_num = rs.getString("reservation_number");
		}
		rs.close();
		
		String sql_bill = "SELECT * FROM bill WHERE reservation_number = '" + r_num + "'"; 
		ResultSet rs1 = stmt.executeQuery(sql_bill);
		int totalAmount = 0;
		String out_date = "";
		int p_num=0;
		int room_num=0;
		int p_amount=0;
		int r_amount=0;
		while (rs1.next()){
			String bill_id = rs1.getString("bill_id");
			int amount = rs1.getInt("amount");
			out_date = rs1.getTimestamp("date").toString();
			totalAmount += amount;
			
			Statement stmt1 = conn.createStatement();
			String sql2 = "SELECT * FROM parking_bill WHERE bill_id = '" + bill_id + "'"; 
			ResultSet rs2 = stmt1.executeQuery(sql2);
			
			while (rs2.next())
				p_num = rs2.getInt("parking_number");
				
			if(p_num>0 && p_amount==0)
				p_amount = amount;
			
			
			Statement stmt2 = conn.createStatement();
			String sql3 = "SELECT * FROM room_bill WHERE bill_id = '" + bill_id + "'"; 
			ResultSet rs3 = stmt2.executeQuery(sql3);
			
			while (rs3.next())
				room_num = rs3.getInt("room_number");

			if(room_num>0 && r_amount==0)
				r_amount = amount;

			rs2.close();
			rs3.close();
			stmt1.close();
			stmt2.close();
		}

		string = "Reservation number: "+r_num+"\n"+"Check-out date: "+out_date+"\n"+"Total amount: "+totalAmount+"\n"
				+"Room bill: "+r_amount+"\n"+"Parking bill: "+p_amount;
		//System.out.println("Reservation number: "+r_num+"\n"+"Check-out date: "+out_date+"\n"+"Total amount: "+totalAmount+"\n"
		//+"Room bill: "+r_amount+"\n"+"Parking bill: "+p_amount);
		
		String sql4 = "UPDATE guestroom SET guest_id = null WHERE room_number = " + roomNo;
		stmt.executeUpdate(sql4);
		
		rs1.close();
		stmt.close();
		conn.close();
		}
		
		return string;
	}

	
	//check room calendar to find empty room with certain type
	public static String check_calendar_op1(Date start_date , Date end_date , String room_type, String old_room_number) throws SQLException{
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
		}
		
		//now check overall_Date to find intersection of room
	
		
		stmt.close();
		conn.close();
		
		return find_intersection_op1(overall_date, old_room_number);
	}
	
	
	//helper method for check_calendar
	public static String find_intersection_op1(ArrayList<Vector<String>> overall_date, String old_room_number){
		for(int i = 0 ; i<overall_date.get(0).size();i++){
				int days = 0;
				String room = new String(overall_date.get(0).get(i));
				for(int j = 0 ; j < overall_date.size();j++){
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
	

	//roomNo, start, end
	public static String changeRoom(int roomNo, Date start, Date end) throws SQLException{
		
		//Frank Edit: add one day to end since my find room exclude end day
		LocalDate temp_end = end.toLocalDate();
		temp_end = temp_end.plusDays(1);
		end = Date.valueOf(temp_end);
		
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		
		String string = "";
	
		String check = "SELECT *  FROM reservation WHERE room_number =" + roomNo+"AND depature_date::date = date '"+ end.toString() + "'"+"AND arrival_date::date = date '"+ start.toString() + "'";
		ResultSet rs = stmt.executeQuery(check);
		int c=0;
		while (rs.next()){
			c++;
		}
		if (c==0)
			string="Invalid combination of room number and dates!";
		
		else{
		String sql = "SELECT * FROM guestroom WHERE room_number =" + roomNo;
		rs = stmt.executeQuery(sql);
		//String r_num="";
		String type="";
		while (rs.next()){
			type = rs.getString("type");
		}
		rs.close();
		
		String newRoom = check_calendar_op1(start, end, type,Integer.toString(roomNo));
		//System.out.println(newRoom);
		if(newRoom.compareTo("no available room")==0)
			string= "No available room!";
		else{
		int newRoomNum = Integer.parseInt(newRoom);
		string="New room is "+newRoomNum;
		
		String sql1 = "SELECT * FROM reservation WHERE room_number =" + roomNo;
		ResultSet rs1 = stmt.executeQuery(sql1);
		String r_num="";
		
		while (rs1.next()){
			Date d = new Date(rs1.getTimestamp("depature_date").getTime());
			if(d.toString().compareTo(end.toString())==0)
				r_num = rs1.getString("reservation_number");
		}
		rs1.close();
		
		//System.out.println(r_num);
		//System.out.println(newRoomNum);
		
		//changed
		String sql2 = "UPDATE reservation SET room_number ="+newRoomNum+" WHERE reservation_number = '" + r_num + "'";
		stmt.executeUpdate(sql2);
		
		String sql4 = "UPDATE calendar SET reservation_number = null WHERE reservation_number = '" + r_num+"'";
		stmt.executeUpdate(sql4);
		
		//change
		String sql5 = "SELECT * FROM calendar WHERE room_number =" + newRoomNum;
		ResultSet rs2 = stmt.executeQuery(sql5);
		while (rs2.next()){
			Date d = new Date(rs2.getTimestamp("date").getTime());
			//System.out.println("date is " + d);
			if((d.toString().compareTo(start.toString())>0 && d.toString().compareTo(end.toString())<0) ||d.toString().compareTo(start.toString())==0||d.toString().compareTo(end.toString())==0 ){	
				Statement stmt1 = conn.createStatement();
				String sql6 = "UPDATE calendar SET reservation_number = '"+r_num+"' WHERE date::date = date '"+d.toString()+"' AND room_number ="+newRoomNum;
				stmt1.executeUpdate(sql6);	
			}		
		}	
		}
		}
		return string;	
	}
	
	public static void test(Date start, Date end) throws SQLException{
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		
		String sql5 = "SELECT * FROM calendar WHERE room_number =" + 213;
		ResultSet rs2 = stmt.executeQuery(sql5);
		while (rs2.next()){
			Date d = new Date(rs2.getTimestamp("date").getTime());
			//System.out.println(d);
			if((d.toString().compareTo(start.toString())>0 && d.toString().compareTo(end.toString())<0) ||d.toString().compareTo(start.toString())==0||d.toString().compareTo(end.toString())==0 ){
				System.out.println(d.toString());
				/*Statement stmt1 = conn.createStatement();
				String sql6 = "UPDATE calendar SET reservation_number = '"+r_num+"'";
				stmt1.executeUpdate(sql6);	*/
			}		
		}	
	}
	
	
}

