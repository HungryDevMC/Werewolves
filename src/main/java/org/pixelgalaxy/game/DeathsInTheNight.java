package org.pixelgalaxy.game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.timers.GameDayNightTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeathsInTheNight {

    private static int hero_max_uses = WerewolfMain.config.getInt("role_info.hero.uses");
    private static int protector_max_uses = WerewolfMain.config.getInt("role_info.protector.uses");

    private static final List<Role> offensiveRoles = Arrays.asList(Role.ALPHA_WOLF, Role.BETA_WOLF, Role.OMEGA_WOLF, Role.MURDERER);

    private static boolean alphaIsAlive = true;

    private static boolean betaIsAlive = true;

    private static List<Integer> canWin = Arrays.asList(1, 2, 3);

    private static void killPlayer(Player p) {
        p.getInventory().clear();
        p.setHealth(0);
        if (getPlayerRole(p).equals(Role.ALPHA_WOLF)) {
            setAlphaIsAlive(false);
        }
        if (getPlayerRole(p).equals(Role.BETA_WOLF)) {
            setBetaIsAlive(false);
        }
        p.setGameMode(GameMode.SPECTATOR);
        p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        Game.getGamePlayers().remove(p);

        List<Integer> teamsAlive = new ArrayList<>();
        for(GamePlayer gp : Game.getGamePlayers().values()){

            if(!teamsAlive.contains(gp.getPlayerRole().getTeam()) && gp.getPlayerRole().getTeam() != 4){

                teamsAlive.add(gp.getPlayerRole().getTeam());

            }

        }
        canWin = teamsAlive;
        if(canWin.size() == 1){
            GameDayNightTimer.currentGameTimer.cancel();
            Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The " + Role.ROLE_TEAM_NAMES.get(canWin.get(0)) + " have won the game!");
            new BukkitRunnable(){
                public void run(){
                    Bukkit.getServer().spigot().restart();
                }
            }.runTaskLater(WerewolfMain.plugin, 200);
        }

    }

    private static Role getPlayerRole(Player p) {

        for (Player pGame : Game.getGamePlayers().keySet()) {

            if (pGame.equals(p)) {
                return Game.getGamePlayers().get(p).getPlayerRole();
            }

        }
        return null;
    }

    private static Player getRolePlayer(Role role) {

        for (Player pGame : Game.getGamePlayers().keySet()) {
            Role pGameRole = Game.getGamePlayers().get(pGame).getPlayerRole();
            if (pGameRole.equals(role)) {
                return pGame;
            }

        }
        return null;
    }

    public static void checkForKills() {

        List<Player> playersKilled = new ArrayList<>();

        // Remove the role from the map that has been tempted
        if (Game.getRoleTargetMap().keySet().contains(Role.TEMPTRESS)) {

            Player target = Game.getRoleTargetMap().get(Role.TEMPTRESS);
            Game.getRoleTargetMap().remove(getPlayerRole(target));
            if (getPlayerRole(target).equals(Role.ALPHA_WOLF) && isBetaIsAlive()) {

                Game.getRoleTargetMap().remove(Role.BETA_WOLF);

            }

        }

        // Send all names of the players that visited target of watcher
        if (Game.getRoleTargetMap().keySet().contains(Role.WATCHER)) {
            List<Player> watcherSeen = new ArrayList<>();
            // The target of the watcher
            Player targetWatcher = Game.getRoleTargetMap().get(Role.WATCHER);

            // All roles that chose a target
            for (Role r : Game.getRoleTargetMap().keySet()) {
                // Target of specific role
                Player roleTarget = Game.getRoleTargetMap().get(r);
                // If the target of the role is the same as watcher, add the visitor to the lsit
                if (roleTarget.equals(targetWatcher)) {

                    watcherSeen.add(getRolePlayer(r));

                }
            Game.getRoleTargetMap().remove(Role.WATCHER);
            }
            StringBuilder seen = new StringBuilder();
            for (Player p : watcherSeen) {
                seen.append(p.getCustomName() + " ");
            }
            getRolePlayer(Role.WATCHER).sendMessage(WerewolfMain.PREFIX + "§7" + watcherSeen.toString() + "§a have visited the player: §7" + targetWatcher + " §atonight.");
        }

        // Let mayor check on player
        if (Game.getRoleTargetMap().keySet().contains(Role.MAYOR)) {

            String status = "INNOCENT";
            if ((isBetaIsAlive() || isAlphaIsAlive()) && Game.getRoleTargetMap().keySet().contains(Role.OMEGA_WOLF)) {

                // If the omega wolf has the same target as mayor and is not killing
                if (Game.getRoleTargetMap().get(Role.MAYOR).equals(Game.getRoleTargetMap().get(Role.OMEGA_WOLF))) {

                    status = "GUILTY";

                }

            } else if (offensiveRoles.contains(getPlayerRole(Game.getRoleTargetMap().get(Role.MAYOR)))) {

                status = "GUILTY";

            }

            getRolePlayer(Role.MAYOR).sendMessage(WerewolfMain.PREFIX + "The player:§7 " + Game.getRoleTargetMap().get(Role.MAYOR).getCustomName() + " §ais §7" + status);
            Game.getRoleTargetMap().remove(Role.MAYOR);

        }

        // Check if protector has protected someone
        if (Game.getRoleTargetMap().keySet().contains(Role.PROTECTOR) && protector_max_uses > 0) {

            Player protectorTarget = Game.getRoleTargetMap().get(Role.PROTECTOR);
            boolean hasProtected = false;

            for (Role r : Game.getRoleTargetMap().keySet()) {

                if (Game.getRoleTargetMap().get(r).equals(protectorTarget)) {

                    if (offensiveRoles.contains(r)) {

                        if (!hasProtected) {
                            protector_max_uses--;
                            hasProtected = true;
                        }

                        Game.getRoleTargetMap().remove(r);

                    }

                }

            }

            Game.getRoleTargetMap().remove(Role.PROTECTOR);

        }

        // Check if the target of the hero got attacked by offensive player
        if (Game.getRoleTargetMap().keySet().contains(Role.HERO) && hero_max_uses > 0) {

            hero_max_uses--;
            Player heroTarget = Game.getRoleTargetMap().get(Role.HERO);

            for (Role r : Game.getRoleTargetMap().keySet()) {

                if (heroTarget.equals(Game.getRoleTargetMap().get(r))) {

                    if (offensiveRoles.contains(r)) {

                        Player killed = getRolePlayer(r);
                        killPlayer(killed);
                        playersKilled.add(killed);
                        Game.getRoleTargetMap().remove(r);

                    }

                }

            }

            Game.getRoleTargetMap().remove(Role.HERO);

        }

        // Check if wolfs killed someone
        if (Game.getRoleTargetMap().containsKey(Role.BETA_WOLF)) {

            Player killed = Game.getRoleTargetMap().get(Role.BETA_WOLF);
            killPlayer(killed);
            playersKilled.add(killed);
            Game.getRoleTargetMap().remove(getPlayerRole(killed));

        } else if (Game.getRoleTargetMap().containsKey(Role.ALPHA_WOLF)) {

            Player killed = Game.getRoleTargetMap().get(Role.ALPHA_WOLF);
            killPlayer(killed);
            playersKilled.add(killed);
            Game.getRoleTargetMap().remove(getPlayerRole(killed));

        } else {

            if (!isAlphaIsAlive() && !isBetaIsAlive() && Game.getRoleTargetMap().containsKey(Role.OMEGA_WOLF)) {

                Player killed = Game.getRoleTargetMap().get(Role.OMEGA_WOLF);
                killPlayer(killed);
                playersKilled.add(killed);
                Game.getRoleTargetMap().remove(getPlayerRole(killed));

            }

        }

        if (Game.getRoleTargetMap().containsKey(Role.MURDERER)) {

            Player killed = Game.getRoleTargetMap().get(Role.MURDERER);
            killPlayer(killed);
            playersKilled.add(killed);

        }

        StringBuilder killed = new StringBuilder();
        for (Player p : playersKilled) {
            killed.append(p.getCustomName() + " ");
        }
        if (playersKilled.size() == 0) {
            Bukkit.broadcastMessage(WerewolfMain.PREFIX + "Nobody has been killed tonight!");
        } else {
            Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The players §7" + killed.toString() + " §ahave been killed this night!");
        }

        Game.getRoleTargetMap().clear();

    }

    public static void setAlphaIsAlive(boolean alphaIsAlive) {
        DeathsInTheNight.alphaIsAlive = alphaIsAlive;
    }

    public static void setBetaIsAlive(boolean betaIsAlive) {
        DeathsInTheNight.betaIsAlive = betaIsAlive;
    }

    public static boolean isAlphaIsAlive() {
        return alphaIsAlive;
    }

    public static boolean isBetaIsAlive() {
        return betaIsAlive;
    }
}
