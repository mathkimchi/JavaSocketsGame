package src.main.shooter.game.entities;

import src.main.shooter.game.ServerGame.Entity;
import src.main.shooter.game.entities.HorDirectionedEntity.HorDirection;

public class PistolEntity extends Entity {
    private static final long serialVersionUID = 348408736704866955L;

    private final PlayerEntity owner;

    public PistolEntity(final PlayerEntity owner) {
        // the 3 and 2 are dimensions of the pistol. the division by 8 is because the
        // player is 16x8 pixels and considered 2x1 in the game
        super(owner.getGame(), 3. / 8., 2. / 8., Double.NaN, Double.NaN);

        this.owner = owner;
    }

    @Override
    public void tick() {
    }

    @Override
    public void handleCollision(final Entity otherEntity) {
    }

    @Override
    public double getX() {
        return getCenterX() - getWidth() / 2;
    }

    @Override
    public double getY() {
        return owner.getBottomY() + 1.25;
    }

    @Override
    public double getCenterX() {
        return owner.getCenterX() + owner.getHorDirection().getSign() * getXOffset();
    }

    private double getXOffset() {
        return owner.getWidth() / 2. + this.getWidth() / 2. + 1. / 8.;
    }

    public HorDirection getHorDirection() {
        return owner.getHorDirection();
    }
}
