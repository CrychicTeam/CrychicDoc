package dev.ftb.mods.ftblibrary.core.mixin.common;

import dev.ftb.mods.ftblibrary.core.ItemFTBL;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Item.class })
public abstract class ItemMixin implements ItemFTBL {

    @Mutable
    @Accessor("craftingRemainingItem")
    @Override
    public abstract void setCraftingRemainingItemFTBL(Item var1);
}