package VRentMain;
import java.sql.Connection;
import java.util.Scanner;
import Sql.basicQueries;
import Sql.mainQueries;
import Vehicle.Vehicle;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
public class VRentMain{
	static Scanner sc = new Scanner(System.in);
	public static void userLogin(ArrayList<User> users) throws SQLException {
		System.out.println("Enter User Name:");
		String uname = sc.next();
		System.out.println("Enter Password:");
		String pass = sc.next();
		for(User u : users) {
			if(u.userName.equals(uname) && u.password.equals(pass)) {
				u.UserController();
				return;
			}
		}
		System.out.println("Username or Password incorrect.");
	}
	public static void adminLogin(ArrayList<Admin> admins) throws SQLException {
		
		System.out.println("Enter AdminId:");
		String uname = sc.next();
		System.out.println("Enter Password:");
		String pass = sc.next();
		for(Admin u : admins) {
			if(u.adminId.equals(uname) && u.password.equals(pass)) {
				u.AdminController();
				return;
			}
		}
		System.out.println("Admin Id or Password incorrect.");
	}
	public static void userSignUp(ArrayList<User> users, Connection conn, mainQueries q) {
		System.out.println("Enter User Name:");
		String uname = sc.next();
		System.out.println("Enter phone number:");
		String phno = sc.next();
		System.out.println("Enter email Id:");
		String email = sc.next();
		System.out.println("Enter Password:");
		String pass = sc.next();
		User u = new User(uname, pass, phno, email, conn);
		q.addUser(u);
		q.loadUsers(users); //loads into the arraylist
	}
	public static void adminSignUp(ArrayList<Admin> admins, Connection conn, mainQueries q) {
		System.out.println("Enter User Name:");
		String uname = sc.next();
		System.out.println("Enter Password:");
		String pass = sc.next();
		String secret = q.retrieveSecret();
		if(secret.length()==0) {
			System.out.println("Something went wrong");
			return ;
		}
		System.out.println("Enter Admin Secret:");
		String as = sc.next();
		if(!as.equals(secret)) {
			System.out.println("Incorrect secret");
			return ;
		}
		Admin a =new Admin(uname, pass, conn);
		q.addAdmin(a);
		q.loadAdmins(admins);
	}
	public static void main(String[] args) throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		ArrayList<Admin> admins = new ArrayList<Admin>();
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/world?allowLoadLocalInfile=true","root","Qwerty@123");
		mainQueries q = new mainQueries(conn);
		basicQueries b = new basicQueries(conn);
		q.initializeDb();
		q.loadAllViaCsv();
		q.loadAdmins(admins);
		q.loadUsers(users);
		q.loadVehicles(vehicles);
		if(args.length!=0) {
			switch(args[0]) {
			case "searchVId":
				System.out.println("Enter Vehicle Id: ");
				String vid = sc.next();
				b.searchVehicleById(vid);
				return;
			case "searchVS":
				System.out.println("Enter Seating Capacity: ");
				int seating = sc.nextInt();
				b.searchVehicleBySeating(seating);
				return;
			case "searchUN":
				System.out.println("Enter Name: ");
				String username = sc.next();
				b.searchUser(username);
				return;
			case "deleteV":
				System.out.println("Enter Vehicle Id: ");
				String vid2 = sc.next();
				b.removeVehicle(vid2);
				return;
			case "showUsers":
				b.showUsers();
				return;
			case "showVehicles":
				b.showVehicles();
				return;
			case "insertViaCsv":
				String tableName;
				if(args.length>1) {
					tableName=args[1];
				}else{
				System.out.println("Enter table name: ");
				tableName = sc.next();
				}
				q.loadFromCsv(tableName);
				return;
			case "insertAllViaCsv":
				q.loadAllViaCsv();
				return;
			case "updateViaCsv":
				String tableName2;
				if(args.length>1) {
					tableName2=args[1];
				}else{
				System.out.println("Enter table name: ");
				tableName2 = sc.next();
				}
				q.updateFromCsv(tableName2);
				return;
			case "help":
			default:
				System.out.println("help:");
				System.out.println("searchVId      -> Searches for Vehicle By Id");
				System.out.println("searchVS       -> Searches for Vehicle BySeating Capacity");
				System.out.println("searchUN       -> Searches for User by User Name (Partial Search Supported)");
				System.out.println("DeleteV        -> Delete A Specific Vehicle");
				System.out.println("showUsers      -> Shows All Users");
				System.out.println("showVehicles   -> Shows All Vehicles");
				System.out.println("insertViaCsv <tableName>   -> Inserts from sqlcsv/tableName.csv to specified table");
				System.out.println("insertAllViaCsv            -> Inerts from all sqlcsv/tableName.csv files to tables");
				System.out.println("updateViaCsv <tableName>   -> Updates from sqlcsv/updatetableName.csv to specified table (matched by user name or Ids)");
				System.out.println("help      -> Prints this message");
				return;
			}
		}
		int option = 0;
		while(option!=5) {
		System.out.println("Options:");
		System.out.println("1) Login as User");
		System.out.println("2) Login as Admin");
		System.out.println("3) Sign Up as User");
		System.out.println("4) Sign Up as Admin");
		System.out.println("5) Exit");
		System.out.print("Select: ");
		option = sc.nextInt();
			if(option==1) {
				userLogin(users);
			}
			else if(option==2) {
				adminLogin(admins);
			}
			else if(option==3) {
				userSignUp(users, conn, q);
			}
			else if(option==4) {
				adminSignUp(admins, conn, q);
			}
		}
		
	}
}