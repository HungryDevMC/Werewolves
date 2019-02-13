package org.pixelgalaxy.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.utils.CustomIS;

public class ColorChooser {

    public static Inventory getInv(){
        Inventory inv = Bukkit.createInventory(null, 36, "§cChoose wisely!");

        for(int i = 0; i<Game.getGamePlayers().keySet().size(); i++){

            Player currentPlayer = (Player)Game.getGamePlayers().keySet().toArray()[i];

            if(i > 6){

                inv.setItem(i + 12, CustomIS.getColoredChest(currentPlayer));

            }else {

                inv.setItem(i + 10, CustomIS.getColoredChest(currentPlayer));

            }
        }
        return inv;
    }

}
