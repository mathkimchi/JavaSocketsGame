package src.main.shooter.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.Entity;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = -355339008103996038L;
    private final double[][] gameViewRanges = new double[][] { { 2, -2 }, { 2, -2 } }; // {xRange, yRange}
    private final ClientGame game;

    public GamePanel(final ClientGame game) {
        this.game = game;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public void paint(final Graphics g) {
        // System.out.println("Starting to paint.");
        final Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setColor(Color.BLACK);
        for (final Entity entity : game.getEntities().values()) {
            // System.out.println("Entity " + entity.getId() + ": [x=" + entity.getX() + ",
            // y=" + entity.getY() + ", w="
            // + entity.getWidth() + ", h=" + entity.getHeight() + "]");

            // because swing doesn't work with negative sizes
            final int x1 = remapXCoords(entity.getX()), y1 = remapYCoords(entity.getY()),
                    x2 = x1 + rescaleWidth(entity.getWidth()), y2 = y1 + rescaleHeight(entity.getHeight());

            final int botX = Math.min(x1, x2), botY = Math.min(y1, y2), topX = Math.max(x1, x2),
                    topY = Math.max(y1, y2);
            final int width = topX - botX, height = topY - botY;
            graphics2d.fillRect(botX, botY, width, height);
        }
    }

    private int remapXCoords(final double gameX) {
        return remap(gameX, gameViewRanges[0][0], gameViewRanges[0][1], getWidth(), 0);
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
        return rescale(gameHeight, gameViewRanges[1][1] - gameViewRanges[1][0], getHeight());
    }

    private int rescale(final double initialLength, final double initialRange, final double newRange) {

        // ratio of [initial length]:[initial range]
        final double ratio = initialLength / initialRange;

        final double newLength = newRange * ratio;
        return (int) Math.round(newLength);
    }
}