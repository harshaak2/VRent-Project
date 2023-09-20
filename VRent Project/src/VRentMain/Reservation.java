package VRentMain;

import java.sql.Date;
public class Reservation{
	private String reservationId;
	private String vehicleId;
	private String userId;
	private Date startDate;
	private Date endDate;
	public Reservation(String reservationId,String vehicleID, String userId, Date startDate, Date endDate){
		this.vehicleId=vehicleID;
		this.userId=userId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reservationId=reservationId;
	}
	public Date getEndDate() {
		return endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public String getUserId() {
		return userId;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getReservationId() {
		return reservationId;
	}
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
}