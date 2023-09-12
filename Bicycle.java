public class Bicycle extends Scooter {
    private int isFoldable;

    public Bicycle(String modelName, double maxSpeed, double weight, int isFoldable) throws InvalidInputException {
        super(modelName, maxSpeed, weight);
        if (isValidFoldable(isFoldable)) {
            this.isFoldable = isFoldable;
        } else {
            throw new InvalidInputException("Must insert 1 or 0 in isFoldable");
        }
    }

    private boolean isValidFoldable(int isFoldable) {
        return isFoldable == 0 || isFoldable == 1;
    }
}