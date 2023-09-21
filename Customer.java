
public class Customer implements Runnable {

	private String name;
	private String indication;
	private int arrivalTime;
	private int cost;
	private ElectricVehicle vehicle;
	private String vehicleType;
	private String model;
	private double maxSpeed;
	private double weight;
	private int isClosing;
	private boolean returnedFromManager = false;

	public Customer(String name, String indication, int arrivalTime, ElectricVehicle vehicle) {
		this.name = name;
		this.indication = indication;
		this.arrivalTime = arrivalTime;
		this.vehicle = vehicle;
	}

	public Customer(String name, String indication, int arrivalTime, String vehicleType, String model,
            double maxSpeed, double weight, int isClosing) throws InvalidInputException {
        this.name = name;
        this.indication = indication;
        this.arrivalTime = arrivalTime;
        this.vehicleType = vehicleType;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.weight = weight;
        this.isClosing = isClosing;
        // You should initialize the 'vehicle' field here based on 'vehicleType'
        this.vehicle = createElectricVehicle(vehicleType, model, maxSpeed, weight, isClosing);
    }
	
	 private ElectricVehicle createElectricVehicle(String vehicleType, String model, double maxSpeed,
	            double weight, int isClosing) throws InvalidInputException {
	        // Implement your logic to create ElectricVehicle here based on 'vehicleType'
	        // You may need to validate the input and handle exceptions.
	        // Example:
	        if ("Bike".equalsIgnoreCase(vehicleType)) {
	            return new Bike(model, maxSpeed, weight, isClosing, 0, 0);
	        } else if ("Scooter".equalsIgnoreCase(vehicleType)) {
	            return new Scooter(model, maxSpeed, weight, 0, 0);
	        } else {
	            // Handle unknown vehicle types or throw an exception
	            throw new InvalidInputException("Unknown vehicle type: " + vehicleType);
	        }
	    }

	@Override
	public void run() {
		try {
			Thread.sleep(arrivalTime * 1000);
			Clerk.addCustomerToClerk(this);
		} catch (InterruptedException e) {
			e.getMessage();
		}
	}

	public static Customer parseCustomerFromLine(String line) throws InvalidInputException {
		String[] parts = line.split("\t");
		if (parts.length < 4) {
			// Invalid line format
			return null;
		}

		String name = parts[0];
		String indication = parts[1];
		int arrivalTime = Integer.parseInt(parts[2]);
		String vehicleType = parts[3];
		String model = parts.length > 4 ? parts[4] : null;
		double maxSpeed = parts.length > 5 ? Double.parseDouble(parts[5]) : 0.0;
		double weight = parts.length > 6 ? Double.parseDouble(parts[6]) : 0.0;
		int isClosing = parts.length > 7 ? Integer.parseInt(parts[7]) : 0;

		// Create and return a new Customer object
		return new Customer(name, indication, arrivalTime, vehicleType, model, maxSpeed, weight, isClosing);
	}

	public String getName() {
		return this.name;
	}

	public String getIndication() {
		return this.indication;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return this.cost;
	}

	public ElectricVehicle getVehicle() {
		return vehicle;
	}

	public boolean hasReturnedFromManager() {
		return returnedFromManager;
	}

	public void setReturnedFromManager() {
		this.returnedFromManager = true;
	}

}
