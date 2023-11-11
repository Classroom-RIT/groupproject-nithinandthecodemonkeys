package rushhour.model;

public class Vehicle {
    private char symbol;
    private Position back;
    private Position front;

    public Vehicle(char symbol, Position back, Position front) {
        this.symbol = symbol;
        this.back = back;
        this.front = front;

        // for horizontal vehicle back.row == front.row && back.col < front.col
        // for vertical vehicle back.row < front.row && back.col == front.col

        //
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

    public void move(Direction dir) {
        // if vehicle is horizontal:
        // if move direction is right or left
        // if move position meets back.row == front.row && back.col < front.col
        // move vehicle
        // else
        // throw exception VehicleOrientationException

        // else vehicle is vertical:
        // if move direction is up or down
        // for vertical vehicle back.row < front.row && back.col == front.col
        // move vehicle
        // else
        // throw exception VehicleOrientationException

    }
}
