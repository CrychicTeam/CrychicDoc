package net.mehvahdjukaar.supplementaries.mixins.forge.self;

import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.common.items.forge.QuiverItemImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ QuiverItem.class })
public abstract class SelfQuiverItem extends Item {

    protected SelfQuiverItem(Item.Properties arg) {
        super(arg);
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new QuiverItemImpl.QuiverCapability();
    }

    @Nullable
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag baseTag = stack.getTag();
        if (QuiverItemImpl.getQuiverData(stack) instanceof QuiverItemImpl.QuiverCapability c) {
            if (baseTag == null) {
                baseTag = new CompoundTag();
            }
            baseTag = baseTag.copy();
            baseTag.put("QuiverCap", c.serializeNBT());
        }
        return baseTag;
    }

    public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
        if (tag != null && tag.contains("QuiverCap")) {
            CompoundTag capTag = tag.getCompound("QuiverCap");
            tag.remove("QuiverCap");
            if (QuiverItemImpl.getQuiverData(stack) instanceof QuiverItemImpl.QuiverCapability c) {
                c.deserializeNBT(capTag);
            }
        }
        stack.setTag(tag);
    }
}