package com.github.alexmodguy.alexscaves.server.block.blockentity;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EnigmaticEngineBlockEntity extends BlockEntity {

    private int checkTime;

    public EnigmaticEngineBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntityRegistry.ENIGMATIC_ENGINE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, EnigmaticEngineBlockEntity entity) {
        if (entity.checkTime-- <= 0) {
            entity.checkTime = 30 + level.random.nextInt(30);
            entity.attemptAssembly();
        }
    }

    public boolean attemptAssembly() {
        Direction assembleIn = null;
        for (Direction direction : ACMath.HORIZONTAL_DIRECTIONS) {
            if (this.isAssembledInDirection(direction)) {
                assembleIn = direction;
                break;
            }
        }
        if (assembleIn == null) {
            return false;
        } else {
            for (BlockPos pos : BlockPos.betweenClosed(this.m_58899_().m_123341_() - 1, this.m_58899_().m_123342_() - 1, this.m_58899_().m_123343_() - 1, this.m_58899_().m_123341_() + 1, this.m_58899_().m_123342_() + 1, this.m_58899_().m_123343_() + 1)) {
                if (this.f_58857_.getBlockState(pos).m_60713_(ACBlockRegistry.DEPTH_GLASS.get()) || this.f_58857_.getBlockState(pos).m_204336_(ACTagRegistry.SUBMARINE_ASSEMBLY_BLOCKS) || this.f_58857_.getBlockState(pos).m_60713_(ACBlockRegistry.ENIGMATIC_ENGINE.get())) {
                    this.f_58857_.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
            if (!this.f_58857_.isClientSide) {
                SubmarineEntity submarine = ACEntityRegistry.SUBMARINE.get().create(this.f_58857_);
                Vec3 vec31 = Vec3.atCenterOf(this.m_58899_()).add(0.0, -1.0, 0.0);
                submarine.m_146922_(assembleIn.toYRot());
                submarine.m_6034_(vec31.x, vec31.y, vec31.z);
                submarine.setOxidizationLevel(0);
                this.f_58857_.m_7967_(submarine);
            }
            return true;
        }
    }

    private boolean isAssembledInDirection(Direction direction) {
        List<BlockPos> windowPos = new ArrayList();
        for (int i = -1; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                BlockPos at = this.m_58899_().relative(direction).relative(direction.getClockWise(), i).above(j);
                if (!this.f_58857_.getBlockState(at).m_60713_(ACBlockRegistry.DEPTH_GLASS.get())) {
                    return false;
                }
                windowPos.add(at);
            }
        }
        if (windowPos.size() == 6) {
            for (BlockPos pos : BlockPos.betweenClosed(this.m_58899_().m_123341_() - 1, this.m_58899_().m_123342_() - 1, this.m_58899_().m_123343_() - 1, this.m_58899_().m_123341_() + 1, this.m_58899_().m_123342_() + 1, this.m_58899_().m_123343_() + 1)) {
                if (!windowPos.contains(pos) && !pos.equals(this.m_58899_()) && !this.f_58857_.getBlockState(pos).m_204336_(ACTagRegistry.SUBMARINE_ASSEMBLY_BLOCKS)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}