package com.simibubi.create.content.trains.track;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class CurvedTrackDestroyPacket extends BlockEntityConfigurationPacket<TrackBlockEntity> {

    private BlockPos targetPos;

    private BlockPos soundSource;

    private boolean wrench;

    public CurvedTrackDestroyPacket(BlockPos pos, BlockPos targetPos, BlockPos soundSource, boolean wrench) {
        super(pos);
        this.targetPos = targetPos;
        this.soundSource = soundSource;
        this.wrench = wrench;
    }

    public CurvedTrackDestroyPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.targetPos);
        buffer.writeBlockPos(this.soundSource);
        buffer.writeBoolean(this.wrench);
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        this.targetPos = buffer.readBlockPos();
        this.soundSource = buffer.readBlockPos();
        this.wrench = buffer.readBoolean();
    }

    protected void applySettings(ServerPlayer player, TrackBlockEntity be) {
        int verifyDistance = AllConfigs.server().trains.maxTrackPlacementLength.get() * 4;
        if (!be.m_58899_().m_123314_(player.m_20183_(), (double) verifyDistance)) {
            Create.LOGGER.warn(player.m_6302_() + " too far away from destroyed Curve track");
        } else {
            Level level = be.m_58904_();
            BezierConnection bezierConnection = (BezierConnection) be.getConnections().get(this.targetPos);
            be.removeConnection(this.targetPos);
            if (level.getBlockEntity(this.targetPos) instanceof TrackBlockEntity other) {
                other.removeConnection(this.pos);
            }
            BlockState blockState = be.m_58900_();
            TrackPropagator.onRailRemoved(level, this.pos, blockState);
            if (this.wrench) {
                AllSoundEvents.WRENCH_REMOVE.playOnServer(player.m_9236_(), this.soundSource, 1.0F, Create.RANDOM.nextFloat() * 0.5F + 0.5F);
                if (!player.isCreative() && bezierConnection != null) {
                    bezierConnection.addItemsToPlayer(player);
                }
            } else if (!player.isCreative() && bezierConnection != null) {
                bezierConnection.spawnItems(level);
            }
            bezierConnection.spawnDestroyParticles(level);
            SoundType soundtype = blockState.getSoundType(level, this.pos, player);
            if (soundtype != null) {
                level.playSound(null, this.soundSource, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            }
        }
    }

    @Override
    protected int maxRange() {
        return 64;
    }

    protected void applySettings(TrackBlockEntity be) {
    }
}