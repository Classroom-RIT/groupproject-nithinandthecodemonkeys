package rushhour.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
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

    public void moveVehicle(Move move) throws RushHourException {
        char symbol = move.getSymbol();
        Direction direction = move.getDirection();

        // 1) Find where the car symbol from the move is on the board
        Position vehiclePos = findVehiclePosition(symbol);

        // 2) Find out the orientation and direction of the move
        boolean isHorizontal = (direction == Direction.LEFT || direction == Direction.RIGHT);
        boolean isVertical = (direction == Direction.UP || direction == Direction.DOWN);

        // 3) Check if the space next to the car is occupied
        if (vehiclePos != null) {
            int row = vehiclePos.getRow();
            int col = vehiclePos.getCol();

            if (isHorizontal) {
                if (direction == Direction.LEFT) {
                    if (col - 1 >= 0 && board[row][col - 1] == EMPTY_SYMBOL) {
                        // Move the vehicle to the left
                        board[row][col - 1] = symbol;
                        board[row][col + 1] = EMPTY_SYMBOL;
                    } else {
                        throw new RushHourException("Invalid move. The space next to the vehicle is occupied.");
                    }
                } else { // Direction.RIGHT
                    if (col + 2 < BOARD_DIM && board[row][col + 2] == EMPTY_SYMBOL) {
                        // Move the vehicle to the right
                        board[row][col + 2] = symbol;
                        board[row][col] = EMPTY_SYMBOL;
                    } else {
                        throw new RushHourException("Invalid move. The space next to the vehicle is occupied.");
                    }
                }
            } else if (isVertical) {
                if (direction == Direction.UP) {
                    if (row - 1 >= 0 && board[row - 1][col] == EMPTY_SYMBOL) {
                        // Move the vehicle up
                        board[row - 1][col] = symbol;
                        board[row + 1][col] = EMPTY_SYMBOL;
                    } else {
                        throw new RushHourException("Invalid move. The space next to the vehicle is occupied.");
                    }
                } else { // Direction.DOWN
                    if (row + 2 < BOARD_DIM && board[row + 2][col] == EMPTY_SYMBOL) {
                        // Move the vehicle down
                        board[row + 2][col] = symbol;
                        board[row][col] = EMPTY_SYMBOL;
                    } else {
                        throw new RushHourException("Invalid move. The space next to the vehicle is occupied.");
                    }
                }
            }
        } else {
            throw new RushHourException("Invalid move. The specified vehicle symbol was not found on the board.");
        }

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
        if (command == "help") {
            System.out.println("Help Menu:\n" + //
                    "        help - this menu\n" + //
                    "        quit - quit\n" + //
                    "        hint - display a valid move\n" + //
                    "        reset - reset the game\n" + //
                    "        <symbol> <UP|DOWN|LEFT|RIGHT> - move the vehicle one space in the given direction");
        } else if (command == "move") {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter symbol and direction (e.g., A UP): ");
            String input = scanner.nextLine();

            // Split the input into symbol and direction
            String[] parts = input.split(" ");
            if (parts.length == 2) {
                char symbol = parts[0].charAt(0);
                Direction direction = Direction.valueOf(parts[1].toUpperCase());

                // Create a Move object
                Move move = new Move(symbol, direction);
                rushHour.moveVehicle(move);

                // System.out.println("Symbol: " + move.getSymbol());
                // System.out.println("Direction: " + move.getDirection());
            } else {
                System.out.println("Invalid input format. Please use the format 'Symbol Direction'.");
            }

            scanner.close();
        } else if (command == "reset")
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