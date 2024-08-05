package com.mna.api.cantrips;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ICantrip {

    ICantrip dynamicItem(Item var1);

    ICantrip setDelay(int var1);

    ICantrip setSound(SoundEvent var1);

    ICantrip setIcon(ResourceLocation var1);

    ICantrip setRequiredAdvancement(ResourceLocation var1);

    int getDelay();

    SoundEvent getSound();

    int getTier();

    @Nullable
    ResourceLocation getRequiredAdvancement();

    ResourceLocation getId();

    ResourceLocation getIcon();

    boolean isStackLocked();

    ItemStack getDefaultStack();

    @Nonnull
    Item getValidDynamicItem();

    List<ResourceLocation> getDefaultCombination();
}