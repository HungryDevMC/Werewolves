package org.pixelgalaxy.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.roles.Role;
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.utils.CustomIS;

import java.util.*;

public class Game {

    public static final int NIGHT_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.night");
    public static final int DAY_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.day");

    private static List<String> customNames = WerewolfMain.config.getStringList("names");

    @Getter @Setter private static boolean started = false;

    @Getter private static Set<GamePlayer> gamePlayers = new HashSet<>();
    @Getter private static Map<GamePlayer, GamePlayer> targetMap = new HashMap<>();

    /**
     * To start the game timer and initialize all players in gameQueue
     */

    public static void start(){
        setStarted(true);
        initGamePlayers();
        GameDayNightTimer.start();
    }

    /**
     *  Gives player random role and team,
     *  aswell gives the player items for the game.
     *
     * @param p Player to assign role and team
     * @param team team (color) to assign to player
     * @param role role for player
     *
     */

    private static void addGamePlayer(Player p, Team team, Role role){

        getGamePlayers().add(GamePlayer.builder().player(p).playerTeam(team).playerRole(role).build());
        p.getEquipment().setChestplate(CustomIS.getColoredChest(team));
        CustomIS.giveImageMap(p, role);
        setRandomCustomName(p);

    }

    /**
     *  Gives the player a random custom name from the config
     *
     * @param p to set custom name on
     */

    private static void setRandomCustomName(Player p){
        Random random = new Random();
        int randomName = random.nextInt(customNames.size());
        String customName = customNames.get(randomName);

        p.setCustomNameVisible(true);
        p.setCustomName(customName);
        p.setDisplayName(customName);
        p.setPlayerListName(customName);
        customNames.remove(customName);
    }

    /**
     * Shuffles the players in gameQueue and initializes their game requirements
     */

    public static void initGamePlayers(){

        Collections.shuffle(Lobby.getCurrentPlayers());
        for(Player p : Lobby.getCurrentPlayers()){

            addGamePlayer(p, Team.getRandom(), Role.getRandom());

        }

    }

    /**
     * For checking who killed who in the night and sending
     * other roles information about their targets
     */

    public static void checkNightKills(){

    }

}
