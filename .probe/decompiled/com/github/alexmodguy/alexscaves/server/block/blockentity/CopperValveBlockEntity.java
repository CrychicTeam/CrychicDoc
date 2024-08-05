package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.server.block.CopperValveBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CopperValveBlockEntity extends BlockEntity {

    private boolean movingDown;

    private float downProgress;

    private float prevDownProgress;

    public CopperValveBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.COPPER_VALVE.get(), pos, state);
        if ((Boolean) state.m_61143_(CopperValveBlock.TURNED)) {
            this.movingDown = true;
            this.prevDownProgress = this.downProgress = 10.0F;
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, CopperValveBlockEntity entity) {
        entity.prevDownProgress = entity.downProgress;
        if (entity.movingDown && entity.downProgress < 10.0F) {
            if (entity.downProgress == 0.0F) {
                level.playLocalSound((double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_() + 0.5, (double) blockPos.m_123343_() + 0.5, ACSoundRegistry.COPPER_VALVE_CREAK_ON.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F, false);
            }
            entity.downProgress += 0.5F;
        } else if (!entity.movingDown && entity.downProgress > 0.0F) {
            if (entity.downProgress == 10.0F) {
                level.playLocalSound((double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_() + 0.5, (double) blockPos.m_123343_() + 0.5, ACSoundRegistry.COPPER_VALVE_CREAK_OFF.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F, false);
            }
            entity.downProgress -= 0.5F;
        }
        if (entity.movingDown && entity.downProgress >= 10.0F && !(Boolean) state.m_61143_(CopperValveBlock.TURNED)) {
            level.setBlockAndUpdate(blockPos, (BlockState) state.m_61124_(CopperValveBlock.TURNED, true));
            level.updateNeighborsAt(blockPos, state.m_60734_());
            level.updateNeighborsAt(blockPos.relative(((Direction) state.m_61143_(CopperValveBlock.FACING)).getOpposite()), state.m_60734_());
        }
        if ((!entity.movingDown || entity.downProgress < 10.0F) && (Boolean) state.m_61143_(CopperValveBlock.TURNED)) {
            level.setBlockAndUpdate(blockPos, (BlockState) state.m_61124_(CopperValveBlock.TURNED, false));
            level.updateNeighborsAt(blockPos, state.m_60734_());
            level.updateNeighborsAt(blockPos.relative(((Direction) state.m_61143_(CopperValveBlock.FACING)).getOpposite()), state.m_60734_());
        }
    }

    public void moveDown(boolean in) {
        this.movingDown = in;
    }

    public boolean isMovingDown() {
        return this.movingDown;
    }

    public float getDownAmount(float partialTicks) {
        return (this.prevDownProgress + (this.downProgress - this.prevDownProgress) * partialTicks) * 0.1F;
    }
}