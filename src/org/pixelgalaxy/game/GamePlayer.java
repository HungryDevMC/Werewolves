package org.pixelgalaxy.game;

public class GamePlayer {

    private Team playerTeam;
    private Role playerRole;

    public GamePlayer(Team playerTeam, Role playerRole){

        setPlayerTeam(playerTeam);
        setPlayerRole(playerRole);

    }

    public void setPlayerRole(Role playerRole) {
        this.playerRole = playerRole;
    }

    public void setPlayerTeam(Team playerTeam) {
        this.playerTeam = playerTeam;
    }

    public Role getPlayerRole() {
        return playerRole;
    }

    public Team getPlayerTeam() {
        return playerTeam;
    }
}
