package org.pixelgalaxy.events;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.timers.Time;
import org.pixelgalaxy.utils.CustomIS;

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

    /**
     * Cancel inventory interaction and check if player clicked on target in color chooser,
     * if so then place the selected target in the targetmap
     *
     * @param e InventoryClickEvent
     */

    @EventHandler
    public void onInvInteract(InventoryClickEvent e) {
        ItemStack chest = e.getCurrentItem();
        e.setCancelled(true);

        if (e.getClickedInventory() != null && e.getClickedInventory().getTitle() != null && ChatColor.stripColor(e.getClickedInventory().getTitle()).equalsIgnoreCase("choose wisely!")) {

            Player p = (Player) e.getWhoClicked();

            GamePlayer target = null;
            for (GamePlayer gp : Game.getGamePlayers()) {

                if (CustomIS.getColoredChest(gp.getPlayerTeam(), gp.getPlayer()).equals(chest)) {
                    target = gp;
                    break;
                } else if (CustomIS.getBarrierBlock().equals(e.getCurrentItem())) {
                    target = null;
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
                }
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
