package org.pixelgalaxy.frameworks.gui.objects;

import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.pixelgalaxy.frameworks.gui.interfaces.CloseAction;

import java.util.*;

public class Gui implements InventoryHolder {

    private static final HashSet<Gui> CUSTOM_INVENTORIES = new HashSet<>();
    private Inventory gui;
    private HashMap<GuiItem, List<Integer>> guiItems = new HashMap<>();
    private CloseAction closeAction;

    public Gui(int rows, String title) {
        if(rows > 6 || rows < 0) {
            throw new IllegalArgumentException("Rows have to be between 0 and 6 (inclusive) for InventoryType chest");
        }
        this.gui = Bukkit.createInventory(this, rows * 9, title);
    }

    /**
     *
     * We need to create some sort of map that remembers which inventories are custom.
     * InventoryHolders for non chest GUI's are not saved anymore.
     *
     * @param invType type of inv
     * @param title title of inv
     */
    public Gui(InventoryType invType, String title){
        this.gui = Bukkit.createInventory(null, invType, title);
        CUSTOM_INVENTORIES.add(this);
    }

    public void onClose(CloseAction closeAction) {
        this.closeAction = closeAction;
    }

    public void executeClose(InventoryCloseEvent e){
        if(this.closeAction != null){
            this.closeAction.executeOnClose(e);
        }
    }

    public static void removeCachedGui(Inventory inv) {
        Optional<Gui> gui = getCachdGuiFromInv(inv);
        gui.ifPresent((CUSTOM_INVENTORIES::remove));
    }

    public static Optional<Gui> getCachdGuiFromInv(Inventory inv) {
        return CUSTOM_INVENTORIES.parallelStream().filter(gui -> gui.gui.equals(inv)).findFirst();
    }

    public static boolean isInventoryGui(Inventory inv){
        return CUSTOM_INVENTORIES.parallelStream().anyMatch(gui -> gui.gui.equals(inv));
    }

    public void addItem(GuiItem guiItem, Integer... slots) {
        guiItems.put(guiItem, Arrays.asList(slots));
        Arrays.stream(slots).forEach(slot -> this.gui.setItem(slot, guiItem));
    }

    public void addItem(GuiItem guiItem, Integer slot, Player player) {
        guiItems.put(guiItem, Arrays.asList(slot));
        if(slot > 53 && slot < 81){
            slot = slot - 45;
        }else if(slot > 80 && slot < 90){
            slot = slot - 81;
        }
        player.getInventory().setItem(slot, guiItem);
    }

    public void addItem(GuiItem guiItem, int[] slots){
        guiItems.put(guiItem, Ints.asList(slots));
        Arrays.stream(slots).forEach(slot -> this.gui.setItem(slot, guiItem));
    }

    public Optional<GuiItem> getItemOnPosition(int clickedSlot) {
        for(GuiItem item : guiItems.keySet()){
            if(guiItems.get(item).contains(clickedSlot)){
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public static Gui getFromInventory(Player player, Inventory inv) {
        Gui openGui = null;
        if (Gui.isInventoryGui(inv)) {
            Optional<Gui> optionalGui = Gui.getCachdGuiFromInv(inv);
            if(optionalGui.isPresent()){
                openGui = optionalGui.get();
            }
        } else if (player.getOpenInventory().getTopInventory().getHolder() instanceof Gui) {
            openGui = (Gui) player.getOpenInventory().getTopInventory().getHolder();
        }
        return openGui;
    }

    @Override
    public Inventory getInventory() {
        return gui;
    }
}
