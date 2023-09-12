
public class Customer implements Runnable {

	private String name;
	private String indication;
	private int arrivalTime;

	public Customer(String name, String indication, int arrivalTime, Clerk clerk) throws InvalidInputException {
		if (isValidIndication()) {
			this.name = name;
			this.indication = indication;
			this.arrivalTime = arrivalTime;
		} else
			throw new InvalidInputException("indication must be either 'buying' or 'repairing'");
	}

	private boolean isValidIndication() {
		return indication.equals("buying") || indication.equals("repairing");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(arrivalTime * 1000);
			Clerk.addCustomer(this);
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


}
