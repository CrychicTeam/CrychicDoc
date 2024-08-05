package com.simibubi.create.content.trains.station;

import com.simibubi.create.content.decoration.slidingDoor.DoorControl;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class StationEditPacket extends BlockEntityConfigurationPacket<StationBlockEntity> {

    boolean dropSchedule;

    boolean assemblyMode;

    Boolean tryAssemble;

    DoorControl doorControl;

    String name;

    public static StationEditPacket dropSchedule(BlockPos pos) {
        StationEditPacket packet = new StationEditPacket(pos);
        packet.dropSchedule = true;
        return packet;
    }

    public static StationEditPacket tryAssemble(BlockPos pos) {
        StationEditPacket packet = new StationEditPacket(pos);
        packet.tryAssemble = true;
        return packet;
    }

    public static StationEditPacket tryDisassemble(BlockPos pos) {
        StationEditPacket packet = new StationEditPacket(pos);
        packet.tryAssemble = false;
        return packet;
    }

    public static StationEditPacket configure(BlockPos pos, boolean assemble, String name, DoorControl doorControl) {
        StationEditPacket packet = new StationEditPacket(pos);
        packet.assemblyMode = assemble;
        packet.tryAssemble = null;
        packet.name = name;
        packet.doorControl = doorControl;
        return packet;
    }

    public StationEditPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public StationEditPacket(BlockPos pos) {
        super(pos);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.dropSchedule);
        if (!this.dropSchedule) {
            buffer.writeBoolean(this.doorControl != null);
            if (this.doorControl != null) {
                buffer.writeVarInt(this.doorControl.ordinal());
            }
            buffer.writeBoolean(this.tryAssemble != null);
            if (this.tryAssemble != null) {
                buffer.writeBoolean(this.tryAssemble);
            } else {
                buffer.writeBoolean(this.assemblyMode);
                buffer.writeUtf(this.name);
            }
        }
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            this.dropSchedule = true;
        } else {
            if (buffer.readBoolean()) {
                this.doorControl = DoorControl.values()[Mth.clamp(buffer.readVarInt(), 0, DoorControl.values().length)];
            }
            this.name = "";
            if (buffer.readBoolean()) {
                this.tryAssemble = buffer.readBoolean();
            } else {
                this.assemblyMode = buffer.readBoolean();
                this.name = buffer.readUtf(256);
            }
        }
    }

    protected void applySettings(ServerPlayer player, StationBlockEntity be) {
        Level level = be.m_58904_();
        BlockPos blockPos = be.m_58899_();
        BlockState blockState = level.getBlockState(blockPos);
        if (this.dropSchedule) {
            be.dropSchedule(player);
        } else {
            if (this.doorControl != null) {
                be.doorControls.set(this.doorControl);
            }
            if (!this.name.isBlank()) {
                be.updateName(this.name);
            }
            if (blockState.m_60734_() instanceof StationBlock) {
                Boolean isAssemblyMode = (Boolean) blockState.m_61143_(StationBlock.ASSEMBLING);
                boolean assemblyComplete = false;
                if (this.tryAssemble != null) {
                    if (!isAssemblyMode) {
                        return;
                    }
                    if (!this.tryAssemble) {
                        if (be.tryDisassembleTrain(player) && be.tryEnterAssemblyMode()) {
                            be.refreshAssemblyInfo();
                        }
                    } else {
                        be.assemble(player.m_20148_());
                        assemblyComplete = be.getStation() != null && be.getStation().getPresentTrain() != null;
                    }
                    if (!assemblyComplete) {
                        return;
                    }
                }
                if (this.assemblyMode) {
                    be.enterAssemblyMode(player);
                } else {
                    be.exitAssemblyMode();
                }
            }
        }
    }

    protected void applySettings(StationBlockEntity be) {
    }
}