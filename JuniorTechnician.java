import java.util.*;

public class JuniorTechnician implements Runnable {

	private String id;
	private int seniority;
	private Random random = new Random();
	private static Queue<Customer> seniorQueue = new Queue<Customer>();
	private static volatile boolean isDayOver = false;

	public JuniorTechnician(String technicianID, int seniority) {
		this.id = technicianID;
		this.seniority = seniority;
	}

	@Override
	public void run() {
		try {
			while (true) {
				processCustomer();
			}
		} catch (InterruptedException e) {
			// Handle interrupted exception if needed
			e.printStackTrace();
		}
	}

	private synchronized void processCustomer() throws InterruptedException {

		while (!isDayOver) {

			while (Clerk.getRepairingBuffer().isEmpty() && !isDayOver) {
				System.out.println("Technician " + id + " is waiting for customers.");
				wait(); // Wait for a customer to be added
			}

			if (isDayOver)
				break;

			Customer customer = Clerk.getRepairingBuffer().remove();

			// Simulate the test result
			if (random.nextDouble() < 0.2) {
				System.out.println("Complex malfunction detected. Transferring customer " + customer.getName()
						+ " to senior technician.");
				addCustomerToSenior(customer);
			} else if (random.nextDouble() < 0.3) {
				System.out.println(
						"Customer " + customer.getName() + " is very dissatisfied. Transferring to customer manager.");
				// Add the customer to the CustomerManager queue here.
			} else {
				Thread.sleep(3000);
				int cost = random.nextInt(701) + 100; // Random cost between 100-800
				System.out.println("Customer " + customer.getName() + " treatment completed. Cost: " + cost + " NIS");
				customer.setRepairCost(cost);
				SummaryDetails doc = new SummaryDetails(customer.getName(), customer.getIndication(),
						"Junior Technician", cost);
				// Add the customer to the Cashier queue here.
			}
		}
	}

	public static void addCustomerToSenior(Customer customer) {
		synchronized (SeniorTechnician.class) {
			seniorQueue.add(customer);
			SeniorTechnician.class.notifyAll();
		}
	}

	public static Queue<Customer> getSeniorQueue() {
		return seniorQueue;
	}

	protected static void setJuniorDayOver() {
		synchronized (SeniorTechnician.class) {
			isDayOver = true;

		}
	}
}
