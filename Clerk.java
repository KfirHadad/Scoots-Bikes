import java.util.*;

public class Clerk implements Runnable {

	private String clerkID;
	private Random random = new Random();
	private static final int repairingBufferCapacity = 10;
	private static List<Customer> clerkQueue = new ArrayList<>();
	private static List<Customer> buyingBuffer = new ArrayList<>();
	private static List<Customer> repairingBuffer = new ArrayList<>();
	private static final Object key = new Object();

	public Clerk(String clerkID) {
		this.clerkID = clerkID;
	}

	public static void addCustomer(Customer customer) {
		synchronized (key) {
			clerkQueue.add(customer);
			key.notify();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				synchronized (key) {
					// Check if there are customers in the queue; if not, wait
					if (clerkQueue.isEmpty()) {
						System.out.println("Clerk " + clerkID + " is waiting for customers.");
						key.wait(); // Wait for a customer to be added
					}

					// Dequeue a customer from the list
					Customer customer = clerkQueue.remove(0);

					// Clerk starts servicing the customer
					System.out.println("Clerk " + clerkID + " is serving customer: " + customer.getName());

					// Random service time between 3 and 6 seconds
					int serviceTime = random.nextInt(4) + 3;
					Thread.sleep(serviceTime * 1000);

					// Finished serving the customer
					System.out.println("Clerk " + clerkID + " finished serving customer: " + customer.getName());

					// Place the customer into the appropriate buffer (buying or repairing)
					if (customer.getIndication().equals("buying"))
						buyingBuffer.add(customer);
					else if (customer.getIndication().equals("repairing"))
						if (repairingBuffer.size() < repairingBufferCapacity)
							System.out.println(
									"Repairing buffer is full. Customer " + customer.getName() + " cannot be added.");
					repairingBuffer.add(customer);
				}
			}

		} catch (InterruptedException e) {
			// Handle interrupted exception if needed
			e.getMessage();
		}
	}

}
