package src.main.shooter.net.packets;

import java.io.Serializable;

/**
 * Used when the client wants to send a packet to the server.
 * 
 * Stores only the actions--instant and long, of an entity.
 * (I feel like my grammar is incorrect.)
 */
public class ClientPacket implements Serializable {
    private static final long serialVersionUID = -8731243900388342502L;
}
