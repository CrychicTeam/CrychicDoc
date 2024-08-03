package com.simibubi.create.content.trains.schedule;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.schedule.condition.FluidThresholdCondition;
import com.simibubi.create.content.trains.schedule.condition.IdleCargoCondition;
import com.simibubi.create.content.trains.schedule.condition.ItemThresholdCondition;
import com.simibubi.create.content.trains.schedule.condition.PlayerPassengerCondition;
import com.simibubi.create.content.trains.schedule.condition.RedstoneLinkCondition;
import com.simibubi.create.content.trains.schedule.condition.ScheduleWaitCondition;
import com.simibubi.create.content.trains.schedule.condition.ScheduledDelay;
import com.simibubi.create.content.trains.schedule.condition.StationPoweredCondition;
import com.simibubi.create.content.trains.schedule.condition.StationUnloadedCondition;
import com.simibubi.create.content.trains.schedule.condition.TimeOfDayCondition;
import com.simibubi.create.content.trains.schedule.destination.ChangeThrottleInstruction;
import com.simibubi.create.content.trains.schedule.destination.ChangeTitleInstruction;
import com.simibubi.create.content.trains.schedule.destination.DestinationInstruction;
import com.simibubi.create.content.trains.schedule.destination.ScheduleInstruction;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Schedule {

    public static List<Pair<ResourceLocation, Supplier<? extends ScheduleInstruction>>> INSTRUCTION_TYPES = new ArrayList();

    public static List<Pair<ResourceLocation, Supplier<? extends ScheduleWaitCondition>>> CONDITION_TYPES = new ArrayList();

    public List<ScheduleEntry> entries = new ArrayList();

    public boolean cyclic = true;

    public int savedProgress = 0;

    private static void registerInstruction(String name, Supplier<? extends ScheduleInstruction> factory) {
        INSTRUCTION_TYPES.add(Pair.of(Create.asResource(name), factory));
    }

    private static void registerCondition(String name, Supplier<? extends ScheduleWaitCondition> factory) {
        CONDITION_TYPES.add(Pair.of(Create.asResource(name), factory));
    }

    public static <T> List<? extends Component> getTypeOptions(List<Pair<ResourceLocation, T>> list) {
        String langSection = list.equals(INSTRUCTION_TYPES) ? "instruction." : "condition.";
        return list.stream().map(Pair::getFirst).map(rl -> rl.getNamespace() + ".schedule." + langSection + rl.getPath()).map(Components::translatable).toList();
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        ListTag list = NBTHelper.writeCompoundList(this.entries, ScheduleEntry::write);
        tag.put("Entries", list);
        tag.putBoolean("Cyclic", this.cyclic);
        if (this.savedProgress > 0) {
            tag.putInt("Progress", this.savedProgress);
        }
        return tag;
    }

    public static Schedule fromTag(CompoundTag tag) {
        Schedule schedule = new Schedule();
        schedule.entries = NBTHelper.readCompoundList(tag.getList("Entries", 10), ScheduleEntry::fromTag);
        schedule.cyclic = tag.getBoolean("Cyclic");
        if (tag.contains("Progress")) {
            schedule.savedProgress = tag.getInt("Progress");
        }
        return schedule;
    }

    static {
        registerInstruction("destination", DestinationInstruction::new);
        registerInstruction("rename", ChangeTitleInstruction::new);
        registerInstruction("throttle", ChangeThrottleInstruction::new);
        registerCondition("delay", ScheduledDelay::new);
        registerCondition("time_of_day", TimeOfDayCondition::new);
        registerCondition("fluid_threshold", FluidThresholdCondition::new);
        registerCondition("item_threshold", ItemThresholdCondition::new);
        registerCondition("redstone_link", RedstoneLinkCondition::new);
        registerCondition("player_count", PlayerPassengerCondition::new);
        registerCondition("idle", IdleCargoCondition::new);
        registerCondition("unloaded", StationUnloadedCondition::new);
        registerCondition("powered", StationPoweredCondition::new);
    }
}