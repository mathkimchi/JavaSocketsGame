package src.main.shooter.game.entities;

/**
 * Something with position and velocity.
 */
public interface KineticEntity {
    public void setX(double newX);

    public void setY(double newY);

    public void setXVel(double newXVel);

    public void setYVel(double newYVel);

    public double getX();

    public double getXVel();

    public double getY();

    public double getYVel();

    /**
     * @param dX shift in x
     * @return new x
     */
    public default double shiftX(final double dX) {
        setX(getX() + dX);
        return getX();
    }

    /**
     * @param dXVel shift in xVel
     * @return new xVel
     */
    public default double shiftXVel(final double dXVel) {
        setXVel(getXVel() + dXVel);
        return getXVel();
    }

    /**
     * @param dY shift in y
     * @return new y
     */
    public default double shiftY(final double dY) {
        setY(getY() + dY);
        return getY();
    }

    /**
     * @param dYVel shift in yVel
     * @return new yVel
     */
    public default double shiftYVel(final double dYVel) {
        setYVel(getYVel() + dYVel);
        return getYVel();
    }

    public default void applyVelocity() {
        shiftX(getXVel());
        shiftY(getYVel());
    }
}
