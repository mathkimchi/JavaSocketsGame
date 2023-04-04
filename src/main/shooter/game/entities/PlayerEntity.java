package src.main.shooter.game.entities;

import src.main.shooter.game.ServerGame;
import src.main.shooter.game.ServerGame.Entity;
import src.main.shooter.game.ServerGame.GameSettings;
import src.main.shooter.game.action.Action;

public class PlayerEntity extends Entity implements HorDirectionedEntity, GravitationalEntity {
    private static final long serialVersionUID = -3022640676588904126L;

    private final PistolEntity pistol;

    private HorDirection horDirection;
    private double xVel, yVel;

    public PlayerEntity(final ServerGame game, final double x, final double y,
            final HorDirection direction) {
        super(game, 1, 2, x, y);

        this.horDirection = direction;

        xVel = 0;
        yVel = 0;

        this.pistol = new PistolEntity(this);
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
        for (final Action action : getActionSet().getInstantActions()) {
            switch (action) {
                case JUMP: {
                    if (getYVel() != 0) {
                        break;
                    }
                    shiftYVel(GameSettings.JUMP_VEL);
                    break;
                }

                case SHOOT: {
                    if (pistol.getHorDirection() == HorDirection.LEFT) {
                        new BulletEntity(getGame(), this, pistol.getLeftX(), pistol.getTopY(),
                                -GameSettings.BULLET_SPEED,
                                XAxisType.LEFT, YAxisType.TOP);
                    } else if (pistol.getHorDirection() == HorDirection.RIGHT) {
                        new BulletEntity(getGame(), this, pistol.getRightX(), pistol.getTopY(),
                                GameSettings.BULLET_SPEED,
                                XAxisType.RIGHT, YAxisType.TOP);
                    } else {
                        ServerGame.getLogger().warning("Unknown direction \"" + pistol.getHorDirection() + "\".");
                    }
                    break;
                }

                default:
                    ServerGame.getLogger().warning("Unknown action \"" + action + "\" in instant actions.");
                    break;
            }
        }
        getActionSet().getInstantActions().clear();

        for (final Action action : getActionSet().getLongActions()) {
            switch (action) {
                case LEFT_WALK: {
                    setHorDirection(HorDirection.LEFT);
                    shiftX(-GameSettings.WALK_SPEED);
                    break;
                }

                case RIGHT_WALK: {
                    setHorDirection(HorDirection.RIGHT);
                    shiftX(GameSettings.WALK_SPEED);
                    break;
                }

                default:
                    ServerGame.getLogger().warning("Unknown action \"" + action + "\" in long actions.");
                    break;
            }
        }

        // physics
        applyGravity();
        applyVelocity();
    }

    @Override
    public void handleCollision(final Entity otherEntity) {
        if (otherEntity instanceof PlatformEntity) {
            final Vector2D collisionNormal = getCollisionNormal(otherEntity);

            if (collisionNormal.getX() > 0) {
                // set right of this to the left of other
                this.setX(otherEntity.getX() - this.getWidth());
                this.setXVel(0);
            } else if (collisionNormal.getX() < 0) {
                // set left of this to the right of other
                this.setX(otherEntity.getX() + otherEntity.getWidth());
                this.setXVel(0);
            }

            if (collisionNormal.getY() > 0) {
                // set top of this to the bottom of other
                this.setY(otherEntity.getY() - this.getHeight());
                this.setYVel(0);
            } else if (collisionNormal.getY() < 0) {
                // set bottom of this to the top of other
                this.setY(otherEntity.getY() + otherEntity.getHeight());
                this.setYVel(0);
            }
        } else if (otherEntity instanceof BulletEntity) {
            die();
        }
    }

    private void die() { // might cause concurrent mod issues
        getGame().removeEntity(getId());
    }
}

// TODO: add tree like structure to entities so that players can officially own
// guns