package com.simibubi.create.content.trains.schedule.condition;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableObject;

public class TimeOfDayCondition extends ScheduleWaitCondition {

    public TimeOfDayCondition() {
        this.data.putInt("Hour", 8);
        this.data.putInt("Rotation", 5);
    }

    @Override
    public boolean tickCompletion(Level level, Train train, CompoundTag context) {
        int maxTickDiff = 40;
        int targetHour = this.intData("Hour");
        int targetMinute = this.intData("Minute");
        int dayTime = (int) (level.getDayTime() % (long) this.getRotation());
        int targetTicks = (int) (((double) ((targetHour + 18) % 24 * 1000) + Math.ceil((double) ((float) targetMinute / 60.0F * 1000.0F))) % (double) this.getRotation());
        int diff = dayTime - targetTicks;
        return diff >= 0 && maxTickDiff >= diff;
    }

    public int getRotation() {
        int index = this.intData("Rotation");
        return switch(index) {
            case 1 ->
                12000;
            case 2 ->
                6000;
            case 3 ->
                4000;
            case 4 ->
                3000;
            case 5 ->
                2000;
            case 6 ->
                1000;
            case 7 ->
                750;
            case 8 ->
                500;
            case 9 ->
                250;
            default ->
                24000;
        };
    }

    @Override
    public Pair<ItemStack, Component> getSummary() {
        return Pair.of(new ItemStack(Items.STRUCTURE_VOID), this.getDigitalDisplay(this.intData("Hour"), this.intData("Minute"), false));
    }

    public MutableComponent getDigitalDisplay(int hour, int minute, boolean doubleDigitHrs) {
        int hour12raw = hour % 12 == 0 ? 12 : hour % 12;
        String hr12 = doubleDigitHrs ? this.twoDigits(hour12raw) : hour12raw + "";
        String hr24 = doubleDigitHrs ? this.twoDigits(hour) : hour + "";
        return Lang.translateDirect("schedule.condition.time_of_day.digital_format", hr12, hr24, this.twoDigits(minute), hour > 11 ? Lang.translateDirect("generic.daytime.pm") : Lang.translateDirect("generic.daytime.am"));
    }

    @Override
    public List<Component> getTitleAs(String type) {
        return ImmutableList.of(Lang.translateDirect("schedule.condition.time_of_day.scheduled"), this.getDigitalDisplay(this.intData("Hour"), this.intData("Minute"), false).withStyle(ChatFormatting.DARK_AQUA).append(Components.literal(" -> ").withStyle(ChatFormatting.DARK_GRAY)).append(((Component) Lang.translatedOptions("schedule.condition.time_of_day.rotation", "every_24", "every_12", "every_6", "every_4", "every_3", "every_2", "every_1", "every_0_45", "every_0_30", "every_0_15").get(this.intData("Rotation"))).copy().withStyle(ChatFormatting.GRAY)));
    }

    public String twoDigits(int t) {
        return t < 10 ? "0" + t : t + "";
    }

    @Override
    public ResourceLocation getId() {
        return Create.asResource("time_of_day");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean renderSpecialIcon(GuiGraphics graphics, int x, int y) {
        int displayHr = (this.intData("Hour") + 12) % 24;
        float progress = ((float) displayHr * 60.0F + (float) this.intData("Minute")) / 1440.0F;
        ResourceLocation location = new ResourceLocation("textures/item/clock_" + this.twoDigits(Mth.clamp((int) (progress * 64.0F), 0, 63)) + ".png");
        graphics.blit(location, x, y, 0, 0.0F, 0.0F, 16, 16, 16, 16);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(ModularGuiLineBuilder builder) {
        MutableObject<ScrollInput> minuteInput = new MutableObject();
        MutableObject<ScrollInput> hourInput = new MutableObject();
        MutableObject<Label> timeLabel = new MutableObject();
        builder.addScrollInput(0, 16, (i, l) -> {
            i.withRange(0, 24);
            timeLabel.setValue(l);
            hourInput.setValue(i);
        }, "Hour");
        builder.addScrollInput(18, 16, (i, l) -> {
            i.withRange(0, 60);
            minuteInput.setValue(i);
            l.f_93624_ = false;
        }, "Minute");
        builder.addSelectionScrollInput(52, 62, (i, l) -> i.forOptions(Lang.translatedOptions("schedule.condition.time_of_day.rotation", "every_24", "every_12", "every_6", "every_4", "every_3", "every_2", "every_1", "every_0_45", "every_0_30", "every_0_15")).titled(Lang.translateDirect("schedule.condition.time_of_day.rotation")), "Rotation");
        ((ScrollInput) hourInput.getValue()).titled(Lang.translateDirect("generic.daytime.hour")).calling(t -> {
            this.data.putInt("Hour", t);
            ((Label) timeLabel.getValue()).text = this.getDigitalDisplay(t, ((ScrollInput) minuteInput.getValue()).getState(), true);
        }).writingTo(null).withShiftStep(6);
        ((ScrollInput) minuteInput.getValue()).titled(Lang.translateDirect("generic.daytime.minute")).calling(t -> {
            this.data.putInt("Minute", t);
            ((Label) timeLabel.getValue()).text = this.getDigitalDisplay(((ScrollInput) hourInput.getValue()).getState(), t, true);
        }).writingTo(null).withShiftStep(15);
        ((ScrollInput) minuteInput.getValue()).lockedTooltipX = ((ScrollInput) hourInput.getValue()).lockedTooltipX = -15;
        ((ScrollInput) minuteInput.getValue()).lockedTooltipY = ((ScrollInput) hourInput.getValue()).lockedTooltipY = 35;
        ((ScrollInput) hourInput.getValue()).setState(this.intData("Hour"));
        ((ScrollInput) minuteInput.getValue()).setState(this.intData("Minute")).onChanged();
        builder.customArea(0, 52);
        builder.customArea(52, 69);
    }

    @Override
    public MutableComponent getWaitingStatus(Level level, Train train, CompoundTag tag) {
        int targetHour = this.intData("Hour");
        int targetMinute = this.intData("Minute");
        int dayTime = (int) (level.getDayTime() % (long) this.getRotation());
        int targetTicks = (int) (((double) ((targetHour + 18) % 24 * 1000) + Math.ceil((double) ((float) targetMinute / 60.0F * 1000.0F))) % (double) this.getRotation());
        int diff = targetTicks - dayTime;
        if (diff < 0) {
            diff += this.getRotation();
        }
        int departureTime = (int) (level.getDayTime() + (long) diff) % 24000;
        int departingHour = (departureTime / 1000 + 6) % 24;
        int departingMinute = departureTime % 1000 * 60 / 1000;
        return Lang.translateDirect("schedule.condition.time_of_day.status").append(this.getDigitalDisplay(departingHour, departingMinute, false));
    }
}