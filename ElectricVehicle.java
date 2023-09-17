import java.util.HashSet;
import java.util.Set;

public abstract class ElectricVehicle implements Comparable<ElectricVehicle> {

	private String type;
	private String modelName;
	private double maxSpeed;
	private double weight;
	private int price;
	private int inventory;

	private static Set<ElectricVehicle> stock = new HashSet<ElectricVehicle>();

	public ElectricVehicle(String type, String modelName, double maxSpeed, double weight, int price, int inventory)
			throws InvalidInputException {
		this.type = type;
		this.modelName = modelName;
		this.maxSpeed = maxSpeed;
		this.weight = weight;
		this.price = price;
		this.inventory = inventory;
		addToStock(this);
	}

	@Override
	public int compareTo(ElectricVehicle other) {
		return Integer.compare(this.price, other.price);
	}

	public int getPrice() {
		return price;
	}

	public String getType() {
		return this.type;
	}

	public int getInventory() {
		return this.inventory;
	}

	public void removeFromInventory() {
		if (this.inventory > 0)
			this.inventory = this.inventory - 1;
	}

	public static Set<ElectricVehicle> getStock() {
		return stock;
	}

	public static void addToStock(ElectricVehicle vehicle) {
		stock.add(vehicle);
	}

}
