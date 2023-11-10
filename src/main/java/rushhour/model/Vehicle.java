package rushhour.model;

public class Vehicle {
    private char symbol;
    private Position back;
    private Position front;

    public Vehicle(char symbol, Position back, Position front) {
        this.symbol = symbol;
        this.back = back;
        this.front = front;
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
}
