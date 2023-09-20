package Vehicle;
public abstract class Vehicle{
    protected String VehicleId;
    protected String VehicleName;
    protected int no_of_wheels;//
    protected int seating;
    protected GearType gearType;
    
    //protected ArrayList<Reservation> reservations;
    abstract int getSeating();
    abstract int getNo_of_wheels();
    abstract GearType getGearType();
    public void setGearType(GearType gearType) {
        this.gearType = gearType;
    }
    public void setNo_of_wheels(int no_of_wheels) {
        this.no_of_wheels = no_of_wheels;
    }
    public void setSeating(int seating) {
        this.seating = seating;
    }
    public void setVehicleId(String vehicleId) {
        this.VehicleId = vehicleId;
    }
    public void setVehicleName(String vehicleName) {
        this.VehicleName = vehicleName;
    }
}