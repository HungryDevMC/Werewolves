package org.pixelgalaxy.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.game.Lobby;

public class LobbyJoinLeave implements Listener {

    /**
     * If the game has not started, set player mode and add them to gameQueue
     * @param e Player join
     */

    @EventHandler
    public void onLobbyJoin(PlayerJoinEvent e){

        if(!Game.isStarted()) {
            Player p = e.getPlayer();
            p.teleport(Lobby.getSpawnLoc());
            p.getInventory().clear();
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            Lobby.addCurrentPlayer(p);
        }

    }

    /**
     * If the game has not started, remove player from gameQueue
     * @param e
     */

    @EventHandler
    public void onLobbyLeave(PlayerQuitEvent e){

        if(!Game.isStarted()) {
            Player p = e.getPlayer();
            Lobby.removeCurrentPlayer(p);
        }
    }

}
