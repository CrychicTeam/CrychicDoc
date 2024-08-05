package com.simibubi.create.content.kinetics.simpleRelays;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SimpleKineticBlockEntity extends KineticBlockEntity {

    public SimpleKineticBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).inflate(1.0);
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (!ICogWheel.isLargeCog(state)) {
            return super.addPropagationLocations(block, state, neighbours);
        } else {
            BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).forEach(offset -> {
                if (offset.m_123331_(BlockPos.ZERO) == 2.0) {
                    neighbours.add(this.f_58858_.offset(offset));
                }
            });
            return neighbours;
        }
    }

    @Override
    protected boolean isNoisy() {
        return false;
    }
}