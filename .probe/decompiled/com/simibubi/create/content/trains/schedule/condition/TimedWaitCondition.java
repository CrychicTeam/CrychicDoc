package com.simibubi.create.content.trains.schedule.condition;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class TimedWaitCondition extends ScheduleWaitCondition {

    protected void requestDisplayIfNecessary(CompoundTag context, int time) {
        int ticksUntilDeparture = this.totalWaitTicks() - time;
        if (ticksUntilDeparture < 1200 && ticksUntilDeparture % 100 == 0) {
            this.requestStatusToUpdate(context);
        }
        if (ticksUntilDeparture >= 1200 && ticksUntilDeparture % 1200 == 0) {
            this.requestStatusToUpdate(context);
        }
    }

    public int totalWaitTicks() {
        return this.getValue() * this.getUnit().ticksPer;
    }

    public TimedWaitCondition() {
        this.data.putInt("Value", 5);
        this.data.putInt("TimeUnit", TimedWaitCondition.TimeUnit.SECONDS.ordinal());
    }

    protected Component formatTime(boolean compact) {
        return compact ? Components.literal(this.getValue() + this.getUnit().suffix) : Components.literal(this.getValue() + " ").append(Lang.translateDirect(this.getUnit().key));
    }

    @Override
    public List<Component> getTitleAs(String type) {
        return ImmutableList.of(Components.translatable(this.getId().getNamespace() + ".schedule." + type + "." + this.getId().getPath()), Lang.translateDirect("schedule.condition.for_x_time", this.formatTime(false)).withStyle(ChatFormatting.DARK_AQUA));
    }

    @Override
    public ItemStack getSecondLineIcon() {
        return new ItemStack(Items.REPEATER);
    }

    @Override
    public List<Component> getSecondLineTooltip(int slot) {
        return ImmutableList.of(Lang.translateDirect("generic.duration"));
    }

    public int getValue() {
        return this.intData("Value");
    }

    public TimedWaitCondition.TimeUnit getUnit() {
        return this.enumData("TimeUnit", TimedWaitCondition.TimeUnit.class);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(ModularGuiLineBuilder builder) {
        builder.addScrollInput(0, 31, (i, l) -> {
            i.titled(Lang.translateDirect("generic.duration")).withShiftStep(15).withRange(0, 121);
            i.lockedTooltipX = -15;
            i.lockedTooltipY = 35;
        }, "Value");
        builder.addSelectionScrollInput(36, 85, (i, l) -> i.forOptions(TimedWaitCondition.TimeUnit.translatedOptions()).titled(Lang.translateDirect("generic.timeUnit")), "TimeUnit");
    }

    @Override
    public MutableComponent getWaitingStatus(Level level, Train train, CompoundTag tag) {
        int time = tag.getInt("Time");
        int ticksUntilDeparture = this.totalWaitTicks() - time;
        boolean showInMinutes = ticksUntilDeparture >= 1200;
        int num = (int) (showInMinutes ? Math.floor((double) ((float) ticksUntilDeparture / 1200.0F)) : Math.ceil((double) ((float) ticksUntilDeparture / 100.0F)) * 5.0);
        String key = "generic." + (showInMinutes ? (num == 1 ? "daytime.minute" : "unit.minutes") : (num == 1 ? "daytime.second" : "unit.seconds"));
        return Lang.translateDirect("schedule.condition." + this.getId().getPath() + ".status", Components.literal(num + " ").append(Lang.translateDirect(key)));
    }

    public static enum TimeUnit {

        TICKS(1, "t", "generic.unit.ticks"), SECONDS(20, "s", "generic.unit.seconds"), MINUTES(1200, "min", "generic.unit.minutes");

        public int ticksPer;

        public String suffix;

        public String key;

        private TimeUnit(int ticksPer, String suffix, String key) {
            this.ticksPer = ticksPer;
            this.suffix = suffix;
            this.key = key;
        }

        public static List<Component> translatedOptions() {
            return Lang.translatedOptions(null, TICKS.key, SECONDS.key, MINUTES.key);
        }
    }
}