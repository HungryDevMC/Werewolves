package org.pixelgalaxy.frameworks.gui.objects;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.pixelgalaxy.frameworks.gui.interfaces.ClickAction;
import org.pixelgalaxy.frameworks.gui.interfaces.LeftClickAction;
import org.pixelgalaxy.frameworks.gui.interfaces.RightClickAction;

import java.util.ArrayList;
import java.util.List;

@ToString()
public class GuiItem extends ItemStack implements LeftClickAction, RightClickAction {

    private List<ClickAction> itemClickActions = new ArrayList<>();

    public GuiItem clearClickActions() {
        this.itemClickActions.clear();
        return this;
    }

    public GuiItem setHead(String playerName) {
        this.setType(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) this.getItemMeta();
        meta.setOwner(playerName);
        this.setItemMeta(meta);
        return this;
    }

    public GuiItem addItemFlags(ItemFlag... flagsToAdd) {
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(flagsToAdd);
        super.setItemMeta(meta);
        return this;
    }

    public GuiItem addItemFlag(ItemFlag flagToAdd) {
        return addItemFlags(flagToAdd);
    }

    @Builder(builderMethodName = "create")
    public GuiItem(@NonNull Material material, int amount, int durability, @NonNull String displayName, List<String> lore, boolean unbreakable, boolean enchanted, LeftClickAction leftClickAction, RightClickAction rightClickAction) {
        super(material, amount == 0 ? 1 : amount);
        ItemMeta newMeta = super.getItemMeta();

        if(durability > 0){
            Damageable damageable = (Damageable)newMeta;
            damageable.setDamage(durability);
            super.setItemMeta((ItemMeta)damageable);
        }

        newMeta = super.getItemMeta();

        if (newMeta != null) {

            newMeta.setDisplayName(displayName);
            if (lore != null) {
                newMeta.setLore(lore);
            }
            if(unbreakable){
                newMeta.setUnbreakable(true);
            }
            if (enchanted) {
                this.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
                newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            super.setItemMeta(newMeta);

            if (leftClickAction != null) {
                itemClickActions.add(leftClickAction);
            }
            if (rightClickAction != null) {
                itemClickActions.add(rightClickAction);
            }
        }
    }

    public GuiItem addLeftClickAction(LeftClickAction leftClickAction){
        itemClickActions.add(leftClickAction);
        return this;
    }

    public GuiItem addRightClickAction(RightClickAction rightClickAction) {
        itemClickActions.add(rightClickAction);
        return this;
    }

    @Override
    public void executeLeft(GuiItem item, InventoryClickEvent event) {
        itemClickActions.stream().filter(action -> action instanceof LeftClickAction).forEach(clickAction -> ((LeftClickAction) clickAction).executeLeft(item, event));
    }

    @Override
    public void executeRight(GuiItem item, InventoryClickEvent event) {
        itemClickActions.stream().filter(action -> action instanceof RightClickAction).forEach(clickAction -> ((RightClickAction) clickAction).executeRight(item, event));
    }
}
