package src.main.shooter.net.packets;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.ServerGame.Entity;
import src.main.shooter.game.action.ActionSet;

public class ActionPacket extends ClientPacket {
    private static final long serialVersionUID = -710902470934092114L;
    public final ActionSet actionSet;

    @Deprecated
    public ActionPacket(final Entity entity) {
        actionSet = entity.getActionSet();
    }

    public ActionPacket(final ClientGame game) {
        this.actionSet = game.getActionSet();
        // this.actionSet = new ActionSet();
        // this.actionSet.getLongActions().add(null);
    }

    @Override
    public String toString() {
        return "Packet [actionSet=" + actionSet + "]";
    }
}
