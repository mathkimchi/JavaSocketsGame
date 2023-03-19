package src.main.shooter.game.entities;

public class Vector2D {
    private double x, y;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double incX(final double dX) {
        return this.x += dX;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double incY(final double dY) {
        return this.y += dY;
    }

    @Override
    public String toString() {
        return "Vector2D [x=" + x + ", y=" + y + "]";
    }
}
