
public class Customer implements Runnable {

	private String name;
	private String indication;
	private int arrivalTime;
	private int cost;
	private ElectricVehicle vehicle;
    private boolean returnedFromManager = false; 


	public Customer(String name, String indication, int arrivalTime, ElectricVehicle vehicle)
			throws InvalidInputException {
		if (isValidIndication()) {
			this.name = name;
			this.indication = indication;
			this.arrivalTime = arrivalTime;
			this.vehicle = vehicle;
		} else
			throw new InvalidInputException("indication must be either 'purchesing' or 'repairment'");
	}

	private boolean isValidIndication() {
		return indication.equals("purchesing") || indication.equals("repairment");
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

	public String getName() {
		return this.name;
	}

	public String getIndication() {
		return this.indication;
	}

	public void setRepairCost(int cost) {
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
