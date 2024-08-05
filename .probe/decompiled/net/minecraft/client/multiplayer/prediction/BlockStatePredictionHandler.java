package net.minecraft.client.multiplayer.prediction;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockStatePredictionHandler implements AutoCloseable {

    private final Long2ObjectOpenHashMap<BlockStatePredictionHandler.ServerVerifiedState> serverVerifiedStates = new Long2ObjectOpenHashMap();

    private int currentSequenceNr;

    private boolean isPredicting;

    public void retainKnownServerState(BlockPos blockPos0, BlockState blockState1, LocalPlayer localPlayer2) {
        this.serverVerifiedStates.compute(blockPos0.asLong(), (p_289242_, p_289243_) -> p_289243_ != null ? p_289243_.setSequence(this.currentSequenceNr) : new BlockStatePredictionHandler.ServerVerifiedState(this.currentSequenceNr, blockState1, localPlayer2.m_20182_()));
    }

    public boolean updateKnownServerState(BlockPos blockPos0, BlockState blockState1) {
        BlockStatePredictionHandler.ServerVerifiedState $$2 = (BlockStatePredictionHandler.ServerVerifiedState) this.serverVerifiedStates.get(blockPos0.asLong());
        if ($$2 == null) {
            return false;
        } else {
            $$2.setBlockState(blockState1);
            return true;
        }
    }

    public void endPredictionsUpTo(int int0, ClientLevel clientLevel1) {
        ObjectIterator<Entry<BlockStatePredictionHandler.ServerVerifiedState>> $$2 = this.serverVerifiedStates.long2ObjectEntrySet().iterator();
        while ($$2.hasNext()) {
            Entry<BlockStatePredictionHandler.ServerVerifiedState> $$3 = (Entry<BlockStatePredictionHandler.ServerVerifiedState>) $$2.next();
            BlockStatePredictionHandler.ServerVerifiedState $$4 = (BlockStatePredictionHandler.ServerVerifiedState) $$3.getValue();
            if ($$4.sequence <= int0) {
                BlockPos $$5 = BlockPos.of($$3.getLongKey());
                $$2.remove();
                clientLevel1.syncBlockState($$5, $$4.blockState, $$4.playerPos);
            }
        }
    }

    public BlockStatePredictionHandler startPredicting() {
        this.currentSequenceNr++;
        this.isPredicting = true;
        return this;
    }

    public void close() {
        this.isPredicting = false;
    }

    public int currentSequence() {
        return this.currentSequenceNr;
    }

    public boolean isPredicting() {
        return this.isPredicting;
    }

    static class ServerVerifiedState {

        final Vec3 playerPos;

        int sequence;

        BlockState blockState;

        ServerVerifiedState(int int0, BlockState blockState1, Vec3 vec2) {
            this.sequence = int0;
            this.blockState = blockState1;
            this.playerPos = vec2;
        }

        BlockStatePredictionHandler.ServerVerifiedState setSequence(int int0) {
            this.sequence = int0;
            return this;
        }

        void setBlockState(BlockState blockState0) {
            this.blockState = blockState0;
        }
    }
}