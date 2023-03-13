package src.main.shooter.game;

import java.io.Serializable;

import src.main.shooter.game.action.ActionSet;

public class Entity implements Serializable {
    private double x, y;
    private final int id;
    private final double width, height;

    private ActionSet actionSet;

    public Entity(int id, double x, double y, double width, double height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        actionSet = new ActionSet();
    }

    public ActionSet getActionSet() {
        return actionSet;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setActionSet(ActionSet actionSet) {
        this.actionSet = actionSet;
    }
}
