package com.simibubi.create.content.trains.schedule.condition;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class CargoThresholdCondition extends LazyTickedScheduleCondition {

    public CargoThresholdCondition() {
        super(20);
        this.data.putString("Threshold", "10");
    }

    @Override
    public boolean lazyTickCompletion(Level level, Train train, CompoundTag context) {
        int lastChecked = context.contains("LastChecked") ? context.getInt("LastChecked") : -1;
        int status = 0;
        for (Carriage carriage : train.carriages) {
            status += carriage.storage.getVersion();
        }
        if (status == lastChecked) {
            return false;
        } else {
            context.putInt("LastChecked", status);
            return this.test(level, train, context);
        }
    }

    protected void requestStatusToUpdate(int amount, CompoundTag context) {
        context.putInt("CurrentDisplay", amount);
        super.requestStatusToUpdate(context);
    }

    protected int getLastDisplaySnapshot(CompoundTag context) {
        return !context.contains("CurrentDisplay") ? -1 : context.getInt("CurrentDisplay");
    }

    protected abstract boolean test(Level var1, Train var2, CompoundTag var3);

    protected abstract Component getUnit();

    protected abstract ItemStack getIcon();

    @Override
    public Pair<ItemStack, Component> getSummary() {
        return Pair.of(this.getIcon(), Components.literal(this.getOperator().formatted + " " + this.getThreshold()).append(this.getUnit()));
    }

    @Override
    public int slotsTargeted() {
        return 1;
    }

    public CargoThresholdCondition.Ops getOperator() {
        return this.enumData("Operator", CargoThresholdCondition.Ops.class);
    }

    public int getThreshold() {
        try {
            return Integer.valueOf(this.textData("Threshold"));
        } catch (NumberFormatException var2) {
            this.data.putString("Threshold", "0");
            return 0;
        }
    }

    public int getMeasure() {
        return this.intData("Measure");
    }

    @Override
    public List<Component> getSecondLineTooltip(int slot) {
        return ImmutableList.of(Lang.translateDirect("schedule.condition.threshold.place_item"), Lang.translateDirect("schedule.condition.threshold.place_item_2").withStyle(ChatFormatting.GRAY), Lang.translateDirect("schedule.condition.threshold.place_item_3").withStyle(ChatFormatting.GRAY));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(ModularGuiLineBuilder builder) {
        builder.addSelectionScrollInput(0, 24, (i, l) -> i.forOptions(CargoThresholdCondition.Ops.translatedOptions()).titled(Lang.translateDirect("schedule.condition.threshold.train_holds", "")).format(state -> Components.literal(" " + CargoThresholdCondition.Ops.values()[state].formatted)), "Operator");
        builder.addIntegerTextInput(29, 41, (e, t) -> {
        }, "Threshold");
    }

    public static enum Ops {

        GREATER(">"), LESS("<"), EQUAL("=");

        public String formatted;

        private Ops(String formatted) {
            this.formatted = formatted;
        }

        public boolean test(int current, int target) {
            return switch(this) {
                case GREATER ->
                    current > target;
                case EQUAL ->
                    current == target;
                case LESS ->
                    current < target;
                default ->
                    throw new IllegalArgumentException("Unexpected value: " + this);
            };
        }

        public static List<? extends Component> translatedOptions() {
            return Arrays.stream(values()).map(op -> Lang.translateDirect("schedule.condition.threshold." + Lang.asId(op.name()))).toList();
        }
    }
}