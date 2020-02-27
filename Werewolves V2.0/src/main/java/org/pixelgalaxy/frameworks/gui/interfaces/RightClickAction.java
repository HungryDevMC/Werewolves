package org.pixelgalaxy.frameworks.gui.interfaces;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.pixelgalaxy.frameworks.gui.objects.GuiItem;

@FunctionalInterface
public interface RightClickAction extends ClickAction {
    void executeRight(GuiItem item, InventoryClickEvent event);
}
