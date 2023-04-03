package src.main.shooter.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import src.main.shooter.game.ServerGame;
import src.main.shooter.net.packets.ActionPacket;
import src.main.shooter.net.packets.DisconnectPacket;
import src.main.shooter.net.packets.ClientPacket;

/**
 * Can either make server tell every single client every single entity's
 * position every frame, or can make server tell every client the new position
 * for an entity every time it changes position.
 * 
 * The second is probably better in ways such as efficiency, but I will do the
 * first for now. Also it kind of makes more sense with UDP.
 * 
 * 
 * 
 * As for the chain of communication, this is how a key press would work: GUI
 * tells client that key was pressed. Client tells server that key was pressed.
 * On the server side, the server tells it's game that the key was pressed.
 * 
 * Every game tick, the game does game logic. The server tells client what
 * changed. Client updates gui based off of the change.
 * 
 * 
 * 
 * Also, the roles for server and game seem very similar. For example, it makes
 * sense to put the gameloop in the game class, but I put it in the server
 * class. This is because the game should only contain the game logic. In a real
 * time multiplayer game, it does make sense to give the game class
 * responsibility for the gameloop. Both make sense, but I decided that the game
 * should be like it's own world and so the role that real time has on the game
 * should be kept outside of the game class. For example, take tictactoe. The
 * game class would contain the board and everything inside the boards. To it,
 * it doesn't matter how many seconds have passed (unless it is a timed game).
 * It only cares what spots are x or o or blank. It doesn't matter if the Red
 * player is an ai or that there has been 3 games before the current game.
 * Obviousily, if the game was a timed game, then real time would matter. But, I
 * don't want this game to care about real time. For now, time and the tick
 * count will be in the server class. However, if there is a feature such as the
 * game getting harder as the tick increases, then the tick count might be moved
 * to the game class.
 * 
 * 
 * 
 * Speaking of real time, we must also consider that the client's ticks will be
 * offset from the server's. This is made worse with multiple clients. I can
 * either make actions discrete or continuous. It will be best to make
 * everything discrete, but since I am currently using UDP, that isn't really a
 * good idea. To explain better, take the example of walking. If I made this
 * discrete, I would say walking is just repeatedly teleporting a certain
 * meters every tick. In terms of continuity, I could say that walking is
 * smoothly traveling at a rate of a certain meters per tick. I can also ignore
 * actions and say there is only state. By state, I don't mean states like
 * walking but position. Regardless of walking or teleporting or standing, the
 * client could always tell the server the player's position. This would not be
 * good for animations, but it makes the most sense with UDP. Another way is
 * saying that the player is doing the action of walking and is at a certain
 * position. A lot of these methods seem to make sense with walking, but what
 * about instantanious actions like throwing an egg? Sense this only happens for
 * one frame, we can't risk missing this information. With walking, it is
 * forgivable to miss a walking action since in the next frame, we can just get
 * the new position. However, with instant actions, we should use TCP and we
 * can't just do the same thing like telling the player's position because if we
 * do that with inventory, we'd say there was an egg and then the egg wasn't in
 * the player's inventory. The game is able to deduce that the egg was thrown,
 * but how would it know the angle of the throw and the type of throw? It is not
 * ideal to make the client give information about the egg since the client
 * should only give information about the player. So, we can make it send
 * information about the throw. It is tempting to use UDP for speed, but I
 * conclude that TCP is the better choice. This is also better for chatting.
 * With TCP, I can make the client tell the server about every button press, and
 * the server takes care of every input, but that seems unnecessary. I will
 * assume that all actions are instant and discrete. Walking is instantly
 * teleportating every tick. All actions are committed at a whole number time
 * like t = 10 but not t = 10.5, but there can still be order with actions
 * committed at the same tick. For example, even if switching items and using
 * selected item were committed at t=10, one must be done before the other.
 */
public class Server implements Runnable {
    private final int TICKS_PER_SECOND = 20;
    private final int MILLISECONDS_PER_TICK = 1000000000 / TICKS_PER_SECOND;

    public final static int DEFAULT_PORT_NUMBER = 1234;

    private final ServerGame game;
    private ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clientHandlers;

    public Server(final ServerGame game, final int port) {
        this.game = game;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        clientHandlers = new ArrayList<ClientHandler>();
    }

    @Override
    public void run() {
        new Thread(() -> startAcceptClientsLoop()).start();
        new Thread(() -> startGameloop()).start();
    }

    private void startAcceptClientsLoop() {
        System.out.println("Accepting Clients.");
        while (true) {
            System.out.println("Waiting for new client.");
            try {
                final Socket socket = serverSocket.accept();
                System.out.println("A new client has connected.");
                final ClientHandler clientHandler = new ClientHandler(this, socket, game.spawnPlayerEntity());
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGameloop() {
        long lastTickTime = System.nanoTime();

        while (true) {
            final long whenShouldNextTickRun = lastTickTime + MILLISECONDS_PER_TICK;
            if (System.nanoTime() < whenShouldNextTickRun) {
                continue;
            }

            game.tick();

            sendUpdatesToAll();

            lastTickTime = System.nanoTime();
        }
    }

    public void processPacket(final ClientHandler clientHandler, final ClientPacket packet) {
        if (packet instanceof final ActionPacket actionPacket) {
            game.updateActionSet(clientHandler.getEntityId(), actionPacket.actionSet);
        } else if (packet instanceof final DisconnectPacket disconnectPacket) {
            clientHandler.disconnect();
            game.removeEntity(clientHandler.getEntityId());
            clientHandlers.remove(clientHandler);
        }
    }

    // server to all client
    private void sendUpdatesToAll() {
        for (final ClientHandler clientHandler : clientHandlers) {
            sendUpdates(clientHandler);
        }
    }

    // server to one client
    public void sendUpdates(final ClientHandler clientHandler) {
        clientHandler.sendUpdate(game.getEntities());
    }

    public void closeServer() {
        // TODO: save state or something

        try {
            serverSocket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void main(final String[] args) {
        new Server(new ServerGame(), Server.DEFAULT_PORT_NUMBER).run();
    }
}
