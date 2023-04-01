package src.main.shooter.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;

import src.main.shooter.game.action.ActionSet;
import src.main.shooter.game.entities.HorDirectionedEntity.HorDirection;
import src.main.shooter.game.entities.PlatformEntity;
import src.main.shooter.game.entities.PlayerEntity;
import src.main.shooter.game.entities.Vector2D;

public class ServerGame {
    /**
     * Static so that the implicit reference to outer class isn't serialized.
     * 
     * Should I create a rectangle class?
     */
    public static abstract class Entity implements Serializable {
        private static final long serialVersionUID = -1816334362202070857L;

        private transient final ServerGame game;
        private final int id;

        private final double width, height;

        private double x, y; // bottom left corner, not center
        private ActionSet actionSet;

        public Entity(final ServerGame game, final double width, final double height, final double x,
                final double y) {
            this.game = game;
            this.id = this.game.getSmallestAvailableId();
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;

            actionSet = new ActionSet();

            game.addEntity(this);
        }

        public ServerGame getGame() {
            return game;
        }

        public ActionSet getActionSet() {
            return actionSet;
        }

        public final int getId() {
            return id;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }

        public double shiftX(final double shiftFactor) {
            return x += shiftFactor;
        }

        public double shiftY(final double shiftFactor) {
            return y += shiftFactor;
        }

        /**
         * @return the X coordinate value of the left bottom point of the entity.
         */
        public double getX() {
            return x;
        }

        /**
         * @return the Y coordinate value of the left bottom point of the entity.
         */
        public double getY() {
            return y;
        }

        public double getLeftX() {
            return getX();
        }

        public double getBottomY() {
            return getY();
        }

        public double getRightX() {
            return getLeftX() + getWidth();
        }

        public double getTopY() {
            return getBottomY() + getHeight();
        }

        public double getCenterX() {
            return getLeftX() + getWidth() / 2;
        }

        public double getCenterY() {
            return getBottomY() + getHeight() / 2;
        }

        public void setX(final double x) {
            this.x = x;
        }

        public void setY(final double y) {
            this.y = y;
        }

        public void setActionSet(final ActionSet actionSet) {
            this.actionSet = actionSet;
        }

        /**
         * I'd rather have the game just calculate everything, because of things like
         * gravity and collisions. However, this is the standard way apparently.
         */
        public abstract void tick();

        public abstract void handleCollision(Entity otherEntity);

        public boolean isColliding(final Entity otherEntity) {
            // Uses AABB collision
            return getX() < otherEntity.getX() + otherEntity.getWidth() && getX() + getWidth() > otherEntity.getX()
                    && getY() < otherEntity.getY() + otherEntity.getHeight()
                    && getY() + getHeight() > otherEntity.getY()
                    && this != otherEntity;
        }

        public Vector2D getCollisionNormal(final Entity otherEntity) {
            // Edge case: collision with a very thin object and jumping on the edge of
            // platform
            final double xOverlap = Math.min(this.getX() + this.getWidth(), otherEntity.getX() + otherEntity.getWidth())
                    - Math.max(this.getX(), otherEntity.getX());
            final double yOverlap = Math.min(this.getY() + this.getHeight(),
                    otherEntity.getY() + otherEntity.getHeight())
                    - Math.max(this.getY(), otherEntity.getY());

            if (xOverlap > yOverlap) { // smaller matters more
                return new Vector2D(0, Math.signum(otherEntity.getY() - this.getY()));
            } else if (xOverlap < yOverlap) {
                return new Vector2D(Math.signum(otherEntity.getX() - this.getX()), 0);
            } else {
                return new Vector2D(Math.signum(otherEntity.getX() - this.getY()),
                        Math.signum(otherEntity.getY() - this.getY()));
            }
        }
    }

    public class GameSettings {
        public static final double GLOBAL_GRAVITY = -0.05;

        public static final double WALK_SPEED = 0.125;
        public static final double JUMP_VEL = 0.5;

        public static final double BULLET_SPEED = 0.25;
        public static final int BULLET_LIFESPAN = 20;
    }

    private static final Logger logger = Logger.getLogger("Server");

    public static Logger getLogger() {
        return logger;
    }

    private int smallestAvailableId = 0; // Use a UUID generator?
    private final TreeMap<Integer, Entity> entities;

    public ServerGame() {
        entities = new TreeMap<Integer, Entity>();

        init();
    }

    public TreeMap<Integer, Entity> getEntities() {
        // recommended to iterate through a copy of this
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
        /**
         * to avoid concurrent modification exceptions
         */
        final TreeMap<Integer, Entity> entitiesCopy = new TreeMap<>(entities);
        for (final Entity entity : entitiesCopy.values()) {
            entity.tick();
        }

        // collisions
        for (final Entity entity1 : entitiesCopy.values()) {
            for (final Entity entity2 : entitiesCopy.values()) {
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
        final PlayerEntity player = new PlayerEntity(this, 4.5, 5, HorDirection.LEFT);
        return player.getId();
    }

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

    private void init() {
        // load world platforms
        try (Scanner platforms = new Scanner(new File("src/res/standard-map-platform-rectangles.csv"))) {
            while (platforms.hasNextLine()) {
                final String[] dimensionsString = platforms.nextLine().split(",");
                new PlatformEntity(this,
                        Double.parseDouble(dimensionsString[0]),
                        Double.parseDouble(dimensionsString[1]),
                        Double.parseDouble(dimensionsString[2]),
                        Double.parseDouble(dimensionsString[3]));
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addEntity(final Entity entity) {
        entities.put(entity.getId(), entity);
    }
}
