package org.pixelgalaxy.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.pixelgalaxy.gui.ColorChooser;
import org.pixelgalaxy.gui.voteYesNo;
import org.pixelgalaxy.utils.CustomIS;

public class ColorChooserEvent implements Listener {

    /**
     *  When player interacts with the color chooser for setting a target, open the target chooser inventory
     * @param e PlayerInteract
     */

    @EventHandler
    public void onColorChooser(PlayerInteractEvent e){

        if(e.getItem() != null && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))){

            if(e.getItem().equals(CustomIS.getColorChooser())) {

                e.getPlayer().openInventory(ColorChooser.getInv());

            }else if(e.getItem().equals(CustomIS.getYesNoStack())){

                e.getPlayer().openInventory(voteYesNo.yesNoInv());

            }

        }

    }

}
