package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.building.entity.Stool;
import org.violetmoon.quark.content.building.module.StoolsModule;
import org.violetmoon.zeta.block.OldMaterials;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class StoolBlock extends ZetaBlock implements SimpleWaterloggedBlock {

    private static final VoxelShape SHAPE_TOP = Block.box(0.0, 1.0, 0.0, 16.0, 9.0, 16.0);

    private static final VoxelShape SHAPE_LEG = Block.box(0.0, 0.0, 0.0, 4.0, 1.0, 4.0);

    private static final VoxelShape SHAPE_TOP_BIG = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_LEG_BIG = Block.box(0.0, 0.0, 0.0, 4.0, 8.0, 4.0);

    private static final VoxelShape SHAPE = Shapes.or(SHAPE_TOP, SHAPE_LEG, SHAPE_LEG.move(0.75, 0.0, 0.0), SHAPE_LEG.move(0.75, 0.0, 0.75), SHAPE_LEG.move(0.0, 0.0, 0.75));

    private static final VoxelShape SHAPE_BIG = Shapes.or(SHAPE_TOP_BIG, SHAPE_LEG_BIG, SHAPE_LEG_BIG.move(0.75, 0.0, 0.0), SHAPE_LEG_BIG.move(0.75, 0.0, 0.75), SHAPE_LEG_BIG.move(0.0, 0.0, 0.75));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty BIG = BooleanProperty.create("big");

    public static final BooleanProperty SAT_IN = BooleanProperty.create("sat_in");

    public StoolBlock(@Nullable ZetaModule module, DyeColor color) {
        super(color.getName() + "_stool", module, OldMaterials.wool().mapColor(color.getMapColor()).sound(SoundType.WOOD).strength(0.2F).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(BIG, false)).m_61124_(SAT_IN, false));
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.COLORED_BLOCKS, Blocks.PINK_BED, false);
        }
    }

    public void blockClicked(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!(Boolean) state.m_61143_(BIG)) {
            world.setBlockAndUpdate(pos, (BlockState) state.m_61124_(BIG, true));
            world.m_186460_(pos, this, 1);
        }
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        this.fixState(worldIn, pos, state);
    }

    @NotNull
    @Override
    public InteractionResult use(BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        if (!(Boolean) state.m_61143_(SAT_IN) && worldIn.getBlockState(pos.above()).m_60795_() && player.m_20202_() == null) {
            if (!worldIn.isClientSide) {
                Stool entity = new Stool(StoolsModule.stoolEntity, worldIn);
                entity.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.6, (double) pos.m_123343_() + 0.5);
                worldIn.m_7967_(entity);
                player.m_20329_(entity);
                worldIn.setBlockAndUpdate(pos, (BlockState) state.m_61124_(SAT_IN, true));
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        } else {
            return super.m_6227_(state, worldIn, pos, player, handIn, hit);
        }
    }

    @Override
    public void fallOn(@NotNull Level worldIn, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entityIn, float fallDistance) {
        super.m_142072_(worldIn, state, pos, entityIn, fallDistance * 0.5F);
    }

    @Override
    public void updateEntityAfterFallOn(@NotNull BlockGetter worldIn, Entity entityIn) {
        if (entityIn.isSuppressingBounce()) {
            super.m_5548_(worldIn, entityIn);
        } else {
            this.bounceEntity(entityIn);
        }
    }

    private void bounceEntity(Entity entity) {
        Vec3 vector3d = entity.getDeltaMovement();
        if (vector3d.y < 0.0) {
            double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vector3d.x, -vector3d.y * 0.66F * d0, vector3d.z);
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.m_61143_(BIG) ? SHAPE_BIG : SHAPE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return !(Boolean) state.m_61143_(WATERLOGGED);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getStateFor(context.m_43725_(), context.getClickedPos());
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
    public void neighborChanged(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        super.m_6861_(state, worldIn, pos, blockIn, fromPos, isMoving);
        this.fixState(worldIn, pos, state);
    }

    private void fixState(Level worldIn, BlockPos pos, BlockState state) {
        BlockState target = this.getStateFor(worldIn, pos);
        if (!target.equals(state)) {
            worldIn.setBlockAndUpdate(pos, target);
        }
    }

    private BlockState getStateFor(Level world, BlockPos pos) {
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER)).m_61124_(BIG, world.getBlockState(pos.above()).m_60808_(world, pos.above()).min(Direction.Axis.Y) == 0.0)).m_61124_(SAT_IN, world.m_6443_(Stool.class, new AABB(pos, pos.above()).inflate(0.4), e -> e.m_20183_().equals(pos)).size() > 0);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, @NotNull Level worldIn, @NotNull BlockPos pos) {
        return blockState.m_61143_(SAT_IN) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, BIG, SAT_IN);
    }
}