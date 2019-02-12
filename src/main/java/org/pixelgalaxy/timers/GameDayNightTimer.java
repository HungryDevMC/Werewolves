package org.pixelgalaxy.timers;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.DeathsInTheNight;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.game.Role;
import org.pixelgalaxy.utils.ConfigSavers;
import org.pixelgalaxy.utils.ScoreHelper;

import java.time.LocalTime;

public class GameDayNightTimer extends BukkitRunnable {

    private int dayTime = Game.DAY_CYCLE_TIME;
    @Setter
    private int nightTime = Game.NIGHT_CYCLE_TIME;

    public static int amountToSelect = 0;
    public static GameDayNightTimer currentGameTimer = null;

    public GameDayNightTimer() {
        amountToSelect = 0;
        currentGameTimer = this;
        Bukkit.getServer().getWorld("world").setTime(18000);

        for (Player p : Game.getGamePlayers().keySet()) {
            p.teleport(ConfigSavers.getLocation("team_locations." + Game.getGamePlayers().get(p).getPlayerTeam().toString().toLowerCase()));
            Role playerRole = Game.getGamePlayers().get(p).getPlayerRole();
            if (Game.NIGHT_ROLES.contains(playerRole)) {
                if (playerRole.equals(Role.BETA_WOLF) && !DeathsInTheNight.isAlphaIsAlive()) {
                    p.getInventory().setItem(8, Game.getColorChooser());
                    amountToSelect++;
                } else if (!playerRole.equals(Role.BETA_WOLF)) {
                    p.getInventory().setItem(8, Game.getColorChooser());
                    amountToSelect++;
                }
            }
        }
    }

    @Override
    public void run() {

        if (nightTime > 0) {
            int minutes = (int)Math.floor(nightTime / 60.0);
            for(Player p : Game.getGamePlayers().keySet()) {
                ScoreHelper.updatePlayerScoreboard(p, LocalTime.of(0, minutes, nightTime - (minutes * 60)), "Night");
            }

            nightTime--;

            if (nightTime == 0) {
                Bukkit.getServer().getWorld("world").setTime(0);
                for (Player p : Game.getGamePlayers().keySet()) {

                    if (Game.NIGHT_ROLES.contains(Game.getGamePlayers().get(p).getPlayerRole())) {
                        p.closeInventory();
                        p.getInventory().setItem(8, null);
                    }
                }
                DeathsInTheNight.checkForKills();
                Game.getRoleTargetMap().clear();
            }

        } else if (dayTime > 0) {

            int minutes = (int)Math.floor(dayTime / 60.0);
            for(Player p : Game.getGamePlayers().keySet()) {

                ScoreHelper.updatePlayerScoreboard(p, LocalTime.of(0, minutes, dayTime - (minutes * 60)), "Day");
            }

            dayTime--;

        } else {

            GameDayNightTimer timer = new GameDayNightTimer();
            timer.runTaskTimer(WerewolfMain.plugin, 0, 20);
            this.cancel();
        }

    }

}
