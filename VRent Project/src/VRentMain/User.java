package VRentMain;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import Sql.basicQueries;
public class User{
	public String userName;
	public String password;
	public String phno;
	public String email;
	ArrayList<Reservation> reservations;
	Connection conn;
	public User(String userName, String password, String phno, String email, Connection conn)
	{
		this.userName = userName;
		this.password = password;
		this.phno = phno;
		this.email = email;
		this.conn = conn;
	}
	Scanner sc = new Scanner(System.in);
	public void UserController() throws SQLException {
		basicQueries queries = new basicQueries(conn);
		int option = 0;
		while(option!=5) {
		this.reservations = queries.loadUserReservations(userName);
		System.out.println("Options:");
		System.out.println("1) Make A Reservation");
		System.out.println("2) Cancel Booking");
		System.out.println("3) update Reservations");
		System.out.println("4) show Reservations");
		System.out.println("5) Log out");
		System.out.print("Select: ");
		option = sc.nextInt();
			if(option==1) {
				MakeReservation();
			}
			else if(option==2) {
				CancelReservation();
			}
			else if(option==3) {
				updateReservation();
			}
			else if(option==4) {
				System.out.println("Reservations:");
				System.out.println("Reservation Id\tvehicle Id\tstartdate\tenddate");
				for(Reservation r : reservations) {
					System.out.printf("%s\t\t%s\t%s\t%s\n",r.getReservationId(),r.getVehicleId(),r.getStartDate().toString(), r.getEndDate().toString());
				}
			}
			else if(option==5)
			{
				System.out.printf("Thank You for using VRent, hope to see you soon!!\n");
			}
			else
			{
				System.out.printf("Invalid Choice");
			}
		}
	}
	public void MakeReservation() throws SQLException {
		basicQueries queries = new basicQueries(conn);
		String sdate, edate;
		System.out.println("Reservation from date:");
		System.out.println("Start Year(YYYY):");
		String sy = sc.next();
		System.out.println("Start Month(MM):");
		String sm = sc.next();
		System.out.println("Start day(DD):");
		String sd = sc.next();
		sdate = String.format("'%s-%s-%s'",sy,sm,sd);
		System.out.println("Reservation to date:");
		System.out.println("End Year(YYYY):");
		String ey = sc.next();
		System.out.println("End Month(MM):");
		String em = sc.next();
		System.out.println("End day(DD):");
		String ed = sc.next();
		edate = String.format("'%s-%s-%s'",ey,em,ed);
		queries.showAvailableVehicles(sdate, edate);
		System.out.println("Enter Vehicle Id of Selected vehicle: ");
		String vid = sc.next();
		queries.makeReservation(this.userName, vid, sdate, edate);
	}
	public void CancelReservation() throws SQLException {
		basicQueries queries = new basicQueries(conn);
		queries.showuserReservations(this.userName);
		System.out.println("Enter reservation id to cancel: ");
		String rid = sc.next();
		queries.cancelReservation(rid);
	}
	public void updateReservation() throws SQLException {
		basicQueries queries = new basicQueries(conn);
		queries.showuserReservations(this.userName);
		System.out.println("Enter reservation id to update: ");
		String rid = sc.next();
		System.out.println("Options:");
		System.out.println("1) Update reservation dates");
		System.out.println("2) Update vehicle");
		int o = sc.nextInt();
		if(o==1) {
			String sdate, edate;
			System.out.println("Reservation from date:");
			System.out.println("Start Year(YYYY):");
			String sy = sc.next();
			System.out.println("Start Month(MM):");
			String sm = sc.next();
			System.out.println("Start day(DD):");
			String sd = sc.next();
			sdate = String.format("'%s-%s-%s'",sy,sm,sd);
			System.out.println("Reservation to date:");
			System.out.println("End Year(YYYY):");
			String ey = sc.next();
			System.out.println("End Month(MM):");
			String em = sc.next();
			System.out.println("End day(DD):");
			String ed = sc.next();
			edate = String.format("'%s-%s-%s'",ey,em,ed);
			if(queries.isAvailableVehicle(sdate, edate, rid)) {
				queries.updateReservation(rid, sdate, edate);
			}
			else {
				System.out.println("Vehicle not available in these dates");
			}
		}
		else if(o==2) {
			String vid;
			queries.showAvailableAltVehicles(rid);
			System.out.println("Enter Vehicle Id of Selected vehicle: ");
			vid = sc.next();
			queries.updateReservation(rid, vid);
		}
	}
}