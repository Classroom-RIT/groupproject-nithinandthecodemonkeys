package rushhour.model;

public class RushHourException extends Exception {
    // print custom exception
    public RushHourException(String errorMessage) {
        super(errorMessage);
    }

    public RushHourException() {
    }

    // prints when a horizontal vehicle tries to move up or down and vice versa
    public void VehicleOrientationException() {
        System.out.println("Vehicle can't move in that direction");
    }

    // vehicle is already occupying the space the user wants to move to
    public void VehicleOccupyingSpaceException() {
        System.out.println("A vehicle is already occupying that space.");
    }

    // vehicle tries to move off the grid
    public void VehicleOffGridException() {
        System.out.println("Your vehicle can't move off the grid.");
    }

    // invalid vehicle symbol ;(
    public void InvalidVehicleSymbol() {
        System.out.println("Invalid vehicle symbol!");
    }

}
