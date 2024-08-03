package net.minecraft.world.level.block.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.slf4j.Logger;

public class SculkSensorBlockEntity extends BlockEntity implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem {

    private static final Logger LOGGER = LogUtils.getLogger();

    private VibrationSystem.Data vibrationData;

    private final VibrationSystem.Listener vibrationListener;

    private final VibrationSystem.User vibrationUser = this.createVibrationUser();

    private int lastVibrationFrequency;

    protected SculkSensorBlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        super(blockEntityType0, blockPos1, blockState2);
        this.vibrationData = new VibrationSystem.Data();
        this.vibrationListener = new VibrationSystem.Listener(this);
    }

    public SculkSensorBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        this(BlockEntityType.SCULK_SENSOR, blockPos0, blockState1);
    }

    public VibrationSystem.User createVibrationUser() {
        return new SculkSensorBlockEntity.VibrationUser(this.m_58899_());
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.lastVibrationFrequency = compoundTag0.getInt("last_vibration_frequency");
        if (compoundTag0.contains("listener", 10)) {
            VibrationSystem.Data.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag0.getCompound("listener"))).resultOrPartial(LOGGER::error).ifPresent(p_281146_ -> this.vibrationData = p_281146_);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putInt("last_vibration_frequency", this.lastVibrationFrequency);
        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(LOGGER::error).ifPresent(p_222820_ -> compoundTag0.put("listener", p_222820_));
    }

    @Override
    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    public int getLastVibrationFrequency() {
        return this.lastVibrationFrequency;
    }

    public void setLastVibrationFrequency(int int0) {
        this.lastVibrationFrequency = int0;
    }

    public VibrationSystem.Listener getListener() {
        return this.vibrationListener;
    }

    protected class VibrationUser implements VibrationSystem.User {

        public static final int LISTENER_RANGE = 8;

        protected final BlockPos blockPos;

        private final PositionSource positionSource;

        public VibrationUser(BlockPos blockPos0) {
            this.blockPos = blockPos0;
            this.positionSource = new BlockPositionSource(blockPos0);
        }

        @Override
        public int getListenerRadius() {
            return 8;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canTriggerAvoidVibration() {
            return true;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel serverLevel0, BlockPos blockPos1, GameEvent gameEvent2, @Nullable GameEvent.Context gameEventContext3) {
            return !blockPos1.equals(this.blockPos) || gameEvent2 != GameEvent.BLOCK_DESTROY && gameEvent2 != GameEvent.BLOCK_PLACE ? SculkSensorBlock.canActivate(SculkSensorBlockEntity.this.m_58900_()) : false;
        }

        @Override
        public void onReceiveVibration(ServerLevel serverLevel0, BlockPos blockPos1, GameEvent gameEvent2, @Nullable Entity entity3, @Nullable Entity entity4, float float5) {
            BlockState $$6 = SculkSensorBlockEntity.this.m_58900_();
            if (SculkSensorBlock.canActivate($$6)) {
                SculkSensorBlockEntity.this.setLastVibrationFrequency(VibrationSystem.getGameEventFrequency(gameEvent2));
                int $$7 = VibrationSystem.getRedstoneStrengthForDistance(float5, this.getListenerRadius());
                if ($$6.m_60734_() instanceof SculkSensorBlock $$8) {
                    $$8.activate(entity3, serverLevel0, this.blockPos, $$6, $$7, SculkSensorBlockEntity.this.getLastVibrationFrequency());
                }
            }
        }

        @Override
        public void onDataChanged() {
            SculkSensorBlockEntity.this.m_6596_();
        }

        @Override
        public boolean requiresAdjacentChunksToBeTicking() {
            return true;
        }
    }
}