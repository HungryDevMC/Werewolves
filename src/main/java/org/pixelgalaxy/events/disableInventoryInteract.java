package org.pixelgalaxy.events;

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
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.utils.CustomIS;

public class disableInventoryInteract implements Listener {

    @EventHandler
    public void onInvInteract(InventoryClickEvent e){
        ItemStack chest = e.getCurrentItem();
        e.setCancelled(true);

        if(ChatColor.stripColor(e.getClickedInventory().getTitle()).equalsIgnoreCase("choose wisely!")){

            Player p = (Player) e.getWhoClicked();

            GamePlayer target = null;
            for(GamePlayer gp : Game.getGamePlayers()){

                if(CustomIS.getColoredChest(gp.getPlayerTeam()) == chest){
                    target = gp;
                    break;
                }

            }

            if(target != null) {

                //Game.addTarget()
                p.sendMessage(WerewolfMain.PREFIX + "You have selected the player: §7" + target.getCustomName() + " §aas your target!");
                p.getInventory().setItem(8, null);
                p.closeInventory();
                if(Game.getTargetMap().size() >= GameDayNightTimer.amountToSelect){
                    GameDayNightTimer.currentGameTimer.setNightTime(1);
                }

            }else{
                p.sendMessage(WerewolfMain.PREFIX + "§cTarget is null!");
            }

        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

}
