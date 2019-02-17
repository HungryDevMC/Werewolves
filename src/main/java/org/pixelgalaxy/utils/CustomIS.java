package org.pixelgalaxy.utils;

import net.minecraft.server.v1_13_R1.Items;
import net.minecraft.server.v1_13_R1.WorldMap;
import net.minecraft.server.v1_13_R1.WorldServer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.pixelgalaxy.game.Team;
import org.pixelgalaxy.game.roles.Role;

import java.util.Arrays;

public class CustomIS {

    /**
     *
     * @param team The color the player has for chestplate
     * @return chestplate item with color of team
     */

    public static ItemStack getColoredChest(Team team, Player p){

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chest.getItemMeta();
        meta.setColor(team.getTeamColor());
        meta.setDisplayName("§7Player: §a" + p.getCustomName());
        chest.setItemMeta(meta);
        return chest;
    }

    /**
     *
     * @return a item that will let the player be able to select a target
     */

    public static ItemStack getColorChooser(){

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowm = arrow.getItemMeta();
        arrowm.setDisplayName("§cColor chooser");
        arrowm.setLore(Arrays.asList("§5With this tool you can", "§5set your targets."));
        arrow.setItemMeta(arrowm);
        return arrow;
    }

    /**
     *
     * @param p The player the map is for
     * @param r role of the player
     */

    public static void giveImageMap(Player p, Role r){

        net.minecraft.server.v1_13_R1.ItemStack itemStack = new net.minecraft.server.v1_13_R1.ItemStack(Items.FILLED_MAP);

        WorldServer worldServer = ((CraftWorld) p.getWorld()).getHandle();
        int id = worldServer.b("map");

        WorldMap worldMap = new WorldMap("map_" + id);
        worldMap.mapView.addRenderer(new RoleMapRenderer(r));

        worldServer.a(worldMap.getId(), worldMap);

        itemStack.getOrCreateTag().setInt("map", id);

        p.getInventory().addItem(CraftItemStack.asBukkitCopy(itemStack));

        ItemMeta mapm = p.getInventory().getItem(0).getItemMeta();
        mapm.setDisplayName("§7Role: " + r.getRoleName());
        p.getInventory().getItem(0).setItemMeta(mapm);
        p.updateInventory();

    }

    /**
     *
     * @return barrier block to select no target
     */

    public static ItemStack getBarrierBlock(){
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierm = barrier.getItemMeta();
        barrierm.setDisplayName("§cNone");
        barrierm.setLore(Arrays.asList("§7Clicking this item will", "§7select nobody"));
        barrier.setItemMeta(barrierm);
        return barrier;
    }

    public static ItemStack getYesNoStack(){

        ItemStack gallows = new ItemStack(Material.LEAD);
        ItemMeta gallowsm = gallows.getItemMeta();
        gallowsm.setDisplayName("§aKill player?");
        gallowsm.setLore(Arrays.asList("§7With this item you can", "§7choose whether or not you", "§7want to kill the player on", "§7the gallows."));
        gallows.setItemMeta(gallowsm);
        return gallows;
    }

}
