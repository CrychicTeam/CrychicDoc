package com.simibubi.create.content.kinetics.transmission.sequencer;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

public class ConfigureSequencedGearshiftPacket extends BlockEntityConfigurationPacket<SequencedGearshiftBlockEntity> {

    private ListTag instructions;

    public ConfigureSequencedGearshiftPacket(BlockPos pos, ListTag instructions) {
        super(pos);
        this.instructions = instructions;
    }

    public ConfigureSequencedGearshiftPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        this.instructions = buffer.readNbt().getList("data", 10);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        tag.put("data", this.instructions);
        buffer.writeNbt(tag);
    }

    protected void applySettings(SequencedGearshiftBlockEntity be) {
        if (!be.computerBehaviour.hasAttachedComputer()) {
            be.run(-1);
            be.instructions = Instruction.deserializeAll(this.instructions);
            be.sendData();
        }
    }
}