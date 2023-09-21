import java.util.*;

public class SeniorTechnician extends Employee implements Runnable {

	private String id;
	private boolean isMaster;
	private Random random = new Random();
	private int customerCount;
	private static volatile boolean isDayOver = false;

	public SeniorTechnician(String id, boolean isMaster) {
		super(id);
		this.isMaster = isMaster;
		this.customerCount = 0;
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
			int cost = 0;

			if (!customer.hasReturnedFromManager()) {

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
				}

				customer.setCost(cost);

				if (!this.isMaster && cost > 450 && random.nextDouble() < 0.9) {
					System.out.println("Customer " + customer.getName()
							+ "is very dissatisfied. Transferring to customer manager.");
					CustomerManager.addToManagerQueue(customer);
				} else {

					if (cost >= 50 && cost < 300) {
						Thread.sleep(1000);
					} else if (cost >= 300 && cost < 450) {
						Thread.sleep(2000);
					} else if (cost >= 450 && cost <= 800) {
						Thread.sleep(3000);
					}
				}
			} else {
				Thread.sleep(1000);
			}
			System.out.println("Customer " + customer.getName() + " treatment completed. Cost: " + cost + " NIS");
			SummaryDetails doc = new SummaryDetails(customer.getName(), customer.getIndication(), this, cost);
			sendToCashier(customer);

		}
	}

	protected static void setSeniorDayOver() {
		synchronized (SeniorTechnician.class) {
			isDayOver = true;

		}
	}

	protected void sendToCashier(Customer customer) {
		this.customerCount = this.customerCount + 1;
		SummaryDetails doc = new SummaryDetails(customer.getName(), customer.getIndication(), this, customer.getCost());
		Cashier.addToQueue(doc);
	}
	

}
