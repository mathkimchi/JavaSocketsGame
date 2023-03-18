package src.main.shooter.game;

import java.io.Serializable;

import src.main.shooter.game.action.ActionSet;

public class Entity implements Serializable {
    private static final long serialVersionUID = -1816334362202070857L;
    private final int id;
    private final double width, height;
    private double x, y; // bottom left corner, not center
    private Direction direction;

    public enum Direction {
        LEFT, RIGHT
    }

    private ActionSet actionSet;

    public Entity(final int id, final double width, final double height, final double x, final double y,
            final Direction direction) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.direction = direction;

        actionSet = new ActionSet();
    }

    public ActionSet getActionSet() {
        return actionSet;
    }

    public int getId() {
        return id;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void shiftX(final double shiftFactor) {
        x += shiftFactor;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    public void setActionSet(final ActionSet actionSet) {
        this.actionSet = actionSet;
    }
}
