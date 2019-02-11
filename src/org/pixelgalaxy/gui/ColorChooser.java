package org.pixelgalaxy.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pixelgalaxy.game.Game;

public class ColorChooser {

    private static ItemStack getPlayerWool(Player p){

        ItemStack wool = new ItemStack(Material.LEGACY_WOOL, 1, (short) Game.getGamePlayers().get(p).getPlayerTeam().getWoolColor());
        ItemMeta woolm = wool.getItemMeta();
        woolm.setDisplayName("§7Player: §a" + p.getDisplayName());
        wool.setItemMeta(woolm);

        return wool;
    }

    public static Inventory getInv(){
        Inventory inv = Bukkit.createInventory(null, 36, "§cChoose wisely!");

        for(int i = 0; i<Game.getGamePlayers().keySet().size(); i++){

            Player currentPlayer = (Player)Game.getGamePlayers().keySet().toArray()[i];

            if(i > 6){

                inv.setItem(i + 12, getPlayerWool(currentPlayer));

            }else {

                inv.setItem(i + 10, getPlayerWool(currentPlayer));

            }
        }
        return inv;
    }

}
