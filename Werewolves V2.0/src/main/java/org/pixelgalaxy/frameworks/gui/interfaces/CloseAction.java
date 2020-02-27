package org.pixelgalaxy.frameworks.gui.interfaces;

import org.bukkit.event.inventory.InventoryCloseEvent;

@FunctionalInterface
public interface CloseAction extends ClickAction {
    void executeOnClose(InventoryCloseEvent inventoryCloseEvent);
}
