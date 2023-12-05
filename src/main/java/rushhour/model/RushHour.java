package rushhour.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import rushhour.view.RushHourObserver;

public class RushHour {
    public int BOARD_DIM = 6;
    public char RED_SYMBOL = 'R';
    public char EMPTY_SYMBOL = '-';
    public int MOVE_COUNT = 0;
    char[][] board; // Create a 2d array for the game board
    public Position EXIT_POS = new Position(2, 5);

    private Collection<RushHourObserver> observers = new ArrayList<>();
    private ArrayList<Vehicle> carList;

    private TreeMap<Character, Vehicle> carMap;

    public void registerObserver(RushHourObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Vehicle vehicle) {
        for (RushHourObserver observer : observers) {
            observer.updateMove(vehicle);
        }
    }

    public RushHour(String filename) {
        board = new char[BOARD_DIM][BOARD_DIM];
        carMap = new TreeMap<>();
        carList = new ArrayList<>();
        fillboard(filename);
    }

    public char[][] getBoard() {
        return board.clone(); // return a copy to prevent modifications
    }

    public void fillboard(String filename) {
        this.observers = new ArrayList<>();
        ArrayList<Vehicle> carList = new ArrayList<>();

        try (FileReader file = new FileReader(filename);
                BufferedReader reader = new BufferedReader(file)) {
            String line = reader.readLine();
            while (line != null) {
                String[] vehicle = line.split(",");
                char symbol = vehicle[0].charAt(0);

                int backRow = Integer.parseInt(vehicle[1]);
                int backCol = Integer.parseInt(vehicle[2]);
                int frontRow = Integer.parseInt(vehicle[3]);
                int frontCol = Integer.parseInt(vehicle[4]);

                Position back = new Position(backRow, backCol);
                Position front = new Position(frontRow, frontCol);

                Vehicle newCar = new Vehicle(symbol, back, front);
                carList.add(newCar);
                carMap.put(symbol, newCar);

                line = reader.readLine();
            }
        } catch (IOException ioe) {
            System.out.println("Invalid File.");
        }

        // Clear the board before filling it
        resetBoard();

        // Place cars on the board
        for (Vehicle car : carList) {
            char symbol = car.getSymbol();
            Position back = car.getBack();
            Position front = car.getFront();

            int startRow = Math.min(back.getRow(), front.getRow());
            int startCol = Math.min(back.getCol(), front.getCol());
            int endRow = Math.max(back.getRow(), front.getRow());
            int endCol = Math.max(back.getCol(), front.getCol());

            for (int i = startRow; i <= endRow; i++) {
                for (int j = startCol; j <= endCol; j++) {
                    board[i][j] = symbol;
                }
            }
        }
    }

