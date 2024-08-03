package com.simibubi.create.content.trains.schedule.condition;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RedstoneLinkCondition extends ScheduleWaitCondition {

    public Couple<RedstoneLinkNetworkHandler.Frequency> freq = Couple.create(() -> RedstoneLinkNetworkHandler.Frequency.EMPTY);

    @Override
    public int slotsTargeted() {
        return 2;
    }

    @Override
    public Pair<ItemStack, Component> getSummary() {
        return Pair.of(AllBlocks.REDSTONE_LINK.asStack(), this.lowActivation() ? Lang.translateDirect("schedule.condition.redstone_link_off") : Lang.translateDirect("schedule.condition.redstone_link_on"));
    }

    @Override
    public List<Component> getSecondLineTooltip(int slot) {
        return ImmutableList.of(Lang.translateDirect(slot == 0 ? "logistics.firstFrequency" : "logistics.secondFrequency").withStyle(ChatFormatting.RED));
    }

    @Override
    public List<Component> getTitleAs(String type) {
        return ImmutableList.of(Lang.translateDirect("schedule.condition.redstone_link.frequency_" + (this.lowActivation() ? "unpowered" : "powered")), Components.literal(" #1 ").withStyle(ChatFormatting.GRAY).append(this.freq.getFirst().getStack().getHoverName().copy().withStyle(ChatFormatting.DARK_AQUA)), Components.literal(" #2 ").withStyle(ChatFormatting.GRAY).append(this.freq.getSecond().getStack().getHoverName().copy().withStyle(ChatFormatting.DARK_AQUA)));
    }

    @Override
    public boolean tickCompletion(Level level, Train train, CompoundTag context) {
        int lastChecked = context.contains("LastChecked") ? context.getInt("LastChecked") : -1;
        int status = Create.REDSTONE_LINK_NETWORK_HANDLER.globalPowerVersion.get();
        if (status == lastChecked) {
            return false;
        } else {
            context.putInt("LastChecked", status);
            return Create.REDSTONE_LINK_NETWORK_HANDLER.hasAnyLoadedPower(this.freq) != this.lowActivation();
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.freq.set(slot == 0, RedstoneLinkNetworkHandler.Frequency.of(stack));
        super.setItem(slot, stack);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.freq.get(slot == 0).getStack();
    }

    @Override
    public ResourceLocation getId() {
        return Create.asResource("redstone_link");
    }

    @Override
    protected void writeAdditional(CompoundTag tag) {
        tag.put("Frequency", this.freq.serializeEach(f -> f.getStack().serializeNBT()));
    }

    public boolean lowActivation() {
        return this.intData("Inverted") == 1;
    }

    @Override
    protected void readAdditional(CompoundTag tag) {
        if (tag.contains("Frequency")) {
            this.freq = Couple.deserializeEach(tag.getList("Frequency", 10), c -> RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(c)));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(ModularGuiLineBuilder builder) {
        builder.addSelectionScrollInput(20, 101, (i, l) -> i.forOptions(Lang.translatedOptions("schedule.condition.redstone_link", "powered", "unpowered")).titled(Lang.translateDirect("schedule.condition.redstone_link.frequency_state")), "Inverted");
    }

    @Override
    public MutableComponent getWaitingStatus(Level level, Train train, CompoundTag tag) {
        return Lang.translateDirect("schedule.condition.redstone_link.status");
    }
}