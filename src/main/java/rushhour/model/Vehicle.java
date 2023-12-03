package rushhour.model;

import java.util.List;

public class Vehicle {
    private char symbol;
    private Position back;
    private Position front;
    private Orientation orientation;
    // public RushHourException exception;

    public Vehicle(char symbol, Position back, Position front) {
        this.symbol = symbol;
        this.back = back;
        this.front = front;

        // for horizontal vehicle back.row == front.row && back.col < front.col
        if ((this.back.getCol() < this.front.getCol()) && (this.back.getRow() == this.front.getRow())) {
            this.orientation = Orientation.HORIZONTAL;
        }
        // for vertical vehicle back.row < front.row && back.col == front.col
        else {
            this.orientation = Orientation.VERTICAL;
        }

    }

    public char getSymbol() {
        return this.symbol;
    }

    public Position getBack() {
        return this.back;
    }

    public Position getFront() {
        return this.front;
    }

    public Orientation getOrientation() {
        return this.orientation;
    }

    public Move move(Direction direction, List<Position> occupiedSpots, List<Vehicle> vehicles) throws RushHourException {
        int backCol = this.back.getCol();
        int frontCol = this.front.getCol();
        int backRow = this.back.getRow();
        int frontRow = this.front.getRow();

        Move move = new Move(this.symbol, direction);

        // if vehicle is horizontal:
        // else
        // throw exception VehicleOrientationException
        if (this.orientation == Orientation.HORIZONTAL) {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                throw new RushHourException("Vehicle can't move in that direction");
            }
            // if move direction is right or left
            // if car is not on the edge of the grid, then move vehicle
            // for horizontal vehicle back.row == front.row && back.col < front.col
            else {
                if (direction == Direction.LEFT) {
                    if (backCol > 0) {
                        this.back.setCol(backCol - 1);
                        this.front.setCol(frontCol - 1);
                    }
                    // if the movement to the left would make the car go beyond the edge of the
                    // board then
                    // throw exception
                    else {
                        throw new RushHourException("Your vehicle can't move off the grid.");
                    }
                } else if (direction == Direction.RIGHT) {
                    if (frontCol < 5) {
                        this.back.setCol(backCol + 1);
                        this.front.setCol(frontCol + 1);
                    }
                    // if the movement to the right would make the car go beyond the edge of the
                    // board then
                    // throw exception
                    else {
                        throw new RushHourException("Your vehicle can't move off the grid.");
                    }
                }
            }
        }

        // else vehicle is vertical:
        else if (this.orientation == Orientation.VERTICAL) {
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                throw new RushHourException("Vehicle can't move in that direction");
            }
            // if move direction is up or down
            // if car is not on the edge of the grid, then move vehicle
            // for vertical vehicle back.row < front.row && back.col == front.col
            else {
                if (direction == Direction.UP) {
                    if (backRow < 5) {
                        this.back.setRow(backRow + 1);
                        this.front.setRow(frontRow + 1);
                    }
                    // if the movement to the left would make the car go beyond the edge of the
                    // board then
                    // throw exception
                    else {
                        throw new RushHourException("Your vehicle can't move off the grid.");
                    }
                } else if (direction == Direction.DOWN) {
                    if (frontRow > 0) {
                        this.back.setRow(backRow - 1);
                        this.front.setRow(frontRow - 1);
                    }
                    // if the movement to the down would make the car go beyond the edge of the
                    // board then
                    // throw exception
                    else {
                        throw new RushHourException("Your vehicle can't move off the grid.");
                    }
                }
            }
        }

        return move;
    }

    public boolean isHorizontal() {
        return this.orientation.equals(Orientation.HORIZONTAL);
    }
}
