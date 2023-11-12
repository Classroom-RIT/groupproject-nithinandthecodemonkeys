package rushhour.model;

import java.util.Collection;

public class RushHour {
    public int BOARD_DIM = 6;
    public char RED_SYMBOL = 'R';
    public char EMPTY_SYMBOL = '-';
    char[][] board; // create a 2d array for the game board
    public Position EXIT_POS = new Position(2, 5);

    public RushHour(String filename) {
        // fillboard()
    }

    public void moveVehicle(Move move) {
        // if another vehicle occupying the space that the vehicle would move to
        // throw rushhourexception
        // if else the vehicle would move off the grid
        // throw rushhourexception
        // if else an invalid direction is given, e.g. Direction.UP is specified for a
        // horizontal vehicle
        // throw rushhourexception
        // if else an invalid vehicle symbol was given
        // throw rushhourexception
        // else
        // move vehicle
    }

    public boolean isGameOver() {
        return false;
    }

    public Collection<Move> getPossibleMoves() {
    }

    public int getMoveCount() {
        return 0;
    }

}
