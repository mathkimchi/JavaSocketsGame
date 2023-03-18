package src.main.shooter.game;

import java.util.TreeMap;

import src.main.shooter.game.action.ActionSet;
import src.main.shooter.game.entities.Entity;
import src.main.shooter.game.entities.PlayerEntity;

public class ClientGame {
    private final ActionSet actionSet;

    private final int playerId;

    private TreeMap<Integer, Entity> entities;

    public ClientGame(final int clientId) {
        playerId = clientId;
        actionSet = new ActionSet();
    }

    public ActionSet getActionSet() {
        return actionSet;
    }

    @Deprecated
    public PlayerEntity getPlayerEntity() {
        return (PlayerEntity) entities.get(playerId);
    }

    public void addEntity(final Entity entity) {
        entities.put(entity.getId(), entity);
    }

    public TreeMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void processEntityList(final TreeMap<Integer, Entity> incomingEntityList) {
        entities = incomingEntityList;
    }

    public void tick() {
        // TODO: game logic
    }
}
