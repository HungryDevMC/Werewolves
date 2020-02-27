package org.pixelgalaxy.frameworks.gui.interfaces;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.pixelgalaxy.frameworks.gui.objects.GuiItem;

@FunctionalInterface
public interface LeftClickAction extends ClickAction {
    void executeLeft(GuiItem item, InventoryClickEvent event);
}
