
public class SummaryDetails {

	private String name;
	private String indication;
	private Employee employee;
	private int total;
	

	private static JuniorTechnician junior1 = new JuniorTechnician("1", 1);
	private static JuniorTechnician junior2 = new JuniorTechnician("2", 2);
	private static JuniorTechnician junior3 = new JuniorTechnician("3", 3);
	private static SeniorTechnician senior1 = new SeniorTechnician("4", true);
	private static SeniorTechnician senior2 = new SeniorTechnician("5", false);
	private static Salesman sale1 = new Salesman("6");
	private static Salesman sale2 = new Salesman("7");

	protected SummaryDetails(String name, String indication, Employee employee, int total) {
		this.name = name;
		this.indication = indication;
		this.employee = employee;
		this.total = total;
	}

	@Override
	public String toString() {
		return "Customer " + name + " who came for " + indication + " service, finished with a " + employee.getID()
				+ " and paid a total of only " + total + "NIS";
	}

	public int getTotal() {
		return this.total;
	}
	
	public Employee getEmployee() {
		return this.employee;
	}
}
