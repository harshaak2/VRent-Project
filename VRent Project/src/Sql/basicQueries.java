package Sql;
import VRentMain.Reservation;
import java.sql.*;
import java.util.ArrayList;

public class basicQueries{
	Connection conn;
	Statement stm, stm2;
	public basicQueries(Connection c) throws SQLException{
		this.conn = c;
		stm = this.conn.createStatement();
		stm2 = this.conn.createStatement();
	}
	
	public void showTables() {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("show tables");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong!");
		}
		if(rs!=null){
			try {
				while(rs.next()){
					System.out.print(rs.getString("Tables_in_VRent")+"\n");
					
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong!");
			}

		}
	}
	public ArrayList<Reservation> loadUserReservations(String userName) {
		ResultSet rs = null;
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		try {
			rs=stm.executeQuery(String.format("select * from reservation where username = '%s'", userName));
		}catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading User Reservations!");
		}
		if(rs!=null) {
			try {
				while(rs.next()){
					reservations.add(new Reservation(rs.getString("reservation_id"),rs.getString("vehicle_id"),rs.getString("username"),rs.getDate("startdate"),rs.getDate("endDate")));
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading User Reservations!");
			}
		}
		return reservations;
	}
	public void searchUser(String username) {
		ResultSet rs = null;
		try {
			rs=stm.executeQuery(String.format("select * from user where username like '%%%s%%'", username));
		}catch (SQLException e) {
			System.out.println("Something Went Wrong While Searching Users!");
		}
		if(rs!=null) {
			System.out.println("Users");
			try {
				while(rs.next()){
					System.out.print(rs.getString("username")+"\t"+rs.getString("email")+"\n");
					
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Searching Users!");
			}
		}
	}
	public void searchVehicleById(String vid) {
		ResultSet rs = null;
		try {
			rs=stm.executeQuery(String.format("select * from vehicle where vehicle_id='%s'", vid));
		}catch (SQLException e) {
			System.out.println("Something Went Wrong While Searching Vehicles!");
		}
		if(rs!=null){
			System.out.println("Vehicles:");
			try {
				System.out.printf("%10s %10s %10s %10s %15s %10s\n","Vehicle Id","Vehicle Name","Gear Type","Seating","Vehicle Type","Rating");
				while(rs.next()){
					System.out.println(String.format("%10s  %10s  %10s  %10d  %15s %10.1f",rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5)?"Four Wheeler":"Two Wheeler", rs.getDouble(6)));
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Searching Vehicles!");
			}

		}
	}
	public void searchVehicleBySeating(int seating) {
		ResultSet rs = null;
		try {
			rs=stm.executeQuery(String.format("select * from vehicle where seating=%d", seating));
		}catch (SQLException e) {
			System.out.println("Something Went Wrong While Searching Vehicles!");
		}
		if(rs!=null){
			System.out.println("Vehicles:");
			try {
				System.out.printf("%10s %10s %10s %10s %15s %10s\n","Vehicle Id","Vehicle Name","Gear Type","Seating","Vehicle Type","Rating");
				while(rs.next()){
					System.out.println(String.format("%10s  %10s  %10s  %10d  %15s %10.1f",rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5)?"Four Wheeler":"Two Wheeler", rs.getDouble(6)));
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Searching Vehicles!");
			}

		}
	}
	public void showVehicles() {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("select * from vehicle");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading Vehicles!");
		}
		if(rs!=null){
			System.out.println("Vehicles:");
			try {
				System.out.printf("%10s %10s %10s %10s %15s %10s\n","Vehicle Id","Vehicle Name","Gear Type","Seating","Vehicle Type","Rating");
				while(rs.next()){
					System.out.println(String.format("%10s  %10s  %10s  %10d  %15s %10.1f",rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5)?"Four Wheeler":"Two Wheeler", rs.getDouble(6)));
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Vehicles!");
			}

		}
	}
	
	public void showUsers() {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("select * from user");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Searching Vehicles!");
		}
		if(rs!=null){
			System.out.println("Users:");
			try {
				while(rs.next()){
					System.out.print(rs.getString("username")+"\t"+rs.getString("email")+"\n");
					
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Users!");
			}

		}
	}
	
