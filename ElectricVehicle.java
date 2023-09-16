
public abstract class ElectricVehicle {

	private String modelName;
	private double maxSpeed;
	private double weight;

	public ElectricVehicle(String modelName, double maxSpeed, double weight) throws InvalidInputException{
		this.modelName = modelName;
		this.maxSpeed = maxSpeed;
		this.weight = weight;
	}

}
