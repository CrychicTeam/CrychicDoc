package com.simibubi.create.content.trains.schedule;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Pair;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IScheduleInput {

    Pair<ItemStack, Component> getSummary();

    ResourceLocation getId();

    CompoundTag getData();

    void setData(CompoundTag var1);

    default int slotsTargeted() {
        return 0;
    }

    default List<Component> getTitleAs(String type) {
        ResourceLocation id = this.getId();
        return ImmutableList.of(Components.translatable(id.getNamespace() + ".schedule." + type + "." + id.getPath()));
    }

    default ItemStack getSecondLineIcon() {
        return ItemStack.EMPTY;
    }

    default void setItem(int slot, ItemStack stack) {
    }

    default ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Nullable
    default List<Component> getSecondLineTooltip(int slot) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    default void initConfigurationWidgets(ModularGuiLineBuilder builder) {
    }

    @OnlyIn(Dist.CLIENT)
    default boolean renderSpecialIcon(GuiGraphics graphics, int x, int y) {
        return false;
    }
}