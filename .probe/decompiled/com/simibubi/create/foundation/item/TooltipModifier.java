package com.simibubi.create.foundation.item;

import com.simibubi.create.foundation.utility.AttachedRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public interface TooltipModifier {

    AttachedRegistry<Item, TooltipModifier> REGISTRY = new AttachedRegistry<>(ForgeRegistries.ITEMS);

    TooltipModifier EMPTY = new TooltipModifier() {

        @Override
        public void modify(ItemTooltipEvent context) {
        }

        @Override
        public TooltipModifier andThen(TooltipModifier after) {
            return after;
        }
    };

    void modify(ItemTooltipEvent var1);

    default TooltipModifier andThen(TooltipModifier after) {
        return after == EMPTY ? this : tooltip -> {
            this.modify(tooltip);
            after.modify(tooltip);
        };
    }

    static TooltipModifier mapNull(@Nullable TooltipModifier modifier) {
        return modifier == null ? EMPTY : modifier;
    }
}