	public void addVehicle(String vid, String vname, String gt, int s, int w, double r) {
		try {
			stm.execute(String.format("insert into vehicle values('%s','%s','%s',%d,%d,%f)",vid, vname, gt, s, w, r));
		} catch (SQLException e) {
			//System.out.println(e.getMessage());
			System.out.println("Something Went Wrong While Adding Vehicle!");
		}
	}
	public void removeVehicle(String vid) {
		try {
			stm.execute(String.format("delete from vehicle where vehicle_id = '%s'",vid));
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Removing Vehicle!");
		} 
		}
	public void showAvailableVehicles(String sdate, String edate) {
		//implement1
		ResultSet rs = null;
		try {
			rs = stm.executeQuery(String.format("select * from vehicle where vehicle_id not in (select vehicle_id from reservation where startdate between %s and %s or enddate between %s and %s or %s between startdate and enddate)", sdate, edate, sdate, edate, sdate));
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading Vehicles!");
		}
		if(rs!=null){
			System.out.println("Vehicles:");
			try {
				System.out.printf("%10s %10s %10s %10s %15s %10s\n","Vehicle Id","Vehicle Name","Gear Type","Seating","Vehicle Type","Rating");
				while(rs.next()){
					System.out.println(String.format("%10s  %10s  %10s  %10d  %15s %10.1f",rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5)?"Four Wheeler":"Two Wheeler", rs.getDouble(6)));
					
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Vehicles!");
			}

		}
	}
	public void makeReservation(String userName, String vid, String sdate, String edate) {
		try {
			stm.execute(String.format("insert into reservation (username, vehicle_id, startdate, enddate) values('%s', '%s', %s, %s)",userName, vid, sdate, edate));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Something Went Wrong While Making the Reservation!");
		}
	}
	public void showuserReservations(String uname) {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery(String.format("select * from reservation where username = '%s'",uname));
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading Reservations!");
		}
		if(rs!=null){
			System.out.println("Reservations:");
			System.out.println("Reservation Id\tvehicle Id\tstartdate\tenddate");
			try {
				while(rs.next()){
					System.out.println(rs.getString("reservation_id")+"\t\t"+rs.getString("vehicle_id")+"\t"+rs.getString("startdate")+"\t"+rs.getString("enddate")+"\n");
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Reservations!");
			}

		}
	}
	public void cancelReservation(String rid) {
		try {
			stm.execute(String.format("delete from reservation where reservation_id = '%s'",rid));
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Canceling Reservations!");
		}
	}
	public boolean isAvailableVehicle(String sdate, String edate, String rid) {
		ResultSet rs = null, vidrs=null;
		String vid;
		try {
			rs = stm.executeQuery(String.format("select reservation_id, vehicle_id from reservation where startdate between %s and %s or enddate between %s and %s or %s between startdate and enddate", sdate, edate, sdate, edate, sdate));
			vidrs = stm2.executeQuery(String.format("select vehicle_id from reservation where reservation_id = '%s'",rid));
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Checking for the vehicle!");
		}
		if(rs!=null){
			
			System.out.println("Vehicles:");
			try {
				if(vidrs!=null && vidrs.next()) {
					vid = vidrs.getString("vehicle_id");
					while(rs.next()){
						if(!rs.getString("reservation_id").equals(rid) && rs.getString("vehicle_id").equals(vid)) {
							return false;
						}
					}
					return true;
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Checking for the vehicle!");
			}

		}
		return false;
	}
	public void updateReservation(String rid, String sdate, String edate) {
		try {
			stm.execute(String.format("update reservation set startdate = %s where reservation_id = '%s' ",sdate, rid));
			stm2.execute(String.format("update reservation set enddate = %s where reservation_id = '%s' ",edate, rid));
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Updating The Reservation!");
		}
	}
	public void showAvailableAltVehicles(String rid) {
		ResultSet rs = null, datesrs=null;
		try {
			datesrs = stm.executeQuery(String.format("select startdate, enddate from reservation where reservation_id = '%s'",rid));
			if(datesrs!=null){
				datesrs.next();
				String sdate = String.format("'%s'",datesrs.getString("startdate"));
				String edate = String.format("'%s'",datesrs.getString("enddate"));
				rs = stm2.executeQuery(String.format("select * from vehicle where vehicle_id not in (select vehicle_id from reservation where startdate between %s and %s or enddate between %s and %s or %s between startdate and enddate)", sdate, edate, sdate, edate, sdate));
		    }
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading Vehicles!");
		}
		if(rs!=null){
			System.out.println("Vehicles:");
			try {
				System.out.printf("%10s %10s %10s %10s %15s %10s\n","Vehicle Id","Vehicle Name","Gear Type","Seating","Vehicle Type","Rating");
				while(rs.next()){
					System.out.println(String.format("%10s  %10s  %10s  %10d  %15s %10.1f",rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5)?"Four Wheeler":"Two Wheeler", rs.getDouble(6)));
					
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Vehicles!");
			}

		}
	}
	public void updateReservation(String rid, String vid) {
		try {
			stm.execute(String.format("update reservation set vehicle_id = '%s' where reservation_id = '%s' ",vid, rid));
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Updating The Reservation!");
		}
	}
}