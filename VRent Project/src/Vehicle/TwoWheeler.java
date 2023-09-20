package Vehicle;

public class TwoWheeler extends Vehicle{
	public TwoWheeler(String VehicleName, String VehicleId, GearType gt){
		this.VehicleName=VehicleName;
		this.VehicleId=VehicleId;
		this.gearType=gt;
	}
    @Override
    int getSeating() {
        return 2;
    }

	@Override
	int getNo_of_wheels() {
		return 2;
	}
	@Override
	GearType getGearType() {
		return this.gearType;
	}
}