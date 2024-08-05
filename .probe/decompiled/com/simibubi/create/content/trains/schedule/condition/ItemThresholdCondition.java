package com.simibubi.create.content.trains.schedule.condition;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
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
import net.minecraftforge.items.IItemHandlerModifiable;

public class ItemThresholdCondition extends CargoThresholdCondition {

    private FilterItemStack stack = FilterItemStack.empty();

    @Override
    protected Component getUnit() {
        return Components.literal(this.inStacks() ? "▤" : "");
    }

    @Override
    protected ItemStack getIcon() {
        return this.stack.item();
    }

    @Override
    protected boolean test(Level level, Train train, CompoundTag context) {
        CargoThresholdCondition.Ops operator = this.getOperator();
        int target = this.getThreshold();
        boolean stacks = this.inStacks();
        int foundItems = 0;
        for (Carriage carriage : train.carriages) {
            IItemHandlerModifiable items = carriage.storage.getItems();
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack stackInSlot = items.getStackInSlot(i);
                if (this.stack.test(level, stackInSlot)) {
                    if (stacks) {
                        foundItems += stackInSlot.getCount() == stackInSlot.getMaxStackSize() ? 1 : 0;
                    } else {
                        foundItems += stackInSlot.getCount();
                    }
                }
            }
        }
        this.requestStatusToUpdate(foundItems, context);
        return operator.test(foundItems, target);
    }

    @Override
    protected void writeAdditional(CompoundTag tag) {
        super.writeAdditional(tag);
        tag.put("Item", this.stack.serializeNBT());
    }

    @Override
    protected void readAdditional(CompoundTag tag) {
        super.readAdditional(tag);
        if (tag.contains("Item")) {
            this.stack = FilterItemStack.of(tag.getCompound("Item"));
        }
    }

    @Override
    public boolean tickCompletion(Level level, Train train, CompoundTag context) {
        return super.tickCompletion(level, train, context);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.stack = FilterItemStack.of(stack);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.stack.item();
    }

    @Override
    public List<Component> getTitleAs(String type) {
        return ImmutableList.of(Lang.translateDirect("schedule.condition.threshold.train_holds", Lang.translateDirect("schedule.condition.threshold." + Lang.asId(this.getOperator().name()))), Lang.translateDirect("schedule.condition.threshold.x_units_of_item", this.getThreshold(), Lang.translateDirect("schedule.condition.threshold." + (this.inStacks() ? "stacks" : "items")), this.stack.isEmpty() ? Lang.translateDirect("schedule.condition.threshold.anything") : (this.stack.isFilterItem() ? Lang.translateDirect("schedule.condition.threshold.matching_content") : this.stack.item().getHoverName())).withStyle(ChatFormatting.DARK_AQUA));
    }

    private boolean inStacks() {
        return this.intData("Measure") == 1;
    }

    @Override
    public ResourceLocation getId() {
        return Create.asResource("item_threshold");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(ModularGuiLineBuilder builder) {
        super.initConfigurationWidgets(builder);
        builder.addSelectionScrollInput(71, 50, (i, l) -> i.forOptions(ImmutableList.of(Lang.translateDirect("schedule.condition.threshold.items"), Lang.translateDirect("schedule.condition.threshold.stacks"))).titled(Lang.translateDirect("schedule.condition.threshold.item_measure")), "Measure");
    }

    @Override
    public MutableComponent getWaitingStatus(Level level, Train train, CompoundTag tag) {
        int lastDisplaySnapshot = this.getLastDisplaySnapshot(tag);
        if (lastDisplaySnapshot == -1) {
            return Components.empty();
        } else {
            int offset = this.getOperator() == CargoThresholdCondition.Ops.LESS ? -1 : (this.getOperator() == CargoThresholdCondition.Ops.GREATER ? 1 : 0);
            return Lang.translateDirect("schedule.condition.threshold.status", lastDisplaySnapshot, Math.max(0, this.getThreshold() + offset), Lang.translateDirect("schedule.condition.threshold." + (this.inStacks() ? "stacks" : "items")));
        }
    }
}