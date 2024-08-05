package com.simibubi.create.content.contraptions.elevator;

import com.simibubi.create.content.decoration.slidingDoor.DoorControl;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

public class ElevatorContactEditPacket extends BlockEntityConfigurationPacket<ElevatorContactBlockEntity> {

    private String shortName;

    private String longName;

    private DoorControl doorControl;

    public ElevatorContactEditPacket(BlockPos pos, String shortName, String longName, DoorControl doorControl) {
        super(pos);
        this.shortName = shortName;
        this.longName = longName;
        this.doorControl = doorControl;
    }

    public ElevatorContactEditPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.shortName, 4);
        buffer.writeUtf(this.longName, 30);
        buffer.writeVarInt(this.doorControl.ordinal());
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        this.shortName = buffer.readUtf(4);
        this.longName = buffer.readUtf(30);
        this.doorControl = DoorControl.values()[Mth.clamp(buffer.readVarInt(), 0, DoorControl.values().length)];
    }

    protected void applySettings(ElevatorContactBlockEntity be) {
        be.updateName(this.shortName, this.longName);
        be.doorControls.set(this.doorControl);
    }
}