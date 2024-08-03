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
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidThresholdCondition extends CargoThresholdCondition {

    private FilterItemStack compareStack = FilterItemStack.empty();

    @Override
    protected Component getUnit() {
        return Components.literal("b");
    }

    @Override
    protected ItemStack getIcon() {
        return this.compareStack.item();
    }

    @Override
    protected boolean test(Level level, Train train, CompoundTag context) {
        CargoThresholdCondition.Ops operator = this.getOperator();
        int target = this.getThreshold();
        int foundFluid = 0;
        for (Carriage carriage : train.carriages) {
            IFluidHandler fluids = carriage.storage.getFluids();
            for (int i = 0; i < fluids.getTanks(); i++) {
                FluidStack fluidInTank = fluids.getFluidInTank(i);
                if (this.compareStack.test(level, fluidInTank)) {
                    foundFluid += fluidInTank.getAmount();
                }
            }
        }
        this.requestStatusToUpdate(foundFluid / 1000, context);
        return operator.test(foundFluid, target * 1000);
    }

    @Override
    protected void writeAdditional(CompoundTag tag) {
        super.writeAdditional(tag);
        tag.put("Bucket", this.compareStack.serializeNBT());
    }

    @Override
    protected void readAdditional(CompoundTag tag) {
        super.readAdditional(tag);
        if (tag.contains("Bucket")) {
            this.compareStack = FilterItemStack.of(tag.getCompound("Bucket"));
        }
    }

    @Override
    public boolean tickCompletion(Level level, Train train, CompoundTag context) {
        return super.tickCompletion(level, train, context);
    }

    @OnlyIn(Dist.CLIENT)
    private FluidStack loadFluid() {
        return this.compareStack.fluid(Minecraft.getInstance().level);
    }

    @Override
    public List<Component> getTitleAs(String type) {
        return ImmutableList.of(Lang.translateDirect("schedule.condition.threshold.train_holds", Lang.translateDirect("schedule.condition.threshold." + Lang.asId(this.getOperator().name()))), Lang.translateDirect("schedule.condition.threshold.x_units_of_item", this.getThreshold(), Lang.translateDirect("schedule.condition.threshold.buckets"), this.compareStack.isEmpty() ? Lang.translateDirect("schedule.condition.threshold.anything") : (this.compareStack.isFilterItem() ? Lang.translateDirect("schedule.condition.threshold.matching_content") : this.loadFluid().getDisplayName())).withStyle(ChatFormatting.DARK_AQUA));
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.compareStack = FilterItemStack.of(stack);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.compareStack.item();
    }

    @Override
    public ResourceLocation getId() {
        return Create.asResource("fluid_threshold");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(ModularGuiLineBuilder builder) {
        super.initConfigurationWidgets(builder);
        builder.addSelectionScrollInput(71, 50, (i, l) -> i.forOptions(ImmutableList.of(Lang.translateDirect("schedule.condition.threshold.buckets"))).titled(null), "Measure");
    }

    @Override
    public MutableComponent getWaitingStatus(Level level, Train train, CompoundTag tag) {
        int lastDisplaySnapshot = this.getLastDisplaySnapshot(tag);
        if (lastDisplaySnapshot == -1) {
            return Components.empty();
        } else {
            int offset = this.getOperator() == CargoThresholdCondition.Ops.LESS ? -1 : (this.getOperator() == CargoThresholdCondition.Ops.GREATER ? 1 : 0);
            return Lang.translateDirect("schedule.condition.threshold.status", lastDisplaySnapshot, Math.max(0, this.getThreshold() + offset), Lang.translateDirect("schedule.condition.threshold.buckets"));
        }
    }
}