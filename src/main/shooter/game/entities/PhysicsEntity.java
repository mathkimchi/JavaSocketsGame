package src.main.shooter.game.entities;

public interface PhysicsEntity {
    public void setY(double newY);

    public void incY(double incAmount);

    public double getY();

    public void setYVel(double newYVel);

    public void incYVel(double newYVel);

    public double getYVel();

    public boolean hasGravity();

    public boolean shouldBounce(PhysicsEntity entity);
}
