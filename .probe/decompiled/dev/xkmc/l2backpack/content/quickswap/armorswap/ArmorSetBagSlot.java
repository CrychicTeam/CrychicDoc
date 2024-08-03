package dev.xkmc.l2backpack.content.quickswap.armorswap;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.content.common.BagSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

public class ArmorSetBagSlot extends BagSlot {

    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[] { InventoryMenu.EMPTY_ARMOR_SLOT_HELMET, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS };

    private final int index;

    public ArmorSetBagSlot(IItemHandlerModifiable handler, int index, int x, int y) {
        super(handler, index, x, y);
        this.index = index;
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[this.index / 9]);
    }
}