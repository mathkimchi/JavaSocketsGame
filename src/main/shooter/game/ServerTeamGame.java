package src.main.shooter.game;

import src.main.shooter.game.entities.TeamedPlayerEntity;
import src.main.shooter.game.entities.HorDirectionedEntity.HorDirection;
import src.main.shooter.game.entities.TeamedPlayerEntity.Team;
import src.main.shooter.utils.ArraySet;

public class ServerTeamGame extends ServerGame {
    private final ArraySet<TeamedPlayerEntity> redTeamPlayers = new ArraySet<>(), blueTeamPlayers = new ArraySet<>();

    public ServerTeamGame() {
        super();

        System.out.println("Playing teams.");
    }

    @Override
    public int spawnPlayerEntity() {
        TeamedPlayerEntity playerEntity;
        if (redTeamPlayers.size() < blueTeamPlayers.size()) {
            playerEntity = new TeamedPlayerEntity(this, Team.RED, 1, 3,
                    HorDirection.RIGHT);
            redTeamPlayers.add(playerEntity);
            System.out.println("New red team player.");
        } else {
            playerEntity = new TeamedPlayerEntity(this, Team.BLUE, 8, 3,
                    HorDirection.LEFT);
            blueTeamPlayers.add(playerEntity);
            System.out.println("New blue team player.");
        }
        return playerEntity.getId();
    }

    @Override
    public void removeEntity(final int id) {
        if (getEntities().get(id) instanceof final TeamedPlayerEntity teamedPlayerEntity) {
            if (teamedPlayerEntity.getTeam() == Team.RED) {
                redTeamPlayers.remove(teamedPlayerEntity);
            } else {
                blueTeamPlayers.remove(teamedPlayerEntity);
            }
        }
        super.removeEntity(id);
    }
}
