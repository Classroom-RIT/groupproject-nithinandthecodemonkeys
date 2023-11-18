package rushhour.view;

import rushhour.model.Vehicle;

public interface RushHourObserver {
    void updateMove(Vehicle vehicle);
    void updateGameStatus(String status);
}
