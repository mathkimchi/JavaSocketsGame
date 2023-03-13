package src.main.shooter.net;

import java.io.Serial;
import java.io.Serializable;

import src.main.shooter.game.Entity;
import src.main.shooter.game.action.ActionSet;

/**
 * Stores only the actions--instant and long, of an entity.
 * (I feel like my grammar is incorrect.)
 */
public class Packet implements Serializable {
    @Serial
    public final ActionSet actionSet;

    public Packet(Entity entity) {
        actionSet = entity.getActionSet();
    }

    public Packet(ActionSet actionSet) {
        this.actionSet = actionSet;
    }
}
