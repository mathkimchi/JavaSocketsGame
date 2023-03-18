package src.main.shooter.game;

import java.util.TreeMap;

import src.main.shooter.game.Entity.Direction;
import src.main.shooter.game.action.Action;
import src.main.shooter.game.action.ActionSet;

public class ServerGame {
    public class GameSettings {
        public static final double WALK_SPEED = 0.01;
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
            // for (Action action : entity.getActionSet().getInstantActions()) {
            // // TODO
            // }

            entity.getActionSet().getInstantActions().clear();

            for (final Action action : entity.getActionSet().getLongActions()) {
                switch (action) {
                    case LEFT_WALK:
                        entity.setDirection(Direction.LEFT);
                        entity.shiftX(-GameSettings.WALK_SPEED);
                        break;

                    case RIGHT_WALK:
                        entity.setDirection(Direction.RIGHT);
                        entity.shiftX(GameSettings.WALK_SPEED);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Creates a new player entity, adds the entity to the game, and returns the ID
     * of the new entity.
     * 
     * @return New entity's id.
     */
    public int spawnPlayerEntity() {
        final Entity player = new Entity(getSmallestAvailableId(), 1, 2, 0, 0, Entity.Direction.LEFT);
        addEntity(player);
        return player.getId();
    }
}
