import java.util.*;

public class SeniorTechnician implements Runnable {

	private String id;
	private boolean isMaster;
	private Random random = new Random();
	private volatile boolean isDayOver = false;

	public SeniorTechnician(String id, boolean isMaster) {
		this.id = id;
		this.isMaster = isMaster;
	}

	@Override
	public void run() {
		try {
			while (true) {
				processCustomer();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void processCustomer() throws InterruptedException {

		while (!isDayOver) {

			while (JuniorTechnician.getSeniorQueue().isEmpty()) {
				System.out.println("Technician " + id + " is waiting for customers.");
				wait(); // Wait for a customer to be added
			}

			if (isDayOver)
				break;

			Customer customer = JuniorTechnician.getSeniorQueue().remove();
			ElectricVehicle vehicle = customer.getVehicle();
			int cost;

			if (vehicle == null) {
				System.out.println("you don't own a vehicle");
				return;
			}

			System.out.println("Evaluating costs...");
			Thread.sleep(1000);

			if (vehicle instanceof Scooter) {
				cost = random.nextInt(451) + 50;
			} else if (vehicle instanceof Bike) {
				cost = random.nextInt(701) + 100;
			} else {
				cost = 0;
			}
			
			
			if (!this.isMaster && cost > 450 && random.nextDouble() < 0.9) {
				System.out.println(
						"Customer " + customer.getName() + "is very dissatisfied. Transferring to customer manager.");
				// Add the customer to the CustomerManager queue here.
			} else {
				if (customer.hasReturnedFromManager()) {
					Thread.sleep(1000);
				} else {

					if (cost >= 50 && cost < 300) {
						Thread.sleep(1000);
					} else if (cost >= 300 && cost < 450) {
						Thread.sleep(2000);
					} else if (cost >= 450 && cost <= 800) {
						Thread.sleep(3000);
					}
				}
				
				System.out.println("Customer " + customer.getName() + " treatment completed. Cost: " + cost + " NIS");
				customer.setRepairCost(cost);
				SummaryDetails doc = new SummaryDetails(customer.getName(), customer.getIndication(),
						"Senior Technician", cost);
				// Add the customer to the Cashier queue here.
			}
		}
	}

	protected void setSeniorDayOver() {
		isDayOver = true;
	}

}
