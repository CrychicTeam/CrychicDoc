package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.MiscUtil;

public class HollowWoodBlock extends HollowFrameBlock {

    protected static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    protected static final BooleanProperty UP = BlockStateProperties.UP;

    protected static final BooleanProperty NORTH = BlockStateProperties.NORTH;

    protected static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

    protected static final BooleanProperty WEST = BlockStateProperties.WEST;

    protected static final BooleanProperty EAST = BlockStateProperties.EAST;

    private final boolean flammable;

    public HollowWoodBlock(Block sourceLog, @Nullable ZetaModule module, boolean flammable) {
        this(Quark.ZETA.registryUtil.inherit(sourceLog, "hollow_%s"), sourceLog, module, flammable);
    }

    public HollowWoodBlock(String name, Block sourceLog, @Nullable ZetaModule module, boolean flammable) {
        super(name, module, MiscUtil.copyPropertySafe(sourceLog).isSuffocating((s, g, p) -> false));
        this.flammable = flammable;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(DOWN, true)).m_61124_(UP, true)).m_61124_(NORTH, true)).m_61124_(SOUTH, true)).m_61124_(WEST, true)).m_61124_(EAST, true));
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT_MIPPED);
        }
    }

    @Override
    public byte getShapeCode(BlockState state) {
        return shapeCode(state, DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        BlockState newState = state;
        for (Direction dir : Direction.values()) {
            newState = (BlockState) newState.m_61124_(MiscUtil.directionProperty(dir), (Boolean) state.m_61143_(MiscUtil.directionProperty(direction.rotate(dir))));
        }
        return newState;
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        BlockState newState = state;
        for (Direction dir : Direction.values()) {
            newState = (BlockState) newState.m_61124_(MiscUtil.directionProperty(dir), (Boolean) state.m_61143_(MiscUtil.directionProperty(mirror.mirror(dir))));
        }
        return newState;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedStateZeta(BlockState state, UseOnContext context, String toolActionType, boolean simulate) {
        if ("axe_strip".equals(toolActionType)) {
            Vec3 exactPos = context.getClickLocation();
            BlockPos centerPos = context.getClickedPos();
            Direction face = Direction.getNearest(exactPos.x - ((double) centerPos.m_123341_() + 0.5), exactPos.y - ((double) centerPos.m_123342_() + 0.5), exactPos.z - ((double) centerPos.m_123343_() + 0.5));
            return (BlockState) state.m_61122_(MiscUtil.directionProperty(face));
        } else {
            return super.getToolModifiedStateZeta(state, context, toolActionType, simulate);
        }
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> def) {
        super.createBlockStateDefinition(def);
        def.add(UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }

    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.flammable;
    }
}