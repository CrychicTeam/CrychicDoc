package com.simibubi.create.content.trains.schedule;

import com.simibubi.create.content.trains.schedule.condition.ScheduleWaitCondition;
import com.simibubi.create.content.trains.schedule.destination.ScheduleInstruction;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class ScheduleEntry {

    public ScheduleInstruction instruction;

    public List<List<ScheduleWaitCondition>> conditions = new ArrayList();

    public ScheduleEntry clone() {
        return fromTag(this.write());
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        ListTag outer = new ListTag();
        tag.put("Instruction", this.instruction.write());
        if (!this.instruction.supportsConditions()) {
            return tag;
        } else {
            for (List<ScheduleWaitCondition> column : this.conditions) {
                outer.add(NBTHelper.writeCompoundList(column, ScheduleWaitCondition::write));
            }
            tag.put("Conditions", outer);
            return tag;
        }
    }

    public static ScheduleEntry fromTag(CompoundTag tag) {
        ScheduleEntry entry = new ScheduleEntry();
        entry.instruction = ScheduleInstruction.fromTag(tag.getCompound("Instruction"));
        entry.conditions = new ArrayList();
        if (entry.instruction.supportsConditions()) {
            for (Tag t : tag.getList("Conditions", 9)) {
                if (t instanceof ListTag list) {
                    entry.conditions.add(NBTHelper.readCompoundList(list, ScheduleWaitCondition::fromTag));
                }
            }
        }
        return entry;
    }
}