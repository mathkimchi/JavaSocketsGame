package src.main.shooter.game.entities;

public class PlatformEntity extends Entity {
    private static final long serialVersionUID = 423302831329368937L;

    public PlatformEntity(final int id, final double width, final double height, final double x, final double y) {
        super(id, width, height, x, y);
    }

    @Override
    public void tick() {
    }

    @Override
    public void handleCollision(final Entity otherEntity) {
    }
}
