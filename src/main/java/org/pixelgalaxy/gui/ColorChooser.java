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
     * @return Inventory with all the other player chestplates and custom names on the item,
     * to select them as target.
     */

    public static Inventory getInv() {
        Inventory inv = Bukkit.createInventory(null, 36, "§cChoose wisely!");

        // 10 - 16, 19 - 25, 28 - 34

        for (int i = 1; i < Game.getGamePlayers().size() + 1; i++) {

            GamePlayer currentPlayer = (GamePlayer) Game.getGamePlayers().toArray()[i - 1];

            inv.setItem((10 + (i - 1) + (2 * ((int)Math.floor(i/6.0)))), CustomIS.getColoredChest(currentPlayer.getPlayerTeam(), currentPlayer.getPlayer()));

        }

        int lastIndex = Game.getGamePlayers().size();
        inv.setItem((10 + lastIndex + (2 * ((int)Math.floor(lastIndex/6.0)))), CustomIS.getBarrierBlock());

        return inv;
    }

}
