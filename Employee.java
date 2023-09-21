
public abstract class Employee {

	private String id;
	protected int customerCount;

	public Employee(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}
	
	public int getCustomerCount() {
		return customerCount;
	}
	
	protected abstract void sendToCashier(Customer customer);

	
}
