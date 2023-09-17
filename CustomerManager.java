import java.util.Random;

public class CustomerManager implements Runnable {

	private String id;
	private volatile boolean isDayOver = false;
	private int totalCustomers;
	private static int customersHandled = 0;
	private static Queue<Customer> managerQueue = new Queue<Customer>();

	public CustomerManager(String id, int totalCustomers) {
		this.id = id;
		this.totalCustomers = totalCustomers;
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

			while (managerQueue.isEmpty()) {
				System.out.println("Customer Manager " + id + " is waiting for customers.");
				wait();
			}

			if (isDayOver) {
				break;
			}

			Customer customer = managerQueue.remove();
			double randomValue = new Random().nextDouble();

			if (customer.getIndication().equals("repairment")) {
				// 10% received a 50 NIS discount and returned to the senior technicians’ queue
				if (randomValue < 0.1) {
					System.out.println("Customer " + customer.getName()
							+ " received a 50 NIS discount and returned to the senior technicians’ queue.");
					customer.setRepairCost(customer.getCost() - 50);
					customer.setReturnedFromManager();
					JuniorTechnician.addCustomerToSenior(customer);
				} else if (randomValue < 0.4) {
					// 30% transferred to the cashier to leave the store
					System.out.println("Customer " + customer.getName() + " decided to leave the store.");
					// Add the customer to the Cashier queue here.
				} else {
					System.out
							.println("Customer " + customer.getName() + " returned to the senior technicians’ queue.");
					JuniorTechnician.addCustomerToSenior(customer);
				}
			} else if (customer.getIndication().equals("purchesing")) {
				if (randomValue < 0.7) {
					System.out.println("Customer " + customer.getName() + " decided to buy with a 100 NIS discount.");
					customer.setReturnedFromManager();
					customer.setRepairCost(customer.getCost() - 100);
					Clerk.addCustomerToSales(customer);
				} else {
					// The remaining customers were transferred to the cashier's queue to leave the
					// store
					customer.setRepairCost(-1);
					System.out.println("Customer " + customer.getName() + " decided to leave the store.");
					// Add the customer to the Cashier queue here.
				}
			}

			if (customersHandled == totalCustomers) {
				// Inform other components that the day is over
				setManagerDayOver();
				Clerk.setClerkDayOver();
				JuniorTechnician.setJuniorDayOver();
				SeniorTechnician.setSeniorDayOver();
				Salesman.setSalesmanDayOver();

			}
		}
	}

	protected static void addToManagerQueue(Customer customer) {
		synchronized (CustomerManager.class) {
			managerQueue.add(customer);
			CustomerManager.class.notifyAll();
		}
	}

	protected static synchronized void addHandledCustomer() {
		customersHandled = customersHandled + 1;
	}

	private void setManagerDayOver() {
		isDayOver = true;
	}

}