    // find where the chosen car is
    public Position findVehiclePosition(char chosenVehicle) {
        int rows = board.length;
        int cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
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

    public void moveVehicle(Move move) throws RushHourException, Exception {
        // Print the board before the move
        System.out.println("Board before move:");
        printBoard();

        if (carMap.containsKey(move.getSymbol())) {
            Vehicle car = carMap.get(move.getSymbol());
            try {
                List<Position> occupiedSpots = getOccupiedSpots();
                Move validMove = car.move(move.getDirection(), occupiedSpots, carList);
                if (validMove != null) {
                    MOVE_COUNT++; // increment the moveCount

                    // Update the game board after a valid move
                    updateBoard();

                    // Print the board after the move
                    System.out.println("Board after move:");
                    printBoard();

                    notifyObservers(car); // notify the observer if the move goes through
                } else {
                    // if the move isn't in the list of possible moves, throw an error
                    throw new RushHourException("Invalid move for vehicle " + move.getSymbol() + ".");
                }
            } catch (Exception e) {
                throw new RushHourException(
                        "Invalid move for vehicle " + move.getSymbol() + " in direction "
                                + move.getDirection().toString());
            }
        } else {
            throw new RushHourException("Vehicle with symbol " + move.getSymbol() + " not found.");
        }
    }

    private void updateBoard() {
        // Clear the board before filling it
        resetBoard();

        // Place cars on the board
        for (Vehicle car : carList) {
            char symbol = car.getSymbol();
            Position back = car.getBack();
            Position front = car.getFront();

            int startRow = Math.min(back.getRow(), front.getRow());
            int startCol = Math.min(back.getCol(), front.getCol());
            int endRow = Math.max(back.getRow(), front.getRow());
            int endCol = Math.max(back.getCol(), front.getCol());

            for (int i = startRow; i <= endRow; i++) {
                for (int j = startCol; j <= endCol; j++) {
                    board[i][j] = symbol;
                }
            }
        }
    }

    public List<Position> getOccupiedSpots() {
        List<Position> occupiedSpots = new ArrayList<>();

        for (Vehicle car : carList) {
            if (car.isHorizontal()) {
                for (int col = car.getBack().getCol(); col <= car.getFront().getCol(); col++) {
                    occupiedSpots.add(new Position(car.getBack().getRow(), col));
                }
            } else {
                for (int row = car.getBack().getRow(); row <= car.getFront().getRow(); row++) {
                    occupiedSpots.add(new Position(row, car.getBack().getCol()));
                }
            }
        }
        return occupiedSpots;
    }

    public boolean isGameOver() {
        // check if the board has at least 3 rows and 6 columns
        if (board.length >= 3 && board[0].length >= 6) {
            if (board[2][5] == RED_SYMBOL) {
                Vehicle winningVehicle = carMap.get(RED_SYMBOL);
                notifyObservers(winningVehicle);
                return true;
            }
        }
        return false;
    }

    public Collection<Move> getPossibleMoves() {
        Collection<Move> possibleMoves = new HashSet<>();

        // for (Vehicle vehicle : ) {
        // for (Direction direction : Direction.values()) {
        // Move move = new Move(vehicle.getSymbol(), direction);

        // try {
        // vehicle.move(direction);
        // possibleMoves.add(move);
        // }
        // catch (RushHourException ignored) {
        // // Move is not valid, so ignore and continue checking other directions.
        // }
        // }
        // }

        return possibleMoves;
    }

    public int getMoveCount() {
        return MOVE_COUNT;
    }

    public void resetBoard() {
        int rows = board.length;
        int cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = EMPTY_SYMBOL;
            }
        }
    }

    public boolean solve() throws RushHourException, Exception {
        // Create a copy of the current board
        char[][] originalBoard = getBoard();

        // Perform backtracking to find a solution
        boolean success = backtrack();

        // Restore the original board
        board = originalBoard;

        return success;
    }

    private boolean backtrack() throws RushHourException, Exception {
        if (isGameOver()) {
            return true; // Solution found
        }

        // Iterate through possible moves
        Collection<Move> possibleMoves = getPossibleMoves();
        for (Move move : possibleMoves) {
            moveVehicle(move);

            // Recursively explore the next state
            if (backtrack()) {
                return true;
            }

            // Undo the move if it doesn't lead to a solution
            undoMove(move);
        }

        return false; // No solution found
    }

    private void undoMove(Move move) {
        char symbol = move.getSymbol();
        Direction direction = move.getDirection();

        try {
            // Find where the car symbol from the move is on the board
            Position vehiclePos = findVehiclePosition(symbol);

            if (vehiclePos != null) {
                int row = vehiclePos.getRow();
                int col = vehiclePos.getCol();

                if (direction == Direction.LEFT) {
                    // Undo the move to the left
                    board[row][col] = EMPTY_SYMBOL;
                    board[row][col + 1] = symbol;
                } else if (direction == Direction.RIGHT) {
                    // Undo the move to the right
                    board[row][col] = symbol;
                    board[row][col + 2] = EMPTY_SYMBOL;
                } else if (direction == Direction.UP) {
                    // Undo the move upwards
                    board[row][col] = EMPTY_SYMBOL;
                    board[row + 1][col] = symbol;
                } else if (direction == Direction.DOWN) {
                    // Undo the move downwards
                    board[row + 2][col] = EMPTY_SYMBOL;
                    board[row][col] = symbol;
                }

                MOVE_COUNT--;
            } else {
                throw new RushHourException("Invalid move. The specified vehicle symbol was not found on the board.");
            }

            // Notify observers about the undo move
            Position frontPos;
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                frontPos = new Position(vehiclePos.getRow(), vehiclePos.getCol() + 2);
            } else {
                frontPos = new Position(vehiclePos.getRow() + 2, vehiclePos.getCol());
            }
            notifyObservers(new Vehicle(symbol, vehiclePos, frontPos));
        } catch (RushHourException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void parseCommand(RushHour rushHour, String command) throws Exception {
        // check for help
        if (command.equals("help") || command.equals("Help")) {
            System.out.println("Help Menu:\n" + //
                    "        help - this menu\n" + //
                    "        quit - quit\n" + //
                    "        hint - display a valid move\n" + //
                    "        reset - reset the game\n" + //
                    "        <symbol> <UP|DOWN|LEFT|RIGHT> - move the vehicle one space in the given direction");
            // check for move
        } else if (command.equals("move") || command.equals("Move")) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print("Enter symbol and direction (e.g., A UP): ");

                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();

                    String[] parts = input.split(" ");
                    if (parts.length == 2) {
                        char symbol = parts[0].charAt(0);
                        Direction direction = Direction.valueOf(parts[1].toUpperCase());

                        Move move = new Move(symbol, direction);
                        rushHour.moveVehicle(move);
                    } else {
                        System.out.println("Invalid input format. Please use the format 'Symbol Direction'!");
                    }
                } else {
                    System.out.println("No input entered. Please try again.");
                }
            } finally {
                // scanner.close(); // Close the scanner when you're done with it
            }

            // check for solve
        } else if (command.equals("solve") || command.equals("Solve")) {
            if (rushHour.solve()) {
                System.out.println("Solution found!");
                rushHour.printBoard();
            } else {
                System.out.println("No solution found.");
            }
            // check for reset
        } else if (command.equals("reset") || command.equals("Reset")) {
            System.out.println("Clearing board...");
            System.out.println("New Game");
            rushHour.resetBoard();
            rushHour.fillboard("data/03_00.csv");
        } else {
            System.out.println("Not a recognized command or format!");
        }
    }

    public static RushHour startGame() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a Rush Hour filename: ");
            String filename = scanner.nextLine();
            // String filename = "data/03_00.csv";
            scanner.close();
            RushHour rushHour = new RushHour(filename);
            System.out.println("Type 'help' for the help menu.");
            rushHour.printBoard();
            return rushHour;
        }
    }

    public static void main(String[] args) throws Exception {
        // RushHour rushHour = startGame();
        String command;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a Rush Hour filename: ");
            String filename = scanner.nextLine();
            // String filename = "data/03_00.csv";
            RushHour rushHour = new RushHour(filename);
            System.out.println("Type 'help' for the help menu.");
            rushHour.printBoard();

            while (!rushHour.isGameOver()) {
                // Scanner scanner = new Scanner(System.in);
                System.out.print("> ");
                command = scanner.nextLine();
                rushHour.parseCommand(rushHour, command);
                rushHour.printBoard();
            }
        }

    }
}