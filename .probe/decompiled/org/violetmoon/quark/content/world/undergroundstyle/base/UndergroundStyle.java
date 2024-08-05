package org.violetmoon.quark.content.world.undergroundstyle.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.util.MiscUtil;

public abstract class UndergroundStyle {

    private static final TagKey<Block> UNDERGROUND_BIOME_REPLACEABLE = BlockTags.create(new ResourceLocation("quark", "underground_biome_replaceable"));

    public boolean canReplace(BlockState state) {
        return state.m_247087_() || state.m_204336_(UNDERGROUND_BIOME_REPLACEABLE);
    }

    public final void fill(UndergroundStyleGenerator.Context context, BlockPos pos) {
        LevelAccessor world = context.world;
        BlockState state = world.m_8055_(pos);
        if (state.m_60800_(world, pos) != -1.0F) {
            if (this.isFloor(world, pos, state)) {
                this.fillFloor(context, pos, state);
            } else if (this.isCeiling(world, pos, state)) {
                this.fillCeiling(context, pos, state);
            } else if (this.isWall(world, pos, state)) {
                this.fillWall(context, pos, state);
            } else if (this.isInside(state)) {
                this.fillInside(context, pos, state);
            }
        }
    }

    public abstract void fillFloor(UndergroundStyleGenerator.Context var1, BlockPos var2, BlockState var3);

    public abstract void fillCeiling(UndergroundStyleGenerator.Context var1, BlockPos var2, BlockState var3);

    public abstract void fillWall(UndergroundStyleGenerator.Context var1, BlockPos var2, BlockState var3);

    public abstract void fillInside(UndergroundStyleGenerator.Context var1, BlockPos var2, BlockState var3);

    public boolean isFloor(LevelAccessor world, BlockPos pos, BlockState state) {
        if (state.m_60804_(world, pos) && this.canReplace(state)) {
            BlockPos upPos = pos.above();
            return world.m_46859_(upPos) || world.m_8055_(upPos).m_247087_();
        } else {
            return false;
        }
    }

    public boolean isCeiling(LevelAccessor world, BlockPos pos, BlockState state) {
        if (state.m_60804_(world, pos) && this.canReplace(state)) {
            BlockPos downPos = pos.below();
            return world.m_46859_(downPos) || world.m_8055_(downPos).m_247087_();
        } else {
            return false;
        }
    }

    public boolean isWall(LevelAccessor world, BlockPos pos, BlockState state) {
        return state.m_60804_(world, pos) && this.canReplace(state) ? this.isBorder(world, pos) : false;
    }

    public Direction getBorderSide(LevelAccessor world, BlockPos pos) {
        BlockState state = world.m_8055_(pos);
        for (Direction facing : MiscUtil.HORIZONTALS) {
            BlockPos offsetPos = pos.relative(facing);
            BlockState stateAt = world.m_8055_(offsetPos);
            if (state != stateAt && world.m_46859_(offsetPos) || stateAt.m_247087_()) {
                return facing;
            }
        }
        return null;
    }

    public boolean isBorder(LevelAccessor world, BlockPos pos) {
        return this.getBorderSide(world, pos) != null;
    }

    public boolean isInside(BlockState state) {
        return state.m_204336_(UNDERGROUND_BIOME_REPLACEABLE);
    }
}