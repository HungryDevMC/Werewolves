package org.pixelgalaxy.timers;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.game.Lobby;

@AllArgsConstructor
public class LobbyTimer extends BukkitRunnable {

    private int lobbyCountdown;

    @Override
    public void run(){

        if(Lobby.getCurrentPlayerCount() >= Lobby.MIN_PLAYERS) {

            if (lobbyCountdown > 0) {
                if (lobbyCountdown % 10 == 0 || lobbyCountdown <= 5) {
                    Bukkit.getServer().broadcastMessage(WerewolfMain.PREFIX + "The game is starting in: §7" + lobbyCountdown + " §aseconds");
                }
                lobbyCountdown--;

            } else {
                Game.start();
                this.cancel();
            }
        }else{

            Bukkit.getServer().broadcastMessage(WerewolfMain.PREFIX + "§cThe timer has been stopped because there is atleast §7" + Lobby.MIN_PLAYERS + " §cplayers needed in order to start the game!");
            this.cancel();

        }
    }

}
