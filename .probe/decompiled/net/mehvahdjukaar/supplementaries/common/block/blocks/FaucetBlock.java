package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FaucetBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.FluidsUtil;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FaucetBlock extends WaterBlock implements EntityBlock {

    protected static final VoxelShape SHAPE_NORTH = Block.box(5.0, 5.0, 5.0, 11.0, 15.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.SOUTH);

    protected static final VoxelShape SHAPE_WEST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.WEST);

    protected static final VoxelShape SHAPE_EAST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.EAST);

    protected static final VoxelShape SHAPE_NORTH_JAR = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH_JAR = MthUtils.rotateVoxelShape(SHAPE_NORTH_JAR, Direction.SOUTH);

    protected static final VoxelShape SHAPE_WEST_JAR = MthUtils.rotateVoxelShape(SHAPE_NORTH_JAR, Direction.WEST);

    protected static final VoxelShape SHAPE_EAST_JAR = MthUtils.rotateVoxelShape(SHAPE_NORTH_JAR, Direction.EAST);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty HAS_WATER = ModBlockProperties.HAS_WATER;

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL_0_7;

    public static final BooleanProperty CONNECTED = ModBlockProperties.CONNECTED;

    public FaucetBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(s -> (Integer) s.m_61143_(LIGHT_LEVEL)));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(CONNECTED, false)).m_61124_(FACING, Direction.NORTH)).m_61124_(ENABLED, false)).m_61124_(POWERED, false)).m_61124_(HAS_WATER, false)).m_61124_(WATERLOGGED, false)).m_61124_(LIGHT_LEVEL, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if ((Boolean) state.m_61143_(CONNECTED)) {
            return switch((Direction) state.m_61143_(FACING)) {
                case SOUTH ->
                    SHAPE_SOUTH_JAR;
                case EAST ->
                    SHAPE_EAST_JAR;
                case WEST ->
                    SHAPE_WEST_JAR;
                default ->
                    SHAPE_NORTH_JAR;
            };
        } else {
            return switch((Direction) state.m_61143_(FACING)) {
                case SOUTH ->
                    SHAPE_SOUTH;
                case EAST ->
                    SHAPE_EAST;
                case WEST ->
                    SHAPE_WEST;
                default ->
                    SHAPE_NORTH;
            };
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        boolean enabled = (Boolean) state.m_61143_(ENABLED);
        float f = enabled ? 1.0F : 1.2F;
        worldIn.playSound(null, pos, (SoundEvent) ModSounds.FAUCET.get(), SoundSource.BLOCKS, 1.0F, f);
        worldIn.m_142346_(player, enabled ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        this.updateBlock(state, worldIn, pos, true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        boolean hasWater = this.updateTileFluid(state, pos, worldIn);
        if (hasWater != (Boolean) state.m_61143_(HAS_WATER)) {
            worldIn.setBlockAndUpdate(pos, (BlockState) state.m_61124_(HAS_WATER, hasWater));
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        if (facing == Direction.DOWN) {
            boolean canConnectDown = this.canConnect(facingState, worldIn, facingPos, facing.getOpposite());
            return (BlockState) stateIn.m_61124_(CONNECTED, canConnectDown);
        } else if (facing == ((Direction) stateIn.m_61143_(FACING)).getOpposite()) {
            boolean hasWater = this.updateTileFluid(stateIn, currentPos, worldIn);
            return (BlockState) stateIn.m_61124_(HAS_WATER, hasWater);
        } else {
            return stateIn;
        }
    }

    public boolean updateTileFluid(BlockState state, BlockPos pos, LevelAccessor world) {
        if (world.m_7702_(pos) instanceof FaucetBlockTile tile && world instanceof Level level) {
            return tile.updateContainedFluidVisuals(level, pos, state);
        }
        return false;
    }

    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        if (world.m_7702_(pos) instanceof FaucetBlockTile tile && world instanceof Level level) {
            boolean water = tile.updateContainedFluidVisuals(level, pos, state);
            if ((Boolean) state.m_61143_(HAS_WATER) != water) {
                level.setBlock(pos, (BlockState) state.m_61124_(HAS_WATER, water), 2);
            }
        }
    }

    private boolean canConnect(BlockState downState, LevelAccessor world, BlockPos pos, Direction dir) {
        if (downState.m_60734_() instanceof JarBlock) {
            return true;
        } else if (downState.m_204336_(ModTags.FAUCET_CONNECTION_BLACKLIST)) {
            return false;
        } else if (downState.m_204336_(ModTags.FAUCET_CONNECTION_WHITELIST)) {
            return false;
        } else if (downState.m_61138_(BlockStateProperties.LEVEL_HONEY)) {
            return true;
        } else {
            if (world instanceof Level level && FluidsUtil.hasFluidHandler(level, pos, dir)) {
                return true;
            }
            return false;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.m_6861_(state, world, pos, neighborBlock, fromPos, moving);
        this.updateBlock(state, world, pos, false);
    }

    public void updateBlock(BlockState state, Level world, BlockPos pos, boolean toggle) {
        boolean isPowered = world.m_276867_(pos);
        if (isPowered != (Boolean) state.m_61143_(POWERED) || toggle) {
            world.setBlock(pos, (BlockState) ((BlockState) state.m_61124_(POWERED, isPowered)).m_61124_(ENABLED, toggle ^ (Boolean) state.m_61143_(ENABLED)), 2);
        }
        boolean hasWater = this.updateTileFluid(state, pos, world);
        if (hasWater != (Boolean) state.m_61143_(HAS_WATER)) {
            world.setBlockAndUpdate(pos, (BlockState) state.m_61124_(HAS_WATER, hasWater));
        }
    }

    public boolean isOpen(BlockState state) {
        return (Boolean) state.m_61143_(BlockStateProperties.POWERED) ^ (Boolean) state.m_61143_(BlockStateProperties.ENABLED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED, POWERED, HAS_WATER, CONNECTED, WATERLOGGED, LIGHT_LEVEL);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        Direction dir = context.m_43719_().getAxis() == Direction.Axis.Y ? Direction.NORTH : context.m_43719_();
        boolean water = world.getFluidState(pos).getType() == Fluids.WATER;
        boolean hasJar = this.canConnect(world.getBlockState(pos.below()), world, pos.below(), Direction.UP);
        boolean powered = world.m_276867_(pos);
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, dir)).m_61124_(CONNECTED, hasJar)).m_61124_(WATERLOGGED, water)).m_61124_(POWERED, powered);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        boolean flag = this.isOpen(state);
        if ((Boolean) state.m_61143_(HAS_WATER) && !(Boolean) state.m_61143_(CONNECTED)) {
            if ((double) random.nextFloat() > (flag ? 0.0 : 0.06)) {
                return;
            }
            float d = 0.125F;
            double x = (double) pos.m_123341_() + 0.5 + (double) d * ((double) random.nextFloat() - 0.5);
            double y = (double) pos.m_123342_() + 0.25;
            double z = (double) pos.m_123343_() + 0.5 + (double) d * ((double) random.nextFloat() - 0.5);
            int color = this.getTileParticleColor(pos, world);
            float r = (float) FastColor.ARGB32.red(color) / 255.0F;
            float g = (float) FastColor.ARGB32.green(color) / 255.0F;
            float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
            world.addParticle((ParticleOptions) ModParticles.DRIPPING_LIQUID.get(), x, y, z, (double) r, (double) g, (double) b);
        }
    }

    public int getTileParticleColor(BlockPos pos, Level world) {
        return world.getBlockEntity(pos) instanceof FaucetBlockTile te ? te.tempFluidHolder.getCachedParticleColor(world, pos) : 4340983;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FaucetBlockTile(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, (BlockEntityType) ModRegistry.FAUCET_TILE.get(), pLevel.isClientSide ? null : FaucetBlockTile::tick);
    }
}