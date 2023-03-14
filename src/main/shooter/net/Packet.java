package src.main.shooter.net;

import java.io.Serializable;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.Entity;
import src.main.shooter.game.action.ActionSet;

/**
 * Stores only the actions--instant and long, of an entity.
 * (I feel like my grammar is incorrect.)
 */
public class Packet implements Serializable {
    private static final long serialVersionUID = -8731243900388342502L;

    public final ActionSet actionSet;

    @Deprecated
    public Packet(final Entity entity) {
        actionSet = entity.getActionSet();
    }

    public Packet(final ClientGame game) {
        this.actionSet = game.getActionSet();
        // this.actionSet = new ActionSet();
        // this.actionSet.getLongActions().add(null);
    }

    @Override
    public String toString() {
        return "Packet [actionSet=" + actionSet + "]";
    }
}
