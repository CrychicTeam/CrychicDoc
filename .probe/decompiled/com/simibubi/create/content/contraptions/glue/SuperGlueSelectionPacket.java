package com.simibubi.create.content.contraptions.glue;

import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkEvent;

public class SuperGlueSelectionPacket extends SimplePacketBase {

    private BlockPos from;

    private BlockPos to;

    public SuperGlueSelectionPacket(BlockPos from, BlockPos to) {
        this.from = from;
        this.to = to;
    }

    public SuperGlueSelectionPacket(FriendlyByteBuf buffer) {
        this.from = buffer.readBlockPos();
        this.to = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.from);
        buffer.writeBlockPos(this.to);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            double range = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue() + 2.0;
            if (!(player.m_20238_(Vec3.atCenterOf(this.to)) > range * range)) {
                if (this.to.m_123314_(this.from, 25.0)) {
                    Set<BlockPos> group = SuperGlueSelectionHelper.searchGlueGroup(player.m_9236_(), this.from, this.to, false);
                    if (group != null) {
                        if (group.contains(this.to)) {
                            if (SuperGlueSelectionHelper.collectGlueFromInventory(player, 1, true)) {
                                AABB bb = SuperGlueEntity.span(this.from, this.to);
                                SuperGlueSelectionHelper.collectGlueFromInventory(player, 1, false);
                                SuperGlueEntity entity = new SuperGlueEntity(player.m_9236_(), bb);
                                player.m_9236_().m_7967_(entity);
                                entity.spawnParticles();
                                AllAdvancements.SUPER_GLUE.awardTo(player);
                            }
                        }
                    }
                }
            }
        });
        return true;
    }
}