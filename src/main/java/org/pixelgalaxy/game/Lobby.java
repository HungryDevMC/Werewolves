package org.pixelgalaxy.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.timers.LobbyTimer;
import org.pixelgalaxy.utils.ConfigSavers;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    public static final int LOBBY_COUNTDOWN = WerewolfMain.config.getInt("lobby_time");
    public static final int MIN_PLAYERS = WerewolfMain.config.getInt("min_player");
    public static final int MAX_PLAYERS = WerewolfMain.config.getInt("max_player");
    @Getter @Setter
    private static Location spawnLoc = ConfigSavers.getLocation("lobby_location");
    @Getter
    private static List<Player> currentPlayers = new ArrayList<>();

    private static void broadCurrentPlayers(){
        Bukkit.getServer().broadcastMessage(WerewolfMain.PREFIX + "Current players: §7" + getCurrentPlayerCount() + "§a/§7" + MAX_PLAYERS);
    }

    private static void startTimer(){

        LobbyTimer timer = new LobbyTimer(LOBBY_COUNTDOWN);
        timer.runTaskTimer(WerewolfMain.plugin, 0, 20);

    }

    public static void addCurrentPlayer(Player player) {
        if(!currentPlayers.contains(player)){
            currentPlayers.add(player);
            broadCurrentPlayers();
            if(getCurrentPlayerCount() == MIN_PLAYERS){
                startTimer();
            }
        }
    }

    public static void removeCurrentPlayer(Player player){
        if(currentPlayers.contains(player)){
            currentPlayers.remove(player);
            broadCurrentPlayers();
        }
    }

    public static int getCurrentPlayerCount(){
        return currentPlayers.size();
    }

}
