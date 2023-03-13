package src.main.shooter.game.action;

public class WalkAction extends Action {
    private double horizontalSpeed; // left is negative, right is positive

    public WalkAction(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }
}
