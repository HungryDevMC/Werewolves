package org.pixelgalaxy.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.game.GamePlayer;
import org.pixelgalaxy.utils.CustomIS;

public class ColorChooser {

    /**
     *
     * @return Inventory with all the other player chestplates and custom names on the item,
     * to select them as target.
     */

    public static Inventory getInv(){
        Inventory inv = Bukkit.createInventory(null, 36, "§cChoose wisely!");

        for(int i = 0; i<Game.getGamePlayers().size(); i++){

            GamePlayer currentPlayer = (GamePlayer) Game.getGamePlayers().toArray()[i];

            if(i > 6){

                inv.setItem(i + 12, CustomIS.getColoredChest(currentPlayer.getPlayerTeam()));

            }else {

                inv.setItem(i + 10, CustomIS.getColoredChest(currentPlayer.getPlayerTeam()));

            }
        }
        return inv;
    }

}
