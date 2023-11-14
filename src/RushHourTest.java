import static org.junit.Assert.*;
import org.junit.Test;

public class RushHourTest {

    private String testFile = "03_01.csv";

    @Test
    public void testFillBoard() {
        RushHour rushHour = new RushHour(testFile);
        assertEquals(6, rushHour.BOARD_DIM);
        assertEquals('R', rushHour.RED_SYMBOL);
        assertEquals('-', rushHour.EMPTY_SYMBOL);
        assertEquals(0, rushHour.MOVE_COUNT);
        assertNotNull(rushHour.board);
        assertEquals(new Position(2, 5), rushHour.EXIT_POS);
    }

    @Test
    public void testFindVehiclePosition() {
        RushHour rushHour = new RushHour(testFile);
        Position position = rushHour.findVehiclePosition('A');
        assertNotNull(position);
        assertEquals(2, position.getRow());
        assertEquals(0, position.getCol());
    }

    @Test
    public void testMoveVehicle() {
        RushHour rushHour = new RushHour(testFile);
        Move move = new Move('A', Direction.RIGHT);
        rushHour.moveVehicle(move);
        assertEquals(1, rushHour.MOVE_COUNT);
    }

    @Test
    public void testIsGameOver() {
        RushHour rushHour = new RushHour(testFile);
        assertFalse(rushHour.isGameOver());
    }

    @Test
    public void testResetBoard() {
        RushHour rushHour = new RushHour(testFile);
        rushHour.moveVehicle(new Move('A', Direction.RIGHT));
        rushHour.resetBoard();
        assertEquals(0, rushHour.MOVE_COUNT);
    }

    @Test
    public void testParseCommand() {
        RushHour rushHour = new RushHour(testFile);
    }
}
