package org.pixelgalaxy.timers;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.game.GamePlayer;
import org.pixelgalaxy.game.roles.Alphawolf;
import org.pixelgalaxy.game.roles.Betawolf;
import org.pixelgalaxy.game.roles.NightRole;
import org.pixelgalaxy.game.roles.Role;
import org.pixelgalaxy.utils.CustomIS;
import org.pixelgalaxy.utils.ScoreHelper;

import java.time.LocalTime;

public class GameDayNightTimer extends BukkitRunnable {

    private int dayTime = Game.DAY_CYCLE_TIME;
    @Setter private int nightTime = Game.NIGHT_CYCLE_TIME;

    public static int amountToSelect = 0;
    public static GameDayNightTimer currentGameTimer = null;

    /**
     * Sets the time to night when timer is initiated,
     * saves the current timer object, teleports player to their locations,
     * checks if the player is a nightrole and if so, give the necessary items.
     * Also saves the amountToSelect that night in order to skip the timer if
     * everyone has selected their target.
     */
    public GameDayNightTimer() {
        currentGameTimer = this;
        Bukkit.getServer().getWorld("world").setTime(18000);

        for (GamePlayer gp : Game.getGamePlayers()) {
            Player p = gp.getPlayer();
            p.getPlayer().teleport((Location) WerewolfMain.config.get("teams." + gp.getPlayerTeam().getColorName().toLowerCase() + ".location"));
            Role playerRole = gp.getPlayerRole();
            if (playerRole instanceof NightRole) {

                if (!(playerRole instanceof Betawolf) || !Alphawolf.isAlive()) {

                    p.getInventory().setItem(8, CustomIS.getColorChooser());
                    amountToSelect++;
                }
            }
        }
    }

    /**
     * Starts a new DayNight timer
     */

    public static void start() {
        GameDayNightTimer timer = new GameDayNightTimer();
        timer.runTaskTimer(WerewolfMain.plugin, 0, 20);
    }

    /**
     * Runnable for keeping track of day and night in the game and activating the killcheck after night
     */

    @Override
    public void run() {

        if (nightTime > 0) {

            // Update all nighttimes on scoreboards
            int minutes = (int) Math.floor(nightTime / 60.0);
            ScoreHelper.updateAllPlayerScoreboards("Night", LocalTime.of(0, minutes, nightTime - (minutes * 60)));
            nightTime--;

            // When the night is over, remove all color choosers from nightroles their inventories and check the deaths from the night + clear target map for next night.
            if (nightTime == 0) {
                Bukkit.getServer().getWorld("world").setTime(0);
                for (GamePlayer gp : Game.getGamePlayers()) {

                    Player p = gp.getPlayer();
                    if (gp.getPlayerRole() instanceof NightRole) {
                        p.closeInventory();
                        p.getInventory().setItem(8, null);
                    }
                }

                Game.checkNightKills();
                Game.getTargetMap().clear();
            }

        } else if (dayTime > 0) {

            // Update daytime on all scoreboards
            int minutes = (int) Math.floor(dayTime / 60.0);
            ScoreHelper.updateAllPlayerScoreboards("Day", LocalTime.of(0, minutes, dayTime - (minutes * 60)));
            dayTime--;

        } else {
            // Start a new day night timer and cancel the current one
            start();
            this.cancel();
        }

    }

}
