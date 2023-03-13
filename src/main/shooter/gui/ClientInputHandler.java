package src.main.shooter.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.action.Action;
import src.main.shooter.game.action.WalkAction;
import src.main.shooter.utils.ArraySet;

public class ClientInputHandler implements KeyListener {
    private final ClientGame game;

    public ClientInputHandler(ClientGame game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ArraySet<Action> longActions = game.getActionSet().getLongActions();

        switch (e.getKeyChar()) {
            case 'w': {
                break;
            }

            case 'a': {
                // System.out.println("a pressed");
                longActions.add(new WalkAction(-1));
                break;
            }

            case 's': {

                break;
            }

            case 'd': {
                longActions.add(new WalkAction(1));
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ArraySet<Action> longActions = game.getActionSet().getLongActions();

        switch (e.getKeyChar()) {
            case 'w': {
                break;
            }

            case 'a': {
                longActions.removeIf((Action action) -> action instanceof WalkAction);
                break;
            }

            case 's': {

                break;
            }

            case 'd': {
                longActions.removeIf((Action action) -> action instanceof WalkAction);
                break;
            }

            default:
                break;
        }
    }
}
