package src.main.shooter.game.entities;

public interface GravitationalEntity extends KineticEntity {
    public static final double DEFAULT_GRAVITY = -0.001;

    /**
     * Standardly, will return GravitationalEntity.DEFAULT_GRAVITY
     * 
     * @return Gravitational force in terms of dist/(tick sqrd).
     */
    public default double getGravitationalForce() {
        return DEFAULT_GRAVITY;
    }

    public default void applyGravity() {
        shiftYVel(getGravitationalForce());
    }
}
