package org.pixelgalaxy.events;

import com.google.common.base.Functions;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.game.GamePlayer;
import org.pixelgalaxy.game.roles.Alphawolf;
import org.pixelgalaxy.game.roles.Betawolf;
import org.pixelgalaxy.gui.voteYesNo;
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.timers.Time;
import org.pixelgalaxy.utils.CustomIS;

import java.util.*;
import java.util.stream.Collectors;

public class disableInventoryInteract implements Listener {

    private static GamePlayer getGamePlayerByPlayer(Player p) {

        for (GamePlayer gp : Game.getGamePlayers()) {

            if (gp.getPlayer() == p) {
                return gp;
            }

        }
        return null;
    }

    private static GamePlayer getBetaWolfGp() {
        for (GamePlayer gp : Game.getGamePlayers()) {
            if (gp.getPlayerRole() instanceof Betawolf) {
                return gp;
            }
        }
        return null;
    }

    private <V> V findMostFrequentItem(final Collection<V> items)
    {
        return items.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting())).entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Getter
    private static int currentYes;

    @Getter
    private static int currentVoted;

    /**
     * Cancel inventory interaction and check if player clicked on target in color chooser,
     * if so then place the selected target in the targetmap
     *
     * @param e InventoryClickEvent
     */

    @EventHandler
    public void onInvInteract(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() != null && e.getClickedInventory().getTitle() != null && ChatColor.stripColor(e.getClickedInventory().getTitle()).equalsIgnoreCase("choose wisely!")) {

            GamePlayer target = null;
            for (GamePlayer gp : Game.getGamePlayers()) {

                if (CustomIS.getColoredChest(gp.getPlayerTeam(), gp.getPlayer()).equals(clickedItem)) {
                    target = gp;
                    break;
                }

            }

            GamePlayer gpTargeter = getGamePlayerByPlayer(p);

            if (target != null && gpTargeter != null) {

                if (gpTargeter.getPlayerRole() instanceof Alphawolf) {
                    if (getBetaWolfGp() != null) {
                        Game.getTargetMap().put(getBetaWolfGp(), target);
                    } else {
                        Game.getTargetMap().put(gpTargeter, target);
                    }
                } else {
                    Game.getTargetMap().put(gpTargeter, target);
                }
                p.sendMessage(WerewolfMain.PREFIX + "You have selected the player: §7" + target.getCustomName() + " §aas your target!");
                p.getInventory().setItem(8, null);
                p.closeInventory();

            } else {
                p.sendMessage(WerewolfMain.PREFIX + "You have selected nobody!");
                Game.getTargetMap().put(gpTargeter, null);
                p.getInventory().setItem(8, null);
                p.closeInventory();
            }

            if (GameDayNightTimer.getCurrentTime().equals(Time.NIGHT)) {

                if (Game.getTargetMap().size() >= GameDayNightTimer.amountToSelect) {
                    GameDayNightTimer.currentGameTimer.setNightTime(1);
                }
            } else {
                if (target != null) {
                    Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The player: §7" + StringUtils.capitalize(gpTargeter.getPlayerTeam().getColorName()) + " §ahas voted to question: §7" +
                            StringUtils.capitalize(target.getPlayerTeam().getColorName()) + "§a.");

                }else{

                    Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The player: §7" + StringUtils.capitalize(gpTargeter.getPlayerTeam().getColorName()) + " §ahas voted to question nobody this night.");

                }

                Bukkit.broadcastMessage("targetmap: " + Game.getTargetMap().size());
                Bukkit.broadcastMessage("gameplayers: " + Game.getGamePlayers().size());

                if(Game.getTargetMap().size() >= Game.getGamePlayers().size()){

                    GamePlayer mostFrequentGamePlayer = findMostFrequentItem(Game.getTargetMap().values());

                    mostFrequentGamePlayer.getPlayer().teleport((Location) WerewolfMain.config.get("gallows_location"));
                    Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The player: §7" + StringUtils.capitalize(mostFrequentGamePlayer.getPlayerTeam().getColorName()) + " §ahas been put on the gallows!");

                    disableMovement.setGallowPlayer(mostFrequentGamePlayer);

                    currentYes = 0;
                    currentVoted = 0;

                    for(GamePlayer gp : Game.getGamePlayers()){

                        if(gp != disableMovement.getGallowPlayer()){

                            gp.getPlayer().getInventory().setItem(7, CustomIS.getYesNoStack());

                        }

                    }

                }

            }

        }else if(e.getClickedInventory() != null && e.getClickedInventory().getTitle() != null && ChatColor.stripColor(e.getClickedInventory().getTitle()).equalsIgnoreCase("Do you want to kill this player?")){

            if(clickedItem.equals(voteYesNo.yes())){
                currentYes++;
                p.getInventory().setItem(7, null);
                Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The player: §7" + p.getCustomName() +" §ahas voted to execute the player on the gallows!");
            }else if(clickedItem.equals(voteYesNo.no())){
                p.getInventory().setItem(7, null);
                Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The player: §7" + p.getCustomName() + " §ahas voted to release the player on the gallows!");
            }

            currentVoted++;

            if(currentYes > (Game.getGamePlayers().size() - 1)){
                Game.killPlayer(disableMovement.getGallowPlayer());
                Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The player on the gallows has been executed!");
            }

            if(currentVoted == Game.getGamePlayers().size() - 1){
                disableMovement.setGallowPlayer(null);
                Bukkit.broadcastMessage(WerewolfMain.PREFIX + "The player on the gallows has been released!");
            }

        }

    }

    /**
     * Cancel player dropping items
     *
     * @param e PlayerDropEvent
     */

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}
