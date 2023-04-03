package src.main.shooter.gui.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.action.Action;
import src.main.shooter.utils.ArraySet;

public class ClientInputHandler implements KeyListener {
    /**
     * In mac and other OS, when a key is held for ~5 seconds, the OS counts it as
     * being rapidly pressed. This is to ensure that press only refers to when
     */
    private final boolean[] previouslyPressed;
    private final ClientGame game;

    public ClientInputHandler(final ClientGame game) {
        this.game = game;
        previouslyPressed = new boolean[0xFFFF];
        for (int i = 0; i < previouslyPressed.length; i++) {
            previouslyPressed[i] = false;
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        final int keyCode = e.getKeyCode();

        if (previouslyPressed[keyCode]) {
            // means that this is due to rapid press in the OS
            return;
        }

        final ArraySet<Action> longActions = game.getActionSet().getLongActions();
        final ArrayList<Action> instantActions = game.getActionSet().getInstantActions();
        switch (keyCode) {
            case KeyEvent.VK_A: {
                longActions.add(Action.LEFT_WALK);
                break;
            }

            case KeyEvent.VK_D: {
                longActions.add(Action.RIGHT_WALK);
                break;
            }

            case KeyEvent.VK_SPACE: {
                instantActions.add(Action.JUMP);
                break;
            }

            case KeyEvent.VK_ENTER: {
                instantActions.add(Action.SHOOT);
                break;
            }

            default:
                break;
        }
        previouslyPressed[keyCode] = true;
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        final int keyCode = e.getKeyCode();

        final ArraySet<Action> longActions = game.getActionSet().getLongActions();

        switch (keyCode) {
            case KeyEvent.VK_A: {
                longActions.remove(Action.LEFT_WALK);
                break;
            }

            case KeyEvent.VK_D: {
                longActions.remove(Action.RIGHT_WALK);
                break;
            }

            default:
                break;
        }
        previouslyPressed[keyCode] = false;
    }
}
