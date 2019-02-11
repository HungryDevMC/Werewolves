package org.pixelgalaxy.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.gui.ColorChooser;

public class ColorChooserEvent implements Listener {

    @EventHandler
    public void onColorChooser(PlayerInteractEvent e){

        if(e.getItem() != null && e.getItem().equals(Game.getColorChooser()) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))){

            e.getPlayer().openInventory(ColorChooser.getInv());

        }

    }

}