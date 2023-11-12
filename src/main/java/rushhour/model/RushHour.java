package rushhour.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class RushHour {
    public int BOARD_DIM = 6;
    public char RED_SYMBOL = 'R';
    public char EMPTY_SYMBOL = '-';
    String[][] board; // create a 2d array for the game board
    public Position EXIT_POS = new Position(2, 5);

    public RushHour(String filename) {
        // fillboard()
    }

    public void fillboard(String filename) {
        // specify the path to the csv
        String csvFile = filename;
        String line;
        String splitByCommas = ","; // separate values by commas

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // define the number of rows and columns in the csv file
            int rows = 3; // number of rows in the csv file
            int cols = 5; // number of columns in the csv file
            String[][] board = new String[rows][cols];

            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(splitByCommas);

                // Populate the 2D array with the CSV values
                for (int col = 0; col < values.length; col++) {
                    board[row][col] = values[col];
                }

                row++; // move to the next row in the 2d array
            }

            for (String[] rowArray : board) {
                for (String value : rowArray) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
