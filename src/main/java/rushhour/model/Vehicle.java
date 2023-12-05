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

    public Move move(Direction direction, List<Position> occupiedSpots, List<Vehicle> vehicles)
            throws RushHourException {
        Move move = new Move(symbol, direction);

        if (this.orientation == Orientation.HORIZONTAL) {
            moveHorizontally(direction);
        } else if (this.orientation == Orientation.VERTICAL) {
            moveVertically(direction);
        }

        return move;
    }

    private void moveHorizontally(Direction direction) throws RushHourException {
        int backCol = this.back.getCol();
        int frontCol = this.front.getCol();

        if (direction == Direction.LEFT && backCol > 0) {
            this.back.setCol(backCol - 1);
            this.front.setCol(frontCol - 1);
        } else if (direction == Direction.RIGHT && frontCol < 5) {
            this.back.setCol(backCol + 1);
            this.front.setCol(frontCol + 1);
        } else {
            throw new RushHourException("Invalid move for vehicle " + this.symbol + ".");
        }
    }

    private void moveVertically(Direction direction) throws RushHourException {
        int backRow = this.back.getRow();
        int frontRow = this.front.getRow();

        if (direction == Direction.UP && backRow < 5) {
            this.back.setRow(backRow + 1);
            this.front.setRow(frontRow + 1);
        } else if (direction == Direction.DOWN && frontRow > 0) {
            this.back.setRow(backRow - 1);
            this.front.setRow(frontRow - 1);
        } else {
            throw new RushHourException("Invalid move for vehicle " + this.symbol + ".");
        }
    }

    public boolean isHorizontal() {
        return this.orientation.equals(Orientation.HORIZONTAL);
    }
}
