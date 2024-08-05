package com.simibubi.create.content.contraptions.actors.trainControls;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ControlsInputPacket extends SimplePacketBase {

    private Collection<Integer> activatedButtons;

    private boolean press;

    private int contraptionEntityId;

    private BlockPos controlsPos;

    private boolean stopControlling;

    public ControlsInputPacket(Collection<Integer> activatedButtons, boolean press, int contraptionEntityId, BlockPos controlsPos, boolean stopControlling) {
        this.contraptionEntityId = contraptionEntityId;
        this.activatedButtons = activatedButtons;
        this.press = press;
        this.controlsPos = controlsPos;
        this.stopControlling = stopControlling;
    }

    public ControlsInputPacket(FriendlyByteBuf buffer) {
        this.contraptionEntityId = buffer.readInt();
        this.activatedButtons = new ArrayList();
        this.press = buffer.readBoolean();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            this.activatedButtons.add(buffer.readVarInt());
        }
        this.controlsPos = buffer.readBlockPos();
        this.stopControlling = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.contraptionEntityId);
        buffer.writeBoolean(this.press);
        buffer.writeVarInt(this.activatedButtons.size());
        this.activatedButtons.forEach(buffer::m_130130_);
        buffer.writeBlockPos(this.controlsPos);
        buffer.writeBoolean(this.stopControlling);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level world = player.m_20193_();
            UUID uniqueID = player.m_20148_();
            if (!player.isSpectator() || !this.press) {
                if (world.getEntity(this.contraptionEntityId) instanceof AbstractContraptionEntity ace) {
                    if (this.stopControlling) {
                        ace.stopControlling(this.controlsPos);
                    } else {
                        if (ace.toGlobalVector(Vec3.atCenterOf(this.controlsPos), 0.0F).closerThan(player.m_20182_(), 16.0)) {
                            ControlsServerHandler.receivePressed(world, ace, this.controlsPos, uniqueID, this.activatedButtons, this.press);
                        }
                    }
                }
            }
        });
        return true;
    }
}