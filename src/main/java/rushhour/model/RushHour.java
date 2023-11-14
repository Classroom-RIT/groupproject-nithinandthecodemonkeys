package rushhour.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RushHour {
    public int BOARD_DIM = 6;
    public char RED_SYMBOL = 'R';
    public char EMPTY_SYMBOL = '-';
    public int MOVE_COUNT = 0;
    char[][] board; // create a 2d array for the game board
    public Position EXIT_POS = new Position(2, 5);

    public RushHour(String filename) {
        fillboard(filename);
    }

    public void fillboard(String filename) {
        String csvFile = "data/" + filename;
        String line;
        String splitByCommas = ","; // separate values by commas

        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            int rows = 0;
            int cols = 0;

            // find the number of rows and columns in the csv file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(splitByCommas);
                rows++;
                cols = Math.max(cols, values.length);
            }

            // reset the file reader to read from the beginning of the file
            br.close();
            br = new BufferedReader(new FileReader(csvFile));

            // create the 2d array with dynamic dimensions
            char[][] board = new char[rows][cols];

            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(splitByCommas);

                // populate the 2d array with the csv values
                for (int col = 0; col < values.length; col++) {
                    board[row][col] = values[col].charAt(0); // Convert the string to char
                }

                row++; // move to the next row in the 2d array
            }

            this.board = board;

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // find where the red car is
    public Position findVehiclePosition(char chosenVehicle) {
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                if (board[i][j] == chosenVehicle) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public void printBoard() {
        for (char[] rowArray : this.board) {
            for (char value : rowArray) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    // Maybe make a helper function to find out the orientation and direction of the
    // move

    public void moveVehicle(Move move) {
        Position vehiclePosition = findVehiclePosition(EMPTY_SYMBOL)
        // if another vehicle occupying the space that the vehicle would move to
        // throw rushhourexception
        /*
         * 1) Find where the car symbol from the move is on the board
         * 2) Find out the the orientation and which the direction the move is so you
         * know which side of the car to look at
         * 3) Then finally check if the space next to the car is occupied aka if the
         * space is not empty
         */

        // if else the vehicle would move off the grid
        // throw rushhourexception
        // if else an invalid direction is given, e.g. Direction.UP is specified for a
        // horizontal vehicle
        // throw rushhourexception
        // if else an invalid vehicle symbol was given
        // throw rushhourexception
        // else
        // move vehicle

        MOVE_COUNT++;
    }

    public boolean isGameOver() {
        if (board[2][5] == RED_SYMBOL) {
            return true;
        } else {
            return false;
        }
    }

    public Collection<Move> getPossibleMoves() {
        return null;
    }

    public int getMoveCount() {
        return MOVE_COUNT;
    }

    public void resetBoard() {
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                board[i][j] = EMPTY_SYMBOL;
            }
        }
    }

    public void parseCommand(RushHour rushHour, String command, String action) {
        if(command == "help") {
            System.out.println("Help Menu:\n" + //
                    "        help - this menu\n" + //
                    "        quit - quit\n" + //
                    "        hint - display a valid move\n" + //
                    "        reset - reset the game\n" + //
                    "        <symbol> <UP|DOWN|LEFT|RIGHT> - move the vehicle one space in the given direction");
        } else if(command == "move") {
            rushHour.moveVehicle(null);
        } else if(command == "reset")
            rushHour.resetBoard();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a Rush Hour filename: ");
            String filename = scanner.nextLine();
            RushHour rushHour = new RushHour(filename);
            System.out.println("Type 'help' for help menu.");
            rushHour.printBoard();
            
        }

    }
}