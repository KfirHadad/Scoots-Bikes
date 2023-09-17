import java.util.Random;

public class Salesman implements Runnable {

	private String id;
	private Random random = new Random();
	private volatile boolean isDayOver = false;

	public Salesman(String id) {
		this.id = id;
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
			} else {
				Thread.sleep(1500);
			}

			boolean isConvinced = random.nextDouble() < 0.87;

			if (!isConvinced) {
				System.out.println(
						"Customer " + customer.getName() + " is not convinced. Transferring to customer manager.");
				// Add the customer to the CustomerManager queue here.
			} else {

				// Simulate searching for the cheapest tool
				ElectricVehicle cheapestVehicle = searchCheapestTool(customer.getVehicle().getType());

				// Create a summary document
				SummaryDetails doc = new SummaryDetails(customer.getName(), customer.getIndication(), "Salesman",
						cheapestVehicle.getPrice());

				// Add the customer to the Cashier queue here.
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

	protected void setSalesmanDayOver() {
		isDayOver = true;
	}
}
