package comp421_project3;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

public class Mia {
//	public static void main(String args[]) throws SQLException{
//		//Class.forName("com.ibm.db2.jcc.")
//		//Date today = Date.valueOf("2017-02-07");
//		//System.out.println(checkIn("W2138-7750741-38", today));
//		//cancelReservation("00000002");
//		Date originalDep = Date.valueOf("2017-02-06");
//		System.out.println(extendDate(111, originalDep, 2));
//		
//	}
	
	
	public static Connection connect() throws SQLException{
		DriverManager.registerDriver(new org.postgresql.Driver());
		Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421",
				"cs421g45","Comp421g45"
				);
		return conn;	
	}
	
	//guest check-in (check room calendar)
	public static String checkIn(String personal_id, Date today) throws SQLException{
		int room_number = 0;
		String guest_id = "";
		// get room number
		String sql1 = "SELECT guest_id FROM guest WHERE personal_id = '" + personal_id + "'";
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		ResultSet guestId = stmt.executeQuery(sql1);
		if(guestId.next()){
			guest_id = guestId.getString("guest_id");
			String sql2 = "SELECT room_number, arrival_date FROM reservation WHERE guest_id = '" + guest_id + "'";
			ResultSet roomNum = stmt.executeQuery(sql2);
			while(roomNum.next()){
				Date date = new Date(roomNum.getTimestamp("arrival_date").getTime());
				if(date.toString().compareTo(today.toString()) == 0)
					room_number = roomNum.getInt("room_number");
			}
			roomNum.close();
		}
		guestId.close();
		if(room_number == 0 || guest_id.compareTo("") == 0)
			return "Guest doesn't have any reservation.";
		
		// set guestId in guest room
		String sql3 = "UPDATE guestroom SET guest_id = '"+ guest_id + "' WHERE room_number = '" + room_number + "'";
		stmt.executeUpdate(sql3);
		
		stmt.close();
		conn.close();
		return String.valueOf(room_number);		
	}
	
	public static String cancelReservation(String reservationNum) throws SQLException{
		// delete info in calendar first then reservation
		String result = "Success";
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		String reservationCheckSql = "SELECT reservation_number FROM reservation WHERE reservation_number = '" + reservationNum + "'";
		ResultSet rs = stmt.executeQuery(reservationCheckSql);
		if(!rs.next())
			return "reservation number doesn't exist!";
		rs.close();
			
		String calendarSql = "UPDATE calendar SET reservation_number = NULL WHERE reservation_number = '" + reservationNum + "'";
		stmt.executeUpdate(calendarSql);
		String reservationSql = "DELETE FROM reservation WHERE reservation_number = '" + reservationNum + "'";
		stmt.executeUpdate(reservationSql);
		stmt.close();
		conn.close();
		return result;
	}
	
	public static String extendDate(int roomNum, Date leave, int duration) throws SQLException{
		boolean Yes = false;
		//String guest_id = "";
		String myReservation = "";
		String otherReservation = "";
		//String type = "";
		Date next = null;
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		
		String reservationSql = "SELECT reservation_number FROM reservation WHERE room_number="+roomNum+" AND depature_date::date = date '"+leave.toString()+"'";
		
		ResultSet reservationNum = stmt.executeQuery(reservationSql);
		if(reservationNum.next())
			myReservation = reservationNum.getString("reservation_number");
		reservationNum.close();
		
		if(myReservation == "")
			return "Invalid Input.";
		
		//System.out.println(myReservation);
		LocalDate start = leave.toLocalDate();
		
		for(int i=0; i<duration; i++){
			start = start.plusDays(1);
			next = Date.valueOf(start);
			//System.out.println("next date " + next);
			
			String calendarSql = "SELECT reservation_number FROM calendar WHERE room_number = " + roomNum + "AND date::date = date '" + next.toString() + "'";
			ResultSet otherReservationNum = stmt.executeQuery(calendarSql);
			if(otherReservationNum.next())
				otherReservation = otherReservationNum.getString("reservation_number");
			//System.out.println("other reservation is " + otherReservation);
			
			if(otherReservation != null && otherReservation != ""){
				String timeSql = "SELECT arrival_date, depature_date FROM reservation WHERE reservation_number = '"+otherReservation+"'";
				ResultSet timeResult = stmt.executeQuery(timeSql);
				if(timeResult.next()){
					//int otherRoomNum = timeResult.getInt("room_number");
					Date arr = new Date(timeResult.getTimestamp("arrival_date").getTime());
					Date dep = new Date(timeResult.getTimestamp("depature_date").getTime());
					
					//Yes = changeRoom(roomNum, arr, dep);
					if(Chelsea.changeRoom(roomNum, arr, dep).compareTo("No available room!") == 0) 
						Yes = false;
					else{
						Yes = true;
						String calendarSql2 = "UPDATE calendar SET reservation_number = '"+myReservation+"' WHERE room_number = " + roomNum + "AND date::date = date '" + next.toString() + "'";
						stmt.executeUpdate(calendarSql2);
					}
					
				}
				timeResult.close();
			}
			else{
				//System.out.println("other reservation is null");
				String calendarSql2 = "UPDATE calendar SET reservation_number = '"+myReservation+"' WHERE room_number = " + roomNum + "AND date::date = date '" + next.toString() + "'";
				stmt.executeUpdate(calendarSql2);
				Yes = true;
			}
			otherReservationNum.close();	
		}
	
		//System.out.println("new dep date " + next);
		if(Yes){
			Timestamp newDep = new Timestamp(next.getTime());
			String reservationSql2 = "UPDATE reservation SET depature_date = '" + newDep + "' WHERE reservation_number = '"+ myReservation + "'";
			stmt.executeUpdate(reservationSql2);
		}
		
		stmt.close();
		return String.valueOf(Yes);
	}
	
}
