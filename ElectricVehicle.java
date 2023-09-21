import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;


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

	public static void loadVehicleStockFromFile(String filePath) throws InvalidInputException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                // Parse each line and create ElectricVehicle objects
                ElectricVehicle vehicle = parseVehicleFromLine(line);
                if (vehicle != null) {
                    addToStock(vehicle);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	
	
	protected static ElectricVehicle parseVehicleFromLine(String line) throws InvalidInputException {
        String[] parts = line.split("\t"); // Assuming tab-separated values

        if (parts.length != 7) {
            // Invalid line format
            return null;
        }

        String type = parts[0];
        String modelName = parts[1];
        double maxSpeed = Double.parseDouble(parts[2]);
        double weight = Double.parseDouble(parts[3]);
        int isClosing = Integer.parseInt(parts[4]);
        int price = Integer.parseInt(parts[5]);
        int inventory = Integer.parseInt(parts[6]);

        // Create ElectricVehicle objects based on the type
        if (type.equals("Scooter")) {
            return new Scooter(modelName, maxSpeed, weight, price, inventory);
        } else if (type.equals("Bike")) {
            return new Bike(modelName, maxSpeed, weight, isClosing, price, inventory);
        }

        // Unknown vehicle type
        return null;
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
