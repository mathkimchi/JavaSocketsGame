package src.main.shooter.game.entities;

import src.main.shooter.game.ServerGame.GameSettings;
import src.main.shooter.game.action.Action;

public class PlayerEntity extends Entity implements HorDirectionedEntity {
    private static final long serialVersionUID = -3022640676588904126L;

    private HorDirection horDirection;

    public PlayerEntity(final int id, final double x, final double y,
            final HorDirection direction) {
        super(id, 1, 2, x, y);

        this.horDirection = direction;
    }

    @Override
    public HorDirection getHorDirection() {
        return horDirection;
    }

    @Override
    public void setHorDirection(final HorDirection horDirection) {
        this.horDirection = horDirection;
    }

    @Override
    public void tick() {
        // for (Action action : entity.getActionSet().getInstantActions()) {
        // // TODO
        // }
        getActionSet().getInstantActions().clear();

        for (final Action action : getActionSet().getLongActions()) {
            switch (action) {
                case LEFT_WALK:
                    setHorDirection(HorDirection.LEFT);
                    shiftX(-GameSettings.WALK_SPEED);
                    break;

                case RIGHT_WALK:
                    setHorDirection(HorDirection.RIGHT);
                    shiftX(GameSettings.WALK_SPEED);
                    break;

                default:
                    break;
            }
        }

        // gravity

        // collision

    }
}
