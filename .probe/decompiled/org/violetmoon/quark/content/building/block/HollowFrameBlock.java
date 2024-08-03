package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.api.ICrawlSpaceBlock;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public abstract class HollowFrameBlock extends ZetaBlock implements SimpleWaterloggedBlock, ICrawlSpaceBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHELL = Shapes.join(Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0), Block.box(2.0, 2.0, 2.0, 14.0, 14.0, 14.0), BooleanOp.ONLY_FIRST);

    private static final VoxelShape SHAPE_BOTTOM = Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0);

    private static final VoxelShape SHAPE_TOP = Block.box(2.0, 14.0, 2.0, 14.0, 16.0, 14.0);

    private static final VoxelShape SHAPE_NORTH = Block.box(2.0, 2.0, 0.0, 14.0, 14.0, 2.0);

    private static final VoxelShape SHAPE_SOUTH = Block.box(2.0, 2.0, 14.0, 14.0, 14.0, 16.0);

    private static final VoxelShape SHAPE_WEST = Block.box(0.0, 2.0, 2.0, 2.0, 14.0, 14.0);

    private static final VoxelShape SHAPE_EAST = Block.box(14.0, 2.0, 2.0, 16.0, 14.0, 14.0);

    private static final byte FLAG_BOTTOM = 1;

    private static final byte FLAG_TOP = 2;

    private static final byte FLAG_NORTH = 4;

    private static final byte FLAG_SOUTH = 8;

    private static final byte FLAG_WEST = 16;

    private static final byte FLAG_EAST = 32;

    private static final VoxelShape[] SHAPES = new VoxelShape[64];

    public HollowFrameBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    public static boolean bottom(byte shapeCode) {
        return (shapeCode & 1) != 0;
    }

    public static boolean top(byte shapeCode) {
        return (shapeCode & 2) != 0;
    }

    public static boolean north(byte shapeCode) {
        return (shapeCode & 4) != 0;
    }

    public static boolean south(byte shapeCode) {
        return (shapeCode & 8) != 0;
    }

    public static boolean west(byte shapeCode) {
        return (shapeCode & 16) != 0;
    }

    public static boolean east(byte shapeCode) {
        return (shapeCode & 32) != 0;
    }

    public static boolean hasDirection(byte shapeCode, Direction direction) {
        return switch(direction) {
            case DOWN ->
                bottom(shapeCode);
            case UP ->
                top(shapeCode);
            case NORTH ->
                north(shapeCode);
            case SOUTH ->
                south(shapeCode);
            case WEST ->
                west(shapeCode);
            case EAST ->
                east(shapeCode);
        };
    }

    public boolean hasDirection(BlockState state, Direction direction) {
        return hasDirection(this.getShapeCode(state), direction);
    }

    protected static byte shapeCode(boolean bottom, boolean top, boolean north, boolean south, boolean west, boolean east) {
        byte flag = 0;
        if (bottom) {
            flag = (byte) (flag | 1);
        }
        if (top) {
            flag = (byte) (flag | 2);
        }
        if (north) {
            flag = (byte) (flag | 4);
        }
        if (south) {
            flag = (byte) (flag | 8);
        }
        if (west) {
            flag = (byte) (flag | 16);
        }
        if (east) {
            flag = (byte) (flag | 32);
        }
        return flag;
    }

    protected static byte shapeCode(BlockState state, BooleanProperty bottom, BooleanProperty top, BooleanProperty north, BooleanProperty south, BooleanProperty west, BooleanProperty east) {
        return shapeCode((Boolean) state.m_61143_(bottom), (Boolean) state.m_61143_(top), (Boolean) state.m_61143_(north), (Boolean) state.m_61143_(south), (Boolean) state.m_61143_(west), (Boolean) state.m_61143_(east));
    }

    public abstract byte getShapeCode(BlockState var1);

    @Override
    public boolean canCrawl(Level level, BlockState state, BlockPos pos, Direction direction) {
        return this.hasDirection(state, direction.getOpposite());
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return SHAPES[this.getShapeCode(state)];
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        byte code = this.getShapeCode(state);
        if (ctx.isDescending()) {
            code &= -3;
            if (ctx instanceof EntityCollisionContext eCtx && eCtx.getEntity() instanceof LivingEntity living && living.m_20186_() >= (double) pos.m_123342_() + SHAPE_BOTTOM.max(Direction.Axis.Y)) {
                code &= -2;
            }
        }
        return SHAPES[code];
    }

    @Override
    public boolean isLadderZeta(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        if (entity.m_20143_() && entity.m_6144_()) {
            return false;
        } else {
            byte shapeCode = this.getShapeCode(state);
            if (!bottom(shapeCode) && !top(shapeCode)) {
                return false;
            } else {
                Vec3 eyePos = entity.m_146892_();
                double pad = 0.125;
                return eyePos.x > (double) pos.m_123341_() + pad && eyePos.z > (double) pos.m_123343_() + pad && eyePos.x < (double) (pos.m_123341_() + 1) - pad && eyePos.z < (double) (pos.m_123343_() + 1) - pad ? true : super.isLadderZeta(state, level, pos, entity);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, ctx.m_43725_().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        byte shapeCode = this.getShapeCode(state);
        return !(Boolean) state.m_61143_(WATERLOGGED) && bottom(shapeCode) && top(shapeCode);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return super.m_7417_(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState blockState0) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> def) {
        super.m_7926_(def);
        def.add(WATERLOGGED);
    }

    static {
        for (byte shapeCode = 0; shapeCode < 64; shapeCode++) {
            VoxelShape shape = SHELL;
            if (bottom(shapeCode)) {
                shape = Shapes.join(shape, SHAPE_BOTTOM, BooleanOp.ONLY_FIRST);
            }
            if (top(shapeCode)) {
                shape = Shapes.join(shape, SHAPE_TOP, BooleanOp.ONLY_FIRST);
            }
            if (north(shapeCode)) {
                shape = Shapes.join(shape, SHAPE_NORTH, BooleanOp.ONLY_FIRST);
            }
            if (south(shapeCode)) {
                shape = Shapes.join(shape, SHAPE_SOUTH, BooleanOp.ONLY_FIRST);
            }
            if (west(shapeCode)) {
                shape = Shapes.join(shape, SHAPE_WEST, BooleanOp.ONLY_FIRST);
            }
            if (east(shapeCode)) {
                shape = Shapes.join(shape, SHAPE_EAST, BooleanOp.ONLY_FIRST);
            }
            SHAPES[shapeCode] = shape;
        }
    }
}