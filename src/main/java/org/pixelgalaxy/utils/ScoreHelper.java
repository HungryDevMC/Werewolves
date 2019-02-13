package org.pixelgalaxy.utils;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.pixelgalaxy.game.Game;

/**
 *
 * @author crisdev333
 *
 */
public class ScoreHelper {

    private static HashMap<UUID, ScoreHelper> players = new HashMap<>();

    public static boolean hasScore(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public static ScoreHelper createScore(Player player) {
        return new ScoreHelper(player);
    }

    public static ScoreHelper getByPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public static ScoreHelper removeScore(Player player) {
        return players.remove(player.getUniqueId());
    }

    private Scoreboard scoreboard;
    private Objective sidebar;

    private ScoreHelper(Player player) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        // Create Teams
        for(int i=1; i<=15; i++) {
            Team team = scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }
        player.setScoreboard(scoreboard);
        players.put(player.getUniqueId(), this);
    }

    public void setTitle(String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);
        sidebar.setDisplayName(title.length()>32 ? title.substring(0, 32) : title);
    }

    public void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(slot);
        }

        text = ChatColor.translateAlternateColorCodes('&', text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if(scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    public void setSlotsFromList(List<String> list) {
        while(list.size()>15) {
            list.remove(list.size()-1);
        }

        int slot = list.size();

        if(slot<15) {
            for(int i=(slot +1); i<=15; i++) {
                removeSlot(i);
            }
        }

        for(String line : list) {
            setSlot(slot, line);
            slot--;
        }
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length()>16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if(s.length()>32) {
            s = s.substring(0, 32);
        }
        return s.length()>16 ? s.substring(16) : "";
    }

    public static void updatePlayerScoreboard(Player p, LocalTime time, String dayornight){
        ScoreHelper helper = ScoreHelper.createScore(p);
        helper.setTitle("&4&lWerewolves &8&l- &7&lInfo");
        helper.setSlot(9, "&8&m----------------");
        helper.setSlot(8, "&aName: &7" + p.getCustomName());
        helper.setSlot(7, "&8&m----------------");
        helper.setSlot(6, "&aColor: &7" + Game.getGamePlayers().get(p).getPlayerTeam().toString());
        helper.setSlot(5, "&8&m----------------");
        helper.setSlot(4, "&aRole: &7" + Game.getGamePlayers().get(p).getPlayerRole().getRoleName());
        helper.setSlot(3, "&8&m----------------");

        String extraZero = "";
        if(time.getSecond() < 10){
            extraZero = "0";
        }
        if(time.getSecond() == 0){
            helper.setSlot(2, "&a" + dayornight + ": &7" + "0" + ":" + "00");
        }else {
            helper.setSlot(2, "&a" + dayornight + ": &7" + time.getMinute() + ":" + extraZero + time.getSecond());
        }
        helper.setSlot(1, "&8&m----------------");
    }

}
