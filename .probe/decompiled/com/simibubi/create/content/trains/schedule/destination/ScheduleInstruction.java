package com.simibubi.create.content.trains.schedule.destination;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.content.trains.schedule.ScheduleDataEntry;
import com.simibubi.create.foundation.utility.Pair;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public abstract class ScheduleInstruction extends ScheduleDataEntry {

    public abstract boolean supportsConditions();

    public final CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        CompoundTag dataCopy = this.data.copy();
        this.writeAdditional(dataCopy);
        tag.putString("Id", this.getId().toString());
        tag.put("Data", dataCopy);
        return tag;
    }

    public static ScheduleInstruction fromTag(CompoundTag tag) {
        ResourceLocation location = new ResourceLocation(tag.getString("Id"));
        Supplier<? extends ScheduleInstruction> supplier = null;
        for (Pair<ResourceLocation, Supplier<? extends ScheduleInstruction>> pair : Schedule.INSTRUCTION_TYPES) {
            if (pair.getFirst().equals(location)) {
                supplier = pair.getSecond();
            }
        }
        if (supplier == null) {
            Create.LOGGER.warn("Could not parse schedule instruction type: " + location);
            return new DestinationInstruction();
        } else {
            ScheduleInstruction scheduleDestination = (ScheduleInstruction) supplier.get();
            scheduleDestination.readAdditional(tag);
            CompoundTag data = tag.getCompound("Data");
            scheduleDestination.readAdditional(data);
            scheduleDestination.data = data;
            return scheduleDestination;
        }
    }
}