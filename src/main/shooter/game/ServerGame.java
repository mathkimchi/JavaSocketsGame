package src.main.shooter.game;

import java.util.TreeMap;

import src.main.shooter.game.action.Action;
import src.main.shooter.game.action.ActionSet;
import src.main.shooter.game.action.WalkAction;

public class ServerGame {
    private int smallestAvailableId = 0;

    public int getSmallestAvailableId() {
        return smallestAvailableId++;
    }

    public ServerGame() {
        entities = new TreeMap<Integer, Entity>();
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }

    private TreeMap<Integer, Entity> entities;

    public TreeMap<Integer, Entity> getEntities() {
        return entities;
    }

    public void updateActionSet(int i, ActionSet actionSet) {
        if (actionSet.getLongActions().size() > 0) {
            System.out.println("new actionset's length: " + actionSet.getLongActions().size());
        }
        entities.get(i).setActionSet(actionSet);
    }

    public void tick() {
        for (Entity entity : entities.values()) {
            // for (Action action : entity.getActionSet().getInstantActions()) {
            // // TODO
            // }

            entity.getActionSet().getInstantActions().clear();

            for (Action action : entity.getActionSet().getLongActions()) {
                if (action instanceof WalkAction) {
                    entity.setX(entity.getX() + ((WalkAction) action).getHorizontalSpeed());
                }
            }

            if (entity.getActionSet().getLongActions().size() > 0) {
                System.out.println("Long Actions: " + entity.getActionSet().getLongActions().size());
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
        Entity player = new Entity(getSmallestAvailableId(), 0, 0, 100, 100);
        addEntity(player);
        return player.getId();
    }
}
