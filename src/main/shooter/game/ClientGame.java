package src.main.shooter.game;

import java.util.TreeMap;

import src.main.shooter.game.ServerGame.Entity;
import src.main.shooter.game.action.ActionSet;
import src.main.shooter.game.entities.PlayerEntity;
import src.main.shooter.net.Client;

public class ClientGame {
    private final int playerId;
    private final Client client;

    private final ActionSet actionSet;

    private TreeMap<Integer, Entity> entities;

    public ClientGame(final Client client, final int clientId) {
        this.client = client;
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
        if (!getEntities().containsKey(playerId)) {
            // TODO: find way to end all threads without exit()
            // make a method in client like client.handleDeath() or maybe simplify
            // client.disconnect()
            client.disconnect();
        }
    }
}
