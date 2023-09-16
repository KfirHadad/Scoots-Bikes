
public class SummaryDetails {

	private String name;
	private String indication;
	private String employee;
	private int total;

	protected SummaryDetails(String name, String indication, String employee, int total) {
		this.name = name;
		this.indication = indication;
		this.employee = employee;
		this.total = total;
	}

	@Override
	public String toString() {
		return "Customer " + name + " who came for " + indication + " service, finished with a " + employee
				+ " and paid a total of only " + total + "NIS";
	}
}
