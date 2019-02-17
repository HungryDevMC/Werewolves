package org.pixelgalaxy.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class voteYesNo {

    public static ItemStack yes(){

        ItemStack yes = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta yesm = yes.getItemMeta();
        yesm.setDisplayName("§aYes");
        yes.setItemMeta(yesm);
        return yes;
    }

    public static ItemStack no(){

        ItemStack no = new ItemStack(Material.RED_CONCRETE);
        ItemMeta nom = no.getItemMeta();
        nom.setDisplayName("§cNo");
        no.setItemMeta(nom);
        return no;
    }

    public static Inventory yesNoInv(){

        Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL, "§aDo you want to kill this player?");
        inv.setItem(0, yes());
        inv.setItem(1, no());
        return inv;
    }

}
