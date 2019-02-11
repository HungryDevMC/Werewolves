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
import org.pixelgalaxy.timers.GameDayNightTimer;

public class disableInventoryInteract implements Listener {

    @EventHandler
    public void onInvInteract(InventoryClickEvent e){
        ItemStack wool = e.getCurrentItem();
        e.setCancelled(true);

        if(ChatColor.stripColor(e.getClickedInventory().getTitle()).equalsIgnoreCase("choose wisely!")){

            Player p = (Player) e.getWhoClicked();

            Player target = null;
            for(Player pGame : Game.getGamePlayers().keySet()){

                if(Game.getGamePlayers().get(pGame).getPlayerTeam().getWoolColor() == wool.getDurability()){
                    target = pGame;
                    break;
                }

            }

            if(target != null) {

                Game.addTarget(p, target);
                p.sendMessage(WerewolfMain.PREFIX + "You have selected the player: §7" + target.getDisplayName() + " §aas your target!");
                p.getInventory().setItem(8, null);
                p.closeInventory();
                if(Game.getRoleTargetMap().size() >= GameDayNightTimer.amountToSelect){
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
