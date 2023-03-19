package src.main.shooter.game.entities;

import java.io.Serializable;

import src.main.shooter.game.action.ActionSet;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = -1816334362202070857L;

    private final int id;
    private final double width, height;
    private double x, y; // bottom left corner, not center

    private ActionSet actionSet;

    public Entity(final int id, final double width, final double height, final double x, final double y) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        actionSet = new ActionSet();
    }

    public ActionSet getActionSet() {
        return actionSet;
    }

    public final int getId() {
        return id;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double shiftX(final double shiftFactor) {
        return x += shiftFactor;
    }

    public double shiftY(final double shiftFactor) {
        return y += shiftFactor;
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

    public void setActionSet(final ActionSet actionSet) {
        this.actionSet = actionSet;
    }

    /**
     * I'd rather have the game just calculate everything, because of things like
     * gravity and collisions. However, this is the standard way apparently.
     */
    public abstract void tick();
}
