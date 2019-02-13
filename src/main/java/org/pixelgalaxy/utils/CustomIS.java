package org.pixelgalaxy.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.pixelgalaxy.game.Team;

import java.util.Arrays;

public class CustomIS {

    public static ItemStack getColoredChest(Team team){

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chest.getItemMeta();
        meta.setColor(team.getTeamColor());
        chest.setItemMeta(meta);

        return chest;
    }

    public static ItemStack getColorChooser(){

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowm = arrow.getItemMeta();
        arrowm.setDisplayName("§cColor chooser");
        arrowm.setLore(Arrays.asList("§5With this tool you can", "§5set your targets."));
        arrow.setItemMeta(arrowm);
        return arrow;
    }


}
