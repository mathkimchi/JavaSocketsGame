package src.main.shooter.game;

import java.util.TreeMap;

import src.main.shooter.game.action.ActionSet;
import src.main.shooter.game.entities.Entity;
import src.main.shooter.game.entities.HorDirectionedEntity.HorDirection;
import src.main.shooter.game.entities.PlayerEntity;

public class ServerGame {
    public class GameSettings {
        public static final double WALK_SPEED = 0.0625;
    }

    private int smallestAvailableId = 0;

    public int getSmallestAvailableId() {
        return smallestAvailableId++;
    }

    public ServerGame() {
        entities = new TreeMap<Integer, Entity>();
    }

    public void addEntity(final Entity entity) {
        entities.put(entity.getId(), entity);
    }

    private final TreeMap<Integer, Entity> entities;

    public TreeMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void updateActionSet(final int id, final ActionSet actionSet) {
        entities.get(id).setActionSet(actionSet);
    }

    public void removeEntity(final int id) {
        // TODO: would have some saving in here
        entities.remove(id);
    }

    public void tick() {
        for (final Entity entity : entities.values()) {
            entity.tick();
        }
    }

    /**
     * Creates a new player entity, adds the entity to the game, and returns the ID
     * of the new entity.
     * 
     * @return New entity's id.
     */
    public int spawnPlayerEntity() {
        final PlayerEntity player = new PlayerEntity(getSmallestAvailableId(), 0, 0, HorDirection.LEFT);
        addEntity(player);
        return player.getId();
    }
}
