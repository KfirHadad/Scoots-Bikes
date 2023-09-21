import java.util.*;

public class JuniorTechnician extends Employee implements Runnable {

	private String id;
	private int seniority;
	private Random random = new Random();
	private static Queue<Customer> seniorQueue = new Queue<Customer>();
	private static volatile boolean isDayOver = false;

	public JuniorTechnician(String technicianID, int seniority) {
		super(technicianID);
		this.seniority = seniority;
		this.customerCount = 0;
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
				CustomerManager.addToManagerQueue(customer);
			} else {
				Thread.sleep(3000);
				int cost = random.nextInt(701) + 100; // Random cost between 100-800
				System.out.println("Customer " + customer.getName() + " treatment completed. Cost: " + cost + " NIS");
				customer.setCost(cost);
				sendToCashier(customer);
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

	protected synchronized static void setJuniorDayOver() {
		isDayOver = true;

	}

	protected void sendToCashier(Customer customer) {
		this.customerCount = this.getCustomerCount() + 1;
		SummaryDetails doc = new SummaryDetails(customer.getName(), customer.getIndication(), this, customer.getCost());
		Cashier.addToQueue(doc);
	}


}
