package org.pixelgalaxy.frameworks.gui.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.pixelgalaxy._2Business.frameworks.gui.objects.Gui;
import org.pixelgalaxy._2Business.frameworks.gui.objects.GuiItem;
import org.pixelgalaxy._2Business.frameworks.registry.AbstractListener;

import java.util.Optional;

public class GuiInteractListener extends AbstractListener {

    @EventHandler
    public void onGuiClose(InventoryCloseEvent e) {
        Gui openGui = Gui.getFromInventory((Player) e.getPlayer(), e.getInventory());
        if(openGui != null){
            openGui.executeClose(e);
        }

        if (Gui.isInventoryGui(e.getInventory())) {
            Gui.removeCachedGui(e.getInventory());
        }
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        // Only assign Gui if the clicked inventory is a GUI
        Gui openGui = Gui.getFromInventory((Player) e.getWhoClicked(), e.getClickedInventory());

        if (openGui != null) {
            e.setCancelled(true);
            int clickedSlot = e.getRawSlot();
            Optional<GuiItem> optionalClickedItem = openGui.getItemOnPosition(clickedSlot);

            optionalClickedItem.ifPresent(
                    clickedItem -> {
                        switch (e.getAction()) {
                            case PICKUP_ALL:
                                clickedItem.executeLeft(clickedItem, e);
                                break;

                            case PICKUP_HALF:
                                clickedItem.executeRight(clickedItem, e);
                                break;
                        }
                    }
            );
        }
    }

}
