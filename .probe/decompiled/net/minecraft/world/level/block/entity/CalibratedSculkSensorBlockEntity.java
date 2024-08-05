package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;

public class CalibratedSculkSensorBlockEntity extends SculkSensorBlockEntity {

    public CalibratedSculkSensorBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.CALIBRATED_SCULK_SENSOR, blockPos0, blockState1);
    }

    @Override
    public VibrationSystem.User createVibrationUser() {
        return new CalibratedSculkSensorBlockEntity.VibrationUser(this.m_58899_());
    }

    protected class VibrationUser extends SculkSensorBlockEntity.VibrationUser {

        public VibrationUser(BlockPos blockPos0) {
            super(blockPos0);
        }

        @Override
        public int getListenerRadius() {
            return 16;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel serverLevel0, BlockPos blockPos1, GameEvent gameEvent2, @Nullable GameEvent.Context gameEventContext3) {
            int $$4 = this.getBackSignal(serverLevel0, this.f_279654_, CalibratedSculkSensorBlockEntity.this.m_58900_());
            return $$4 != 0 && VibrationSystem.getGameEventFrequency(gameEvent2) != $$4 ? false : super.canReceiveVibration(serverLevel0, blockPos1, gameEvent2, gameEventContext3);
        }

        private int getBackSignal(Level level0, BlockPos blockPos1, BlockState blockState2) {
            Direction $$3 = ((Direction) blockState2.m_61143_(CalibratedSculkSensorBlock.FACING)).getOpposite();
            return level0.m_277185_(blockPos1.relative($$3), $$3);
        }
    }
}