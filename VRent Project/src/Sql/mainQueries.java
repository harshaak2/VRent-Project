package Sql;
import java.sql.*;
import java.util.ArrayList;
import java.nio.file.Paths;
import VRentMain.Admin;
import VRentMain.User;
import Vehicle.*;

public class mainQueries{
	Connection conn;
	Statement stm;
	public mainQueries(Connection c) throws SQLException{
		this.conn = c;
		stm = this.conn.createStatement();
	}
	public void initializeDb() {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("show databases");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Initializing Database!");
		}
		try {
			while(rs.next()) {
				if(rs.getString("Database").toLowerCase().equals("vrent")) {
					stm.execute("use vrent");
					return;
				}
			}
			createDbAndTables();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Something Went Wrong While Initializing Database!");
		}
	}
	public void createDbAndTables() {
		try {
			stm.executeUpdate("create database vrent");
			stm.executeUpdate("use vrent");
			stm.executeUpdate("create table user (username varchar(20) primary key, phno varchar(20), email varchar(20), password varchar(20))");
			stm.executeUpdate("create table vehicle(vehicle_id varchar(20) primary key, vehicle_name varchar(20), geartype varchar(10), seating int, four_wheeler boolean, rating double)");
			stm.executeUpdate("create table reservation(reservation_id int primary key auto_increment, username varchar(20), vehicle_id varchar(10), startdate date, enddate date);");
			stm.executeUpdate("alter table reservation add FOREIGN key (username) references user(username)");
			stm.executeUpdate("alter table reservation add FOREIGN key (vehicle_id) references vehicle(vehicle_id)");
			stm.executeUpdate("create table admin(admin_id varchar(20) primary key, password varchar(20))");
			stm.executeUpdate("create table secret(secret varchar(20))");
			stm.executeUpdate("insert into secret value('qaziop')");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Something Went Wrong While Creating Tables!");
		}
	}
	public void loadAllViaCsv() {
		String[] tables = {"vehicle", "user", "admin", "reservation"};
		for(String s: tables) {
			loadFromCsv(s);
		}
	}
	public void loadFromCsv(String tableName) {
		try {
			String p = Paths.get(".").toAbsolutePath().normalize().toString();
			p=p.replace("\\", "/");
			p=p.concat(String.format("/src/sqlcsv/%s.csv", tableName));
			stm.executeUpdate(String.format("LOAD DATA LOCAL INFILE '%s' INTO TABLE %s FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS;",p,tableName));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Something Went Wrong While Loading files From CSV!");
		}
	}
	public void updateFromCsv(String tableName) {
		try {
			String query="";
			stm.executeUpdate("drop table if exists temp");
			switch(tableName) {
			case "user":
				stm.executeUpdate("create table temp (username varchar(20) primary key, phno varchar(20), email varchar(20), password varchar(20))");
				query="update user inner join temp on user.username=temp.username set user.phno = temp.phno, user.email = temp.email, user.password = temp.password";
				break;
			case "vehicle":
				stm.executeUpdate("create table temp (vehicle_id varchar(20) primary key, vehicle_name varchar(20), geartype varchar(10), seating int, four_wheeler boolean, rating double)");
				query="update vehicle inner join temp on vehicle.vehicle_id = temp.vehicle_id set vehicle.vehicle_name=temp.vehicle_name, vehicle.geartype=temp.geartype, vehicle.seating = temp.seating, vehicle.four_wheeler=temp.four_wheeler, vehicle.rating = temp.rating";
				break;
			case "admin":
				stm.executeUpdate("create table temp (admin_id varchar(20) primary key, password varchar(20))");
				query="update admin inner join temp on admin.admin_id = temp.admin_id set admin.password = temp.password";
				break;
			case "reservation":
				stm.executeUpdate("create table temp (reservation_id int primary key, username varchar(20), vehicle_id varchar(10), startdate date, enddate date);");
				query="update reservation inner join temp on reservation.reservation_id=temp.reservation_id set reservation.username=temp.username, reservation.vehicle_id= temp.vehicle_id, reservation.startdate=temp.startdate, reservation.enddate=temp.enddate";
				break;
			}
			String p = Paths.get(".").toAbsolutePath().normalize().toString();
			p=p.replace("\\", "/");
			p=p.concat(String.format("/src/sqlcsv/update%s.csv", tableName));
			stm.executeUpdate(String.format("LOAD DATA LOCAL INFILE '%s' INTO TABLE temp FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS;",p));
			stm.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Updating From Csv!");
		}
	}
	public void loadAdmins(ArrayList<Admin> admins) {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("select admin_id, password from admin");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading Admins");
		}
		if(rs!=null){
			try {
				while(rs.next()){
					admins.add(new Admin(rs.getString("admin_id"),rs.getString("password"),conn));
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Admins");
			}

		}
	}
	public void loadUsers(ArrayList<User> users) {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("select * from user");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading Admins");
		}
		if(rs!=null){
			try {
				while(rs.next()){
					users.add(new User(rs.getString("username"),rs.getString("password"),rs.getString("phno"),rs.getString("email"),conn));
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Users");
			}

		}
	}
    public void loadVehicles(ArrayList<Vehicle> vehicles) {
    	ResultSet rs = null;
		try {
			rs = stm.executeQuery("select * from vehicle");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Loading Vehicles");
		}
		if(rs!=null){
			try {
				while(rs.next()){
					if(rs.getBoolean(5)) {
						if(rs.getString(3).equals("manual"))
						vehicles.add(new FourWheeler(rs.getString(2),rs.getString(1),GearType.Manual, rs.getInt(4)));
						else
						vehicles.add(new FourWheeler(rs.getString(2),rs.getString(1),GearType.Auto, rs.getInt(4)));
					}else {
						if(rs.getString(3).equals("manual"))
						vehicles.add(new TwoWheeler(rs.getString(2), rs.getString(1),GearType.Manual));
						else
						vehicles.add(new TwoWheeler(rs.getString(2), rs.getString(1),GearType.Auto));
					}
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Loading Vehicles");
			}

		}
	}
	public String retrieveSecret() {
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("select * from secret");
		} catch (SQLException e) {
			System.out.println("Something Went Wrong While Retrieving Secret");
		}
		if(rs!=null){
			try {
				while(rs.next()){
					return rs.getString("secret");
				}
			} catch (SQLException e) {
				System.out.println("Something Went Wrong While Retrieving Secret");
			}

		}
		return "";
	}
	public void addUser(User u) {
		try {
			stm.execute(String.format("insert into user values('%s','%s','%s','%s')",u.userName,u.phno,u.email,u.password));
		} catch (SQLException e) {
			if(e.getMessage().contains("PRIMARY")) System.out.println("Username Should be unique.");
			else System.out.println("Something went wrong.");
		}
	}
	public void addAdmin(Admin a) {
		try {
			stm.execute(String.format("insert into admin values('%s','%s')",a.adminId,a.password));
		} catch (SQLException e) {
			if(e.getMessage().contains("PRIMARY")) System.out.println("Admin Id Should be unique.");
			else System.out.println("Something went wrong.");
		}
	}
	
}