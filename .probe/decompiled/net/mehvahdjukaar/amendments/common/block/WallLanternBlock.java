package net.mehvahdjukaar.amendments.common.block;

import java.util.List;
import net.mehvahdjukaar.amendments.common.tile.SwayingBlockTile;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.amendments.integration.SuppSquaredCompat;
import net.mehvahdjukaar.amendments.integration.ThinAirCompat;
import net.mehvahdjukaar.amendments.reg.ModBlockProperties;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.IBlockHolder;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WallLanternBlock extends WaterBlock implements EntityBlock {

    public static final VoxelShape SHAPE_NORTH = Block.box(5.0, 2.0, 6.0, 11.0, 15.99, 16.0);

    public static final VoxelShape SHAPE_SOUTH = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.SOUTH);

    public static final VoxelShape SHAPE_WEST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.WEST);

    public static final VoxelShape SHAPE_EAST = MthUtils.rotateVoxelShape(SHAPE_NORTH, Direction.EAST);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final EnumProperty<ModBlockProperties.BlockAttachment> ATTACHMENT = ModBlockProperties.BLOCK_ATTACHMENT;

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL;

    public WallLanternBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(s -> s.m_61143_(LIT) ? (Integer) s.m_61143_(LIGHT_LEVEL) : 0));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(LIGHT_LEVEL, 15)).m_61124_(WATERLOGGED, false)).m_61124_(LIT, true));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof WallLanternBlockTile te && te.isAccessibleBy(pPlayer)) {
            BlockState lantern = te.getHeldBlock();
            if (CompatHandler.SUPPSQUARED) {
                InteractionResult res = SuppSquaredCompat.lightUpLantern(pLevel, pPos, pPlayer, pHand, te, lantern);
                if (res != null) {
                    return res;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.m_43719_().getAxis() == Direction.Axis.Y) {
            return null;
        } else {
            BlockState state = super.getStateForPlacement(context);
            BlockPos blockpos = context.getClickedPos();
            Level world = context.m_43725_();
            Direction dir = context.m_43719_();
            BlockPos relative = blockpos.relative(dir.getOpposite());
            BlockState facingState = world.getBlockState(relative);
            return (BlockState) getConnectedState(state, facingState, world, relative, dir).m_61124_(FACING, context.m_43719_());
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        BlockEntity te = world.getBlockEntity(pos);
        Item i = stack.getItem();
        if (te instanceof WallLanternBlockTile blockHolder && i instanceof BlockItem blockItem) {
            blockHolder.setHeldBlock(blockItem.getBlock().defaultBlockState());
        }
        if (CompatHandler.SUPPLEMENTARIES) {
            SuppCompat.addOptionalOwnership(world, pos, entity);
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        return facing == ((Direction) stateIn.m_61143_(FACING)).getOpposite() ? (!stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : getConnectedState(stateIn, facingState, worldIn, facingPos, facing)) : stateIn;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        BlockState blockstate = level.m_8055_(blockpos);
        return ModBlockProperties.BlockAttachment.get(blockstate, blockpos, level, direction) != null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    public static BlockState getConnectedState(BlockState state, BlockState facingState, LevelAccessor world, BlockPos pos, Direction dir) {
        ModBlockProperties.BlockAttachment attachment = ModBlockProperties.BlockAttachment.get(facingState, pos, world, dir);
        return attachment == null ? state : (BlockState) state.m_61124_(ATTACHMENT, attachment);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case NORTH ->
                SHAPE_NORTH;
            case WEST ->
                SHAPE_WEST;
            case EAST ->
                SHAPE_EAST;
            default ->
                SHAPE_SOUTH;
        };
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return level.getBlockEntity(pos) instanceof WallLanternBlockTile te ? new ItemStack(te.getHeldBlock().m_60734_()) : new ItemStack(Blocks.LANTERN, 1);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIGHT_LEVEL, LIT, FACING, ATTACHMENT);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        super.m_213897_(state, level, pos, rand);
        if (level.m_7702_(pos) instanceof WallLanternBlockTile te) {
            if (te.isRedstoneLantern() && (Boolean) state.m_61143_(LIT) && !level.m_276867_(pos)) {
                level.m_7731_(pos, (BlockState) state.m_61122_(LIT), 2);
                if (te.getHeldBlock().m_61138_(LIT)) {
                    te.setHeldBlock((BlockState) te.getHeldBlock().m_61122_(LIT));
                }
            }
            if (CompatHandler.THIN_AIR) {
                BlockState lantern = te.getHeldBlock();
                if (ThinAirCompat.isAirLantern(lantern)) {
                    te.setHeldBlock(lantern);
                    if (te.getHeldBlock() != lantern) {
                        level.sendBlockUpdated(pos, state, state, 3);
                    }
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof WallLanternBlockTile tile && tile.isRedstoneLantern()) {
            boolean flag = (Boolean) state.m_61143_(LIT);
            if (flag != world.m_276867_(pos)) {
                if (flag) {
                    world.m_186460_(pos, this, 4);
                } else {
                    world.setBlock(pos, (BlockState) state.m_61122_(LIT), 2);
                    if (tile.getHeldBlock().m_61138_(LIT)) {
                        tile.setHeldBlock((BlockState) tile.getHeldBlock().m_61122_(LIT));
                    }
                }
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof WallLanternBlockTile tile ? tile.getHeldBlock().m_287290_(builder) : super.m_49635_(state, builder);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof WallLanternBlockTile tile) {
            BlockState s = tile.getHeldBlock();
            s.m_60734_().animateTick(s, level, pos, random);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WallLanternBlockTile(pPos, pState);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.m_7892_(state, level, pos, entity);
        if (level.isClientSide && !(Boolean) ClientConfigs.FAST_LANTERNS.get() && level.getBlockEntity(pos) instanceof WallLanternBlockTile tile) {
            tile.getAnimation().hitByEntity(entity, state, pos);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, (BlockEntityType) ModRegistry.WALL_LANTERN_TILE.get(), pLevel.isClientSide ? SwayingBlockTile::clientTick : null);
    }

    public void placeOn(BlockState lantern, BlockPos onPos, Direction face, Level world) {
        BlockState state = (BlockState) getConnectedState(this.m_49966_(), world.getBlockState(onPos), world, onPos, face).m_61124_(FACING, face);
        BlockPos newPos = onPos.relative(face);
        world.setBlock(newPos, state, 3);
        if (world.getBlockEntity(newPos) instanceof IBlockHolder tile) {
            tile.setHeldBlock(lantern);
        }
    }

    public static boolean isValidBlock(Block b) {
        if (b.asItem() == Items.AIR) {
            return false;
        } else {
            ResourceLocation id = Utils.getID(b);
            String namespace = id.getNamespace();
            if (((List) CommonConfigs.WALL_LANTERN_WHITELIST.get()).contains(id.toString())) {
                return true;
            } else if (((List) CommonConfigs.WALL_LANTERN_BLACKLIST.get()).contains(namespace)) {
                return false;
            } else if (!namespace.equals("skinnedlanterns") && (!namespace.equals("twigs") || !id.getPath().contains("paper_lantern"))) {
                return !(b instanceof LanternBlock) ? false : !b.defaultBlockState().m_155947_() || CompatHandler.SUPPSQUARED && SuppSquaredCompat.isLightableLantern(b);
            } else {
                return true;
            }
        }
    }
}