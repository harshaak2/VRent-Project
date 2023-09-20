package Vehicle;

public class FourWheeler extends Vehicle{
	public FourWheeler(String VehicleName, String VehicleId, GearType gt, int seating){
		this.VehicleName=VehicleName;
		this.VehicleId=VehicleId;
		this.gearType=gt;
		this.seating=seating;
	}
    @Override
    int getSeating() {
        return this.seating;
    }

    @Override
    int getNo_of_wheels() {
        return 4;
    }
	@Override
	GearType getGearType() {
		// TODO Auto-generated method stub
		return this.gearType;
	}

  
}