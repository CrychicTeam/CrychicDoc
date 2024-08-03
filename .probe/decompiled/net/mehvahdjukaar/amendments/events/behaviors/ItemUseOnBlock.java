package net.mehvahdjukaar.amendments.events.behaviors;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

interface ItemUseOnBlock extends ItemUse {

    default boolean altersWorld() {
        return false;
    }

    default boolean shouldBlockMapToItem(Item item) {
        return this.appliesToItem(item);
    }

    @Nullable
    @Override
    default MutableComponent getTooltip() {
        return null;
    }

    default boolean placesBlock() {
        return false;
    }
}