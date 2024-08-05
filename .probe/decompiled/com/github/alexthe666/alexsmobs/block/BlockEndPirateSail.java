package com.github.alexthe666.alexsmobs.block;

import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockEndPirateSail extends Block {

    public static final BooleanProperty EASTORWEST = BooleanProperty.create("eastorwest");

    public static final EnumProperty<BlockEndPirateSail.SailType> SAIL = EnumProperty.create("sail", BlockEndPirateSail.SailType.class);

    protected static final VoxelShape EW_AABB = Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 16.0);

    protected static final VoxelShape NS_AABB = Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 9.0);

    public BlockEndPirateSail(boolean spectre) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).noOcclusion().emissiveRendering((a, b, c) -> true).sound(SoundType.WOOL).lightLevel(state -> 5).requiresCorrectToolForDrops().strength(0.4F));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(EASTORWEST, false)).m_61124_(SAIL, BlockEndPirateSail.SailType.SINGLE));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return blockState0.m_61143_(EASTORWEST) ? EW_AABB : NS_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(EASTORWEST, SAIL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader levelreader = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockPos actualPos = context.getClickedPos().relative(context.m_43719_().getOpposite());
        BlockPos u = blockpos.above();
        BlockPos d = blockpos.below();
        BlockState clickState = levelreader.m_8055_(actualPos);
        BlockState upState = levelreader.m_8055_(u);
        BlockState downState = levelreader.m_8055_(d);
        boolean axis = context.m_43719_().getAxis() == Direction.Axis.Y ? context.m_8125_().getAxis() == Direction.Axis.X : context.m_43719_().getAxis() != Direction.Axis.X;
        if (clickState.m_60734_() instanceof BlockEndPirateSail) {
            axis = (Boolean) clickState.m_61143_(EASTORWEST);
        }
        BlockState axisState = (BlockState) this.m_49966_().m_61124_(EASTORWEST, axis);
        return (BlockState) axisState.m_61124_(SAIL, getSailTypeFor(axisState, downState, upState));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelreader, BlockPos blockpos, BlockPos pos2) {
        BlockPos u = blockpos.above();
        BlockPos d = blockpos.below();
        BlockState upState = levelreader.m_8055_(u);
        BlockState downState = levelreader.m_8055_(d);
        return (BlockState) state.m_61124_(SAIL, getSailTypeFor(state, downState, upState));
    }

    private static BlockEndPirateSail.SailType getSailTypeFor(BlockState us, BlockState below, BlockState above) {
        if (below.m_60734_() instanceof BlockEndPirateSail && below.m_61143_(EASTORWEST) == us.m_61143_(EASTORWEST)) {
            return above.m_60734_() instanceof BlockEndPirateSail ? BlockEndPirateSail.SailType.MIDDLE : BlockEndPirateSail.SailType.TOP;
        } else {
            return above.m_60734_() instanceof BlockEndPirateSail && above.m_61143_(EASTORWEST) == us.m_61143_(EASTORWEST) ? BlockEndPirateSail.SailType.BOTTOM : BlockEndPirateSail.SailType.SINGLE;
        }
    }

    private static enum SailType implements StringRepresentable {

        SINGLE, TOP, MIDDLE, BOTTOM;

        public String toString() {
            return this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}