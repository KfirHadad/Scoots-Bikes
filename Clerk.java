import java.util.Random;

public class Clerk implements Runnable {

	private String id;
	private Random random = new Random();
	private boolean isDayOver = false;
	private static Queue<Customer> clerkQueue = new Queue<Customer>();
	private static Queue<Customer> buyingBuffer = new Queue<Customer>();
	private static BoundedQueue<Customer> repairingBuffer = new BoundedQueue<Customer>();

	public Clerk(String clerkID) {
		this.id = clerkID;
	}

	@Override
	public void run() {
		try {
			while (!isDayOver) {
				synchronized (this) {
					// Check if there are customers in the queue; if not, wait
					if (clerkQueue.isEmpty()) {
						System.out.println("Clerk " + id + " is waiting for customers.");
						wait(); // Wait for a customer to be added
					}

					// Dequeue a customer from the list
					Customer customer = clerkQueue.remove();

					// Clerk starts servicing the customer
					System.out.println("Clerk " + id + " is serving customer: " + customer.getName());

					// Random service time between 3 and 6 seconds
					int serviceTime = random.nextInt(4) + 3;
					Thread.sleep(serviceTime * 1000);

					// Finished serving the customer
					System.out.println("Clerk " + id + " finished serving customer: " + customer.getName());

					// Place the customer into the appropriate buffer (buying or repairing)
					if (customer.getIndication().equals("buying"))
						buyingBuffer.add(customer);
					else if (customer.getIndication().equals("repairing")) {
						repairingBuffer.add(customer);
					}
				}
			}

		} catch (InterruptedException e) {
			// Handle interrupted exception if needed
			e.getMessage();
		}
	}

	public static void addCustomerToClerk(Customer customer) {
		synchronized (Clerk.class) {
			clerkQueue.add(customer);
			Clerk.class.notify();
		}
	}

	public static Queue<Customer> getBuyingBuffer() {
		return buyingBuffer;
	}

	public static BoundedQueue<Customer> getRepairingBuffer() {
		return repairingBuffer;
	}

	protected void setClerkDayOver() {
		isDayOver  = true; 
	}
}
