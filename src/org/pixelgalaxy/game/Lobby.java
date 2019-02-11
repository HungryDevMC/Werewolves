package org.pixelgalaxy.game;

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
    private static Location spawnLoc = ConfigSavers.getLocation("lobby_location");
    private static List<Player> currentPlayers = new ArrayList<>();

    public static Location getSpawnLoc() {
        return spawnLoc;
    }

    public static void setSpawnLoc(Location spawn_loc) {
        Lobby.spawnLoc = spawn_loc;
    }

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

    public static List<Player> getCurrentPlayers() {
        return currentPlayers;
    }

}
