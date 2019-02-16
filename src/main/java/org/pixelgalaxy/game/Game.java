package org.pixelgalaxy.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.roles.*;
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.utils.CustomIS;
import org.pixelgalaxy.utils.RolePrioritySorter;

import java.util.*;

public class Game {

    public static final int NIGHT_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.night");
    public static final int DAY_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.day");

    private static List<String> customNames = WerewolfMain.config.getStringList("names");

    @Getter
    @Setter
    private static boolean started = false;

    @Getter
    private static List<GamePlayer> gamePlayers = new ArrayList<>();
    @Getter
    @Setter
    private static Map<GamePlayer, GamePlayer> targetMap = new HashMap<>();
    @Getter
    private static List<GamePlayer> killedPlayers = new ArrayList<>();
    @Getter
    private static List<GamePlayer> heroTargets = new ArrayList<>();

    /**
     * To start the game timer and initialize all players in gameQueue
     */

    public static void start() {
        setStarted(true);
        initGamePlayers();
        GameDayNightTimer.start();
    }

    /**
     * Gives player random role and team,
     * aswell gives the player items for the game.
     *
     * @param p    Player to assign role and team
     * @param team team (color) to assign to player
     * @param role role for player
     */

    private static void addGamePlayer(Player p, Team team, Role role) {

        getGamePlayers().add(GamePlayer.builder().player(p).playerTeam(team).playerRole(role).build());
        p.getEquipment().setChestplate(CustomIS.getColoredChest(team, p));
        CustomIS.giveImageMap(p, role);
        setRandomCustomName(p);
        PlayerMode.set(p, PlayerMode.INGAME);

    }

    /**
     * Gives the player a random custom name from the config
     *
     * @param p to set custom name on
     */

    private static void setRandomCustomName(Player p) {
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

    public static void initGamePlayers() {

        Collections.shuffle(Lobby.getCurrentPlayers());
        for (Player p : Lobby.getCurrentPlayers()) {

            addGamePlayer(p, Team.getRandom(), Role.getRandom());

        }

    }

    /**
     * Randomizes keyset of roles that selected a target and then sort it by the priority of the roles
     *
     * @return sorted map by role priority
     */

    public static LinkedHashMap<GamePlayer, GamePlayer> getSortedTargetMap() {

        LinkedHashMap<GamePlayer, GamePlayer> sortedMap = new LinkedHashMap<>();

        List<GamePlayer> gpKeys = new ArrayList<>(Game.getTargetMap().keySet());
        Collections.sort(gpKeys, new RolePrioritySorter());

        for(GamePlayer gp : gpKeys){
            if(Game.getTargetMap().get(gp) != null) {
                sortedMap.put(gp, Game.getTargetMap().get(gp));
            }
        }
        return sortedMap;
    }

    /**
     * For checking who killed who in the night and sending
     * other roles information about their targets
     */

    public static void checkNightKills() {

        killedPlayers.clear();
        heroTargets.clear();

        for (GamePlayer gp : getSortedTargetMap().keySet()) {

            if (getTargetMap().containsKey(gp)) {

                GamePlayer target = Game.getTargetOfPlayer(gp);

                if (target != null) {

                    switch (gp.getPlayerRole().getClass().getSimpleName().toUpperCase()) {

                        case "WATCHER":

                            List<GamePlayer> playersVisited = new ArrayList<>();

                            for (GamePlayer gp2 : getTargetMap().keySet()) {

                                if (Game.getTargetOfPlayer(gp2) != null && Game.getTargetOfPlayer(gp2).equals(target)) {

                                    if (gp != gp2) {
                                        playersVisited.add(gp2);
                                    }

                                }

                            }

                            if(playersVisited.size() > 0) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(playersVisited.get(0).getPlayer().getCustomName());
                                playersVisited.remove(playersVisited.get(0));
                                for (GamePlayer gpVisit : playersVisited) {
                                    sb.append(", " + gpVisit.getPlayer().getCustomName());
                                }
                                gp.getPlayer().sendMessage(WerewolfMain.PREFIX + "§aThe players: §7" + sb.toString() + " §ahave visited §7" + target.getPlayer().getCustomName() + " §athis night!");
                            }else{
                                gp.getPlayer().sendMessage(WerewolfMain.PREFIX + "Nobody has visited §7" + target.getCustomName() + " §atonight!");
                            }

                            break;

                        case "TEMPTRESS":

                            Game.getTargetMap().remove(target);

                            break;

                        case "MAYOR":

                            String status = "INNOCENT";
                            GamePlayer omegaTarget = Omegawolf.getTarget();
                            if (target.getPlayerRole().isBad() || (omegaTarget != null && omegaTarget == target && !Alphawolf.isAlive() && !Betawolf.isAlive())) {
                                status = "GUILTY";
                            }
                            gp.getPlayer().sendMessage(WerewolfMain.PREFIX + "The player: §7" + target.getPlayer().getCustomName() + " §ais §7" + status + "§a!");
                            break;

                        case "PROTECTOR":

                            boolean hasProtected = false;

                            for (GamePlayer gp2 : getSortedTargetMap().keySet()) {

                                if (Game.getTargetMap().get(gp2) == target && gp2.getPlayerRole().isBad()) {
                                    hasProtected = true;
                                    Game.getTargetMap().remove(gp2);
                                }

                            }

                            if (hasProtected) {
                                ((Protector) gp.getPlayerRole()).setUses((((Protector) gp.getPlayerRole()).getUses()) - 1);

                                if ((((Protector) gp.getPlayerRole()).getUses()) < 0) {
                                    killPlayer(gp);
                                    gp.getPlayer().sendMessage(WerewolfMain.PREFIX + "§cYou have died protecting someone!");
                                }
                            }

                            break;

                        case "HERO":

                            heroTargets.add(target);
                            Bukkit.broadcastMessage(target.getCustomName() + " has been added to the hero targets!");
                            ((Hero) gp.getPlayerRole()).setUses(((Hero) gp.getPlayerRole()).getUses() - 1);
                            gp.getPlayer().sendMessage(WerewolfMain.PREFIX + "You have §7" + ((Hero)gp.getPlayerRole()).getUses() + " §auses remaining!");

                            break;

                        case "OMEGAWOLF":

                            if (!Alphawolf.isAlive() && !Betawolf.isAlive()) {

                                killPlayer(target, gp);

                            }

                            break;

                        case "BETAWOLF":

                            killPlayer(target, gp);

                            break;

                        case "ALPHAWOLF":

                            killPlayer(target, gp);

                            break;

                        case "MURDERER":

                            killPlayer(target, gp);

                            break;

                    }
                }
            }
        }

        if (killedPlayers.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(killedPlayers.get(0).getPlayer().getCustomName());
            killedPlayers.remove(killedPlayers.get(0));
            for (GamePlayer gpVisit : killedPlayers) {
                sb.append(", " + gpVisit.getPlayer().getCustomName());
            }
            Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The players: §7" + sb.toString() + " §ahave been killed tonight!");
        } else {
            Bukkit.broadcastMessage(WerewolfMain.PREFIX + "Nobody has been killed tonight!");
        }

        checkForWinningTeam();
    }

