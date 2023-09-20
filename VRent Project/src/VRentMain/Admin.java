package VRentMain;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import Sql.basicQueries;

public class Admin{
	Connection conn;
	public String adminId;
	public String password;
	public Admin(String admid, String password, Connection c){
		this.adminId = admid;
		this.password = password;
		this.conn = c;
	}
	public void AdminController() throws SQLException {
		basicQueries queries = new basicQueries(conn);
		Scanner sc = new Scanner(System.in);
		int option =0;
		while(option!=8) {
		System.out.println("Options:");
		System.out.println("1) Show Vehicles");
		System.out.println("2) Show Users");
		System.out.println("3) Add Vehicle");
		System.out.println("4) Remove Vehicle");
		System.out.println("5) Search User by UserName");
		System.out.println("6) Search Vehicle by Vehicle Id");
		System.out.println("7) Search Vehicle By Seating");
		System.out.println("8) Logout");
		System.out.print("Select: ");
		option = sc.nextInt();		
			if(option==1) {
				queries.showVehicles();
			}
			else if(option==2) {
				queries.showUsers();
			}
			else if(option==3) {
				System.out.println("Enter Vehicle Id: ");
				String vid = sc.next();
				System.out.print("Enter Vehicle Name:");
				String vname = sc.next();
				System.out.print("Enter Gear Type(m/a): ");
				String gt = sc.next().equals("m")?"manual":"auto";
				System.out.println("Enter Seating: ");
				int s = sc.nextInt();
				System.out.print("Two Wheeler or Four Wheeler(2/4): ");
				int w = sc.nextInt()==4?1:0;
				System.out.print("Rating: ");
				double r = sc.nextDouble();
				queries.addVehicle(vid, vname, gt, s, w, r);
			}
			else if(option==4) {
				System.out.println("Enter Vehicle Id: ");
				String vid = sc.next();
				queries.removeVehicle(vid);
			}
			else if(option==5) {
				System.out.println("Enter Name: ");
				String username = sc.next();
				queries.searchUser(username);
			}
			else if(option==6) {
				System.out.println("Enter Vehicle Id: ");
				String vid = sc.next();
				queries.searchVehicleById(vid);
			}
			else if(option==7) {
				System.out.println("Enter Seating Capacity: ");
				int seating = sc.nextInt();
				queries.searchVehicleBySeating(seating);
			}
		}
	}
}