package rushhour.model;

public class Move {
    private char symbol;
    private Direction dir;

    public Move(char symbol, Direction dir) {
        this.symbol = symbol;
        this.dir = dir;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public Direction getDirection() {
        return this.dir;
    }
}
