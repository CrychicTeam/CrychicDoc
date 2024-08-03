package net.mehvahdjukaar.supplementaries.common.block;

import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface IRopeConnection {

    default boolean shouldConnectToFace(BlockState thisState, BlockState facingState, BlockPos facingPos, Direction dir, LevelReader world) {
        if (!this.canSideAcceptConnection(thisState, dir)) {
            return false;
        } else {
            switch(dir) {
                case UP:
                    return isSupportingCeiling(facingState, facingPos, world);
                case DOWN:
                    return isSupportingCeiling(facingPos.above(2), world) || canConnectDown(facingState);
                default:
                    if ((Boolean) CommonConfigs.Functional.ROPE_UNRESTRICTED.get() && facingState.m_60783_(world, facingPos, dir.getOpposite())) {
                        return true;
                    } else {
                        if (facingState.m_60734_() instanceof IRopeConnection otherRope) {
                            return otherRope.canSideAcceptConnection(facingState, dir.getOpposite());
                        }
                        return false;
                    }
            }
        }
    }

    boolean canSideAcceptConnection(BlockState var1, Direction var2);

    static boolean isSupportingCeiling(BlockPos pos, LevelReader world) {
        return isSupportingCeiling(world.m_8055_(pos), pos, world);
    }

    static boolean canConnectDown(BlockState downState) {
        Block b = downState.m_60734_();
        return b instanceof IRopeConnection ropeConnection ? ropeConnection.canSideAcceptConnection(downState, Direction.UP) : downState.m_204336_(ModTags.ROPE_HANG_TAG) || downState.m_61138_(FaceAttachedHorizontalDirectionalBlock.FACE) && downState.m_61143_(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.CEILING || b instanceof ChainBlock && downState.m_61143_(BlockStateProperties.AXIS) == Direction.Axis.Y || downState.m_61138_(BlockStateProperties.HANGING) && (Boolean) downState.m_61143_(BlockStateProperties.HANGING);
    }

    static boolean isSupportingCeiling(BlockState upState, BlockPos pos, LevelReader world) {
        return upState.m_60734_() instanceof IRopeConnection ropeConnection ? ropeConnection.canSideAcceptConnection(upState, Direction.DOWN) : Block.canSupportCenter(world, pos, Direction.DOWN) || upState.m_204336_(ModTags.ROPE_SUPPORT_TAG);
    }
}