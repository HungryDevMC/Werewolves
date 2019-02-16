package org.pixelgalaxy.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;
import java.util.Map;

/**
 * Different player modes
 */

public enum PlayerMode {

    LOBBY(),
    INGAME(),
    DEAD(),
    SPECTATOR();

    @Getter @Setter private static Map<Player, PlayerMode> playerModeMap = new HashMap<>();

    /**
     * Set the mode of the player, which will set the gamemode, flight etc..
     * @param p Player to set mode
     * @param playerMode playermode to set
     */

    public static void set(Player p, PlayerMode playerMode){

        getPlayerModeMap().put(p, playerMode);

        switch(playerMode){

            case LOBBY:

                p.teleport(Lobby.getSpawnLoc());
                p.getInventory().clear();
                p.setHealth(p.getMaxHealth());
                p.setGameMode(GameMode.ADVENTURE);
                p.setAllowFlight(true);
                p.setFlying(true);
                p.setPlayerListName(p.getName());
                p.setCustomName(p.getName());

                break;

            case INGAME:

                p.setHealth(p.getMaxHealth());
                p.setGameMode(GameMode.ADVENTURE);
                p.setAllowFlight(false);
                p.setFlying(false);

                break;

            case DEAD:

                p.getInventory().clear();
                p.setHealth(0);

            case SPECTATOR:

                p.setGameMode(GameMode.SPECTATOR);
                p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                p.setAllowFlight(true);

                break;

        }

    }

}
