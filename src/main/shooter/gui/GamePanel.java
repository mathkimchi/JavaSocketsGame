package src.main.shooter.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.ServerGame.Entity;
import src.main.shooter.game.entities.BulletEntity;
import src.main.shooter.game.entities.PistolEntity;
import src.main.shooter.game.entities.PlatformEntity;
import src.main.shooter.game.entities.PlayerEntity;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = -355339008103996038L;
    private static final Logger logger = Logger.getLogger(GamePanel.class.getName());

    private final double[][] gameViewRanges = new double[][] { { 0, 10 }, { 0, 10 } }; // {xRange, yRange}
    private final ClientGame game;

    private BufferedImage rightPlayerSprite, leftPlayerSprite, rightPistolSprite, leftPistolSprite, rightBulletSprite,
            leftBulletSprite;

    public GamePanel(final ClientGame game) {
        this.game = game;
        initSprites();
    }

    private BufferedImage getReflectedImage(final BufferedImage originalImage) {
        // flip right player sprite to get left player sprite
        final AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-originalImage.getWidth(), 0);
        final AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(originalImage, null);
    }

    private void initSprites() {
        try {
            rightPlayerSprite = ImageIO.read(new File("src/res/Right-Facing-Shooter.png"));
            leftPlayerSprite = getReflectedImage(rightPlayerSprite);
            rightPistolSprite = ImageIO.read(new File("src/res/Right-Facing-Pistol.png"));
            leftPistolSprite = getReflectedImage(rightPistolSprite);
            rightBulletSprite = ImageIO.read(new File("src/res/Right-Facing-Bullet.png"));
            leftBulletSprite = getReflectedImage(rightBulletSprite);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720, 720);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public void paint(final Graphics g) {
        // System.out.println("Starting to paint.");
        final Graphics2D graphics2d = (Graphics2D) g;

        drawDebugGrid(graphics2d);

        for (final Entity entity : game.getEntities().values()) {
            // System.out.println("Entity " + entity.getId() +
            // ": [x=" + entity.getX() + ", y=" + entity.getY() + ", w="
            // + entity.getWidth() + ", h=" + entity.getHeight() + "]");

            // because swing doesn't work with negative sizes
            final int x1 = remapXCoords(entity.getX()), y1 = remapYCoords(entity.getY()),
                    x2 = x1 + rescaleWidth(entity.getWidth()), y2 = y1 + rescaleHeight(entity.getHeight());

            final int minX = Math.min(x1, x2), minY = Math.min(y1, y2), maxX = Math.max(x1, x2),
                    maxY = Math.max(y1, y2);
            final int width = maxX - minX, height = maxY - minY;

            BufferedImage sprite;

            if (entity instanceof final PlayerEntity playerEntity) {
                sprite = switch (playerEntity.getHorDirection()) {
                    case LEFT -> leftPlayerSprite;
                    case RIGHT -> rightPlayerSprite;
                    default -> {
                        logger.severe("Unstandard hor direction.");
                        yield null;
                    }
                };
            } else if (entity instanceof final PistolEntity pistolEntity) {
                sprite = switch (pistolEntity.getHorDirection()) {
                    case LEFT -> leftPistolSprite;
                    case RIGHT -> rightPistolSprite;
                    default -> {
                        logger.severe("Unstandard hor direction.");
                        yield null;
                    }
                };
            } else if (entity instanceof final BulletEntity bulletEntity) {
                sprite = switch (bulletEntity.getHorDirection()) {
                    case LEFT -> leftBulletSprite;
                    case RIGHT -> rightBulletSprite;
                    default -> {
                        logger.severe("Unstandard hor direction.");
                        yield null;
                    }
                };
            } else if (entity instanceof final PlatformEntity platformEntity) {
                // TODO: create actual sprite
                sprite = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                final Graphics2D spriteGraphics2d = sprite.createGraphics();
                spriteGraphics2d.setColor(Color.BLUE);
                spriteGraphics2d.drawRect(0, 0, 1, 1);
                spriteGraphics2d.dispose();
            } else {
                // create a purple square
                sprite = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
                final Graphics2D spriteGraphics2d = sprite.createGraphics();
                spriteGraphics2d.setColor(Color.MAGENTA);
                spriteGraphics2d.drawRect(0, 0, 1, 1);
                spriteGraphics2d.dispose();

                logger.warning("Entity of unknown type");
            }
            graphics2d.drawImage(sprite, minX, minY, width, height, null);
        }
    }

    private void drawDebugGrid(final Graphics2D graphics2d) {
        graphics2d.setColor(Color.BLACK);

        // vert
        for (int gameX = (int) gameViewRanges[0][0] - 1; gameX < gameViewRanges[0][1] + 1; gameX++) {
            graphics2d.drawLine(remapXCoords(gameX), 0, remapXCoords(gameX), getHeight());
        }

        // hor
        for (int gameY = (int) gameViewRanges[1][0] - 1; gameY < gameViewRanges[1][1] + 1; gameY++) {
            graphics2d.drawLine(0, remapYCoords(gameY), getWidth(), remapYCoords(gameY));
        }
    }

    private int remapXCoords(final double gameX) {
        return remap(gameX, gameViewRanges[0][0], gameViewRanges[0][1], 0, getWidth());
    }

    private int remapYCoords(final double gameY) {
        return remap(gameY, gameViewRanges[1][0], gameViewRanges[1][1], getHeight(), 0);
    }

    private int remap(final double initialPoint, final double initialBottom, final double initialTop,
            final double newBottom, final double newTop) {

        // ratio of [length between point and bottom]:[total initial length]
        final double ratio = (initialPoint - initialBottom) / (initialTop - initialBottom);

        // distance between the new point and new bottom
        final double newDist = (newTop - newBottom) * ratio;

        final double newPoint = newBottom + newDist;
        return (int) Math.round(newPoint); // round because if cast to int across 0, it is not good
    }

    private int rescaleWidth(final double gameWidth) {
        return rescale(gameWidth, gameViewRanges[0][1] - gameViewRanges[0][0], getWidth());
    }

    private int rescaleHeight(final double gameHeight) {
        return rescale(gameHeight, gameViewRanges[1][1] - gameViewRanges[1][0], -getHeight());
    }

    private int rescale(final double initialLength, final double initialRange, final double newRange) {

        // ratio of [initial length]:[initial range]
        final double ratio = initialLength / initialRange;

        final double newLength = newRange * ratio;
        return (int) Math.round(newLength);
    }
}