package src.main.shooter.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.Entity;

public class GamePanel extends JPanel {
    private final double[][] gameViewRanges = new double[][] { { -100, 100 }, { -100, 100 } }; // {xRange, yRange}
    private ClientGame game;

    public GamePanel(ClientGame game) {
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
    public void paint(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setColor(Color.BLACK);
        for (Entity entity : game.getEntities().values()) {
            graphics2d.fillRect(remapXCoords(entity.getX()), remapYCoords(entity.getY()),
                    rescaleWidth(entity.getWidth()), rescaleHeight(entity.getHeight()));
        }
    }

    private int remapXCoords(double gameX) {
        return remap(gameX, -100, 100, getWidth(), 0);
    }

    private int remapYCoords(double gameY) {
        return remap(gameY, -100, 100, getHeight(), 0);
    }

    private int remap(double initialPoint, double initialBottom, double initialTop, double newBottom, double newTop) {

        // ratio of [length between point and bottom]:[total initial length]
        double ratio = (initialPoint - initialBottom) / (initialTop - initialBottom);

        // distance between the new point and new bottom
        double newDist = (newTop - newBottom) * ratio;

        double newPoint = newBottom + newDist;
        return (int) Math.round(newPoint); // round because if cast to int across 0, it is not good
    }

    private int rescaleWidth(double gameWidth) {
        return rescale(gameWidth, gameViewRanges[0][1] - gameViewRanges[0][0], getWidth());
    }

    private int rescaleHeight(double gameHeight) {
        return rescale(gameHeight, gameViewRanges[1][1] - gameViewRanges[1][0], getHeight());
    }

    private int rescale(double initialLength, double initialRange, double newRange) {

        // ratio of [initial length]:[initial range]
        double ratio = initialLength / initialRange;

        double newLength = newRange * ratio;
        return (int) Math.round(newLength);
    }
}