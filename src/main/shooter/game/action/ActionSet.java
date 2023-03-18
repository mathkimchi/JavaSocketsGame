package src.main.shooter.game.action;

import java.io.Serializable;
import java.util.ArrayList;

import src.main.shooter.utils.ArraySet;

/**
 * More about actions are in the {@code server} class, but instant actions are
 * done instantly and long actions are done every tick until it is told not to
 * be done.
 * 
 * To clear actions or add or remove, do getInstantActions and directly
 * call functions on them such as add() or clear() or
 * 
 * @see src.main.shooter.net.Server
 * @see java.util.ArrayList
 */
public class ActionSet implements Serializable {
    private static final long serialVersionUID = -4852037557772448218L;

    private final ArrayList<Action> instantActions;
    private final ArraySet<Action> longActions;

    public ActionSet() {
        instantActions = new ArrayList<Action>();
        longActions = new ArraySet<Action>();
    }

    public ArrayList<Action> getInstantActions() {
        return instantActions;
    }

    public ArraySet<Action> getLongActions() {
        return longActions;
    }

    @Override
    public String toString() {
        return "ActionSet [instantActions=" + instantActions + ", longActions=" + longActions + "]";
    }
}
