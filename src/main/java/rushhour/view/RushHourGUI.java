package rushhour.view;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.Vehicle;

public class RushHourGUI extends Application implements RushHourObserver {
    private RushHour rushHour;
    private GridPane gridPane;
    private Button[][] buttons;
    // hold all of our vehicle colors in an array
    private static String[] colors = new String[] { "#FF4000", "#FF7E00", "#FFDB00", "#CAFA00", "#00FAA0", "#00FCD4",
            "#00CBFF", "#0079FF", "#0433FF",
            "#ED3EFF", "#FF309A", "#FF2E88" };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        rushHour = new RushHour("03_00.csv");
        System.out.println("BOARDDDDDDD: " + rushHour.getBoard().toString());
        rushHour.registerObserver(this);

        primaryStage.setTitle("Rush Hour Game");

        gridPane = new GridPane();
        buttons = new Button[rushHour.BOARD_DIM][rushHour.BOARD_DIM];

        initializeGrid();
        updateGrid();

        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGrid() {
        for (int i = 0; i < rushHour.BOARD_DIM; i++) {
            for (int j = 0; j < rushHour.BOARD_DIM; j++) {
                Button button = new Button();
                button.setStyle("-fx-background-radius: 0");
                button.setMinSize(50, 50);

                final int finalI = i;
                final int finalJ = j;

                button.setOnAction(event -> handleButtonClick(finalI, finalJ));
                buttons[i][j] = button;
                gridPane.add(button, j, i);
            }
        }
    }

    private void updateGrid() {
        char[][] board = rushHour.getBoard();
        for (int i = 0; i < rushHour.BOARD_DIM; i++) {
            for (int j = 0; j < rushHour.BOARD_DIM; j++) {
                buttons[i][j].setText(String.valueOf(board[i][j]));

                if (board[i][j] != 'R') {
                    buttons[i][j].setStyle("-fx-background-color: " + colors[i] + "; -fx-font-size: 18;");
                    System.out.println(colors[i]);
                } else {
                    buttons[i][j].setStyle("-fx-background-color: red; -fx-font-size: 18;");
                }
                // switch (board[i][j]) {
                // case 'R':
                // buttons[i][j].setStyle("-fx-background-color: red; -fx-font-size: 18;");
                // break;
                // case '-':
                // buttons[i][j].setStyle("-fx-background-color: white; -fx-font-size: 18;");
                // break;
                // // Add more cases for other characters if needed
                // }
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        char[][] board = rushHour.getBoard();
        char symbol = board[row][col];

        // check if the clicked position contains a vehicle
        if (symbol != '-') {
            // allow the user to choose the direction to move the vehicle
            showMoveDialog(symbol);
        }
    }

    private Direction getDirectionFromButton(ButtonType buttonType) {
        // Map the chosen button type to the corresponding direction
        switch (buttonType.getText().toLowerCase()) {
            case "up":
                return Direction.UP;
            case "down":
                return Direction.DOWN;
            case "left":
                return Direction.LEFT;
            case "right":
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    private void showMoveDialog(char symbol) {

        // use alert as our modal window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Move Vehicle");
        alert.setHeaderText("Choose a direction to move the vehicle " + symbol);

        ButtonType upButton = new ButtonType("Up");
        ButtonType downButton = new ButtonType("Down");
        ButtonType leftButton = new ButtonType("Left");
        ButtonType rightButton = new ButtonType("Right");

        alert.getButtonTypes().setAll(upButton, downButton, leftButton, rightButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            // User chose a direction, move the vehicle accordingly
            Direction direction = getDirectionFromButton(result.get());
            Move move = new Move(symbol, direction);
            rushHour.moveVehicle(move);
        }
    }

    @Override
    public void updateMove(Vehicle vehicle) {
        // handle the update when a move occurs
        updateGrid();
    }

    @Override
    public void updateGameStatus(String status) {
        // handle the update when the game state changes
        if (rushHour.isGameOver()) {
            showAlert("Congratulations! You've won the game!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
