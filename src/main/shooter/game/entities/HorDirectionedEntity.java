package src.main.shooter.game.entities;

public interface HorDirectionedEntity {
    public enum HorDirection {
        LEFT, RIGHT
    }

    public HorDirection getHorDirection();

    public void setHorDirection(HorDirection horDirection);
}
