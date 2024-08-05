package com.simibubi.create.content.schematics.packet;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class InstantSchematicPacket extends SimplePacketBase {

    private String name;

    private BlockPos origin;

    private BlockPos bounds;

    public InstantSchematicPacket(String name, BlockPos origin, BlockPos bounds) {
        this.name = name;
        this.origin = origin;
        this.bounds = bounds;
    }

    public InstantSchematicPacket(FriendlyByteBuf buffer) {
        this.name = buffer.readUtf(32767);
        this.origin = buffer.readBlockPos();
        this.bounds = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.name);
        buffer.writeBlockPos(this.origin);
        buffer.writeBlockPos(this.bounds);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Create.SCHEMATIC_RECEIVER.handleInstantSchematic(player, this.name, player.m_9236_(), this.origin, this.bounds);
            }
        });
        return true;
    }
}