    /**
     * @param gp The player that has set a target
     * @return the player that is the target of the player gp
     */

    public static GamePlayer getTargetOfPlayer(GamePlayer gp) {
        GamePlayer gpTarget = null;
        if (Game.getTargetMap().containsKey(gp)) {
            gpTarget = Game.getTargetMap().get(gp);
        }
        return gpTarget;
    }

    /**
     * Kill player and remove their data from game + make spectator
     *
     * @param gp GamePlayer
     */

    public static void killPlayer(GamePlayer gp, GamePlayer targeter) {

        if (!heroTargets.contains(gp)) {

            killPlayer(gp);
        } else {
            Bukkit.broadcastMessage(gp.getCustomName() + " is in hero targets, killing " + targeter.getCustomName());
            killPlayer(targeter);
        }

    }

    public static void killPlayer(GamePlayer gp) {

        Player p = gp.getPlayer();
        if (gp.getPlayerRole() instanceof Alphawolf) {
            Alphawolf.setAlive(false);
        } else if (gp.getPlayerRole() instanceof Betawolf) {
            Betawolf.setAlive(false);
        } else if (gp.getPlayerRole() instanceof Omegawolf) {
            Omegawolf.setAlive(false);
        }
        getGamePlayers().remove(gp);
        getTargetMap().remove(gp);
        PlayerMode.set(p, PlayerMode.DEAD);
        killedPlayers.add(gp);

    }

    public static void checkForWinningTeam() {

        List<RoleTeam> roleTeamAlive = new ArrayList<>();
        for (GamePlayer gp : getGamePlayers()) {

            if (!roleTeamAlive.contains(gp.getPlayerRole().getRoleTeam())) {
                roleTeamAlive.add(gp.getPlayerRole().getRoleTeam());
            }

        }

        if(roleTeamAlive.size() <= 1) {
            if (roleTeamAlive.size() == 1) {
                Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The team: §7" + roleTeamAlive.get(0).getTeamName() + "§a has won the game!");
            } else if (roleTeamAlive.size() == 0) {
                Bukkit.broadcastMessage(WerewolfMain.PREFIX + "Everyone was killed! Nobody has won the game.");
            }
            for(GamePlayer gp : Game.getGamePlayers()){
                PlayerMode.set(gp.getPlayer(), PlayerMode.SPECTATOR);
            }
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }
    }

}
