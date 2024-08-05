package com.simibubi.create.content.contraptions.glue;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class SuperGlueRemovalPacket extends SimplePacketBase {

    private int entityId;

    private BlockPos soundSource;

    public SuperGlueRemovalPacket(int id, BlockPos soundSource) {
        this.entityId = id;
        this.soundSource = soundSource;
    }

    public SuperGlueRemovalPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.soundSource = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBlockPos(this.soundSource);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Entity entity = player.m_9236_().getEntity(this.entityId);
            if (entity instanceof SuperGlueEntity superGlue) {
                double range = 32.0;
                if (!(player.m_20238_(superGlue.m_20182_()) > range * range)) {
                    AllSoundEvents.SLIME_ADDED.play(player.m_9236_(), null, this.soundSource, 0.5F, 0.5F);
                    superGlue.spawnParticles();
                    entity.discard();
                }
            }
        });
        return true;
    }
}