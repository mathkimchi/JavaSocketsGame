package src.main.shooter.game;

import java.util.TreeMap;
import java.util.logging.Logger;

import src.main.shooter.game.action.ActionSet;
import src.main.shooter.game.entities.Entity;
import src.main.shooter.game.entities.PlatformEntity;
import src.main.shooter.game.entities.HorDirectionedEntity.HorDirection;
import src.main.shooter.game.entities.PlayerEntity;

public class ServerGame {
    private static final Logger logger = Logger.getLogger("Server");

    public static Logger getLogger() {
        return logger;
    }

    public class GameSettings {
        public static final double GLOBAL_GRAVITY = -0.05;

        public static final double WALK_SPEED = 0.0625;
        public static final double JUMP_VEL = 0.5;
    }

    private int smallestAvailableId = 0; // Use a UUID generator?

    /**
     * ! WARNING: DO NOT CALL THIS MORE THAN NEEDED, IT CHANGES THE ID NUMBER
     * 
     * <p>
     * 
     * It's not too big a deal, worst that could happen is that there is an unused
     * ID number. However, understand that this does assume that you will be using
     * the ID.
     *
     * @return the smallest available id
     */
    private int getSmallestAvailableId() {
        return smallestAvailableId++;
    }

    public ServerGame() {
        entities = new TreeMap<Integer, Entity>();

        init();
    }

    private void init() {
        addEntity(new PlatformEntity(getSmallestAvailableId(), 2, 1, 0, -3));
        addEntity(new PlatformEntity(getSmallestAvailableId(), 1, 5, -1.5, -3));
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

        // collisions
        for (final Entity entity1 : entities.values()) {
            for (final Entity entity2 : entities.values()) {
                if (entity1.isColliding(entity2)) {
                    entity1.handleCollision(entity2);
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
        final PlayerEntity player = new PlayerEntity(getSmallestAvailableId(), 0, -1, HorDirection.LEFT);
        addEntity(player);
        return player.getId();
    }
}
