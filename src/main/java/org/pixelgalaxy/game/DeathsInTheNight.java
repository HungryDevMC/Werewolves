package org.pixelgalaxy.game;

import lombok.Getter;
import lombok.Setter;
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

    @Getter @Setter
    private static int hero_max_uses = WerewolfMain.config.getInt("role_info.hero.uses");
    @Getter @Setter
    private static int protector_max_uses = WerewolfMain.config.getInt("role_info.protector.uses");

    @Getter
    private static final List<Role> offensiveRoles = Arrays.asList(Role.ALPHA_WOLF, Role.BETA_WOLF, Role.OMEGA_WOLF, Role.MURDERER);

    @Getter @Setter
    private static boolean alphaIsAlive = true;
    @Getter @Setter
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

    }

}
