package src.main.shooter.game.entities;

import src.main.shooter.game.ServerGame.GameSettings;
import src.main.shooter.game.action.Action;

public class PlayerEntity extends Entity implements HorDirectionedEntity, GravitationalEntity {
    private static final long serialVersionUID = -3022640676588904126L;

    private HorDirection horDirection;

    private double xVel, yVel;

    public PlayerEntity(final int id, final double x, final double y,
            final HorDirection direction) {
        super(id, 1, 2, x, y);

        this.horDirection = direction;

        xVel = 0;
        yVel = 0;
    }

    @Override
    public double getYVel() {
        return yVel;
    }

    @Override
    public void setYVel(final double yVel) {
        this.yVel = yVel;
    }

    @Override
    public double getXVel() {
        return xVel;
    }

    @Override
    public void setXVel(final double xVel) {
        this.xVel = xVel;
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

        // physics
        applyGravity();
        applyVelocity();
        System.out.println(getY());

        // collision
    }
}
