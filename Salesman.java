import java.util.Random;

public class Salesman extends Employee implements Runnable {

	private String id;
	private Random random = new Random();
	private static volatile boolean isDayOver = false;
	private int totalSales;
	private int customerCount;

	public Salesman(String id) {
		super(id);
		totalSales = 0;
		this.customerCount = 0;
	}

	@Override
	public void run() {
		try {
			while (!isDayOver) {
				processCustomer();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void processCustomer() throws InterruptedException {
		while (!isDayOver) {

			while (Clerk.getBuyingBuffer().isEmpty()) {
				System.out.println("Salesman " + id + " is waiting for customers.");
				wait();
			}

			if (isDayOver) {
				break;
			}

			Customer customer = Clerk.getBuyingBuffer().remove();

			boolean isFirstInteraction = !customer.hasReturnedFromManager();

			if (isFirstInteraction) {
				System.out.println(
						"Salesman " + id + " is having a 3-second conversation with customer: " + customer.getName());
				Thread.sleep(3000);

				boolean isConvinced = random.nextDouble() < 0.87;

				if (!isConvinced) {
					System.out.println(
							"Customer " + customer.getName() + " is not convinced. Transferring to customer manager.");
					CustomerManager.addToManagerQueue(customer);
				} else {

					// Simulate searching for the cheapest tool
					ElectricVehicle cheapestVehicle = searchCheapestTool(customer.getVehicle().getType());
					customer.setCost(cheapestVehicle.getPrice());
					totalSales = totalSales + cheapestVehicle.getPrice();

					// Create a summary document and send to cashier
					sendToCashier(customer);

				}
			} else {
				Thread.sleep(1500);
				sendToCashier(customer);
			}
		}
	}

	private ElectricVehicle searchCheapestTool(String vehicle) {

		ElectricVehicle cheapestTool = null;

		for (ElectricVehicle tool : ElectricVehicle.getStock()) {
			if (cheapestTool == null || (tool.getType().equals(vehicle) && tool.getInventory() > 0
					&& tool.compareTo(cheapestTool) < 0)) {
				cheapestTool = tool;
			}
		}

		cheapestTool.removeFromInventory();
		return cheapestTool;
	}

	protected static void setSalesmanDayOver() {
		synchronized (Salesman.class) {
			isDayOver = true;
		}
	}

	protected void sendToCashier(Customer customer) {
		this.customerCount = this.customerCount + 1;
		SummaryDetails doc = new SummaryDetails(customer.getName(), customer.getIndication(), this, customer.getCost());
		Cashier.addToQueue(doc);
	}

	public int getTotal() {
		return this.totalSales;
	}

}
