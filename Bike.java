public class Bike extends ElectricVehicle {
	private int isClosing;

	public Bike(String modelName, double maxSpeed, double weight, int isClosing, int price, int inventory)
			throws InvalidInputException {
		super("Bike", modelName, maxSpeed, weight, price, inventory);
		if (isValidFoldable(isClosing)) {
			this.isClosing = isClosing;
		} else {
			throw new InvalidInputException("Must insert 1 or 0 in isFoldable");
		}
	}

	private boolean isValidFoldable(int isFoldable) {
		return isFoldable == 0 || isFoldable == 1;
	}
}
