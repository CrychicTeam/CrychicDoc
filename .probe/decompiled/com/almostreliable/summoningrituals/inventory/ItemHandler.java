package com.almostreliable.summoningrituals.inventory;

import com.almostreliable.summoningrituals.platform.TagSerializable;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ItemHandler extends IItemHandlerModifiable, TagSerializable<CompoundTag> {

    List<ItemStack> getNoneEmptyItems();

    ItemStack getCatalyst();
}