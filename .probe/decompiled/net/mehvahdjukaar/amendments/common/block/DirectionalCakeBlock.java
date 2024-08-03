package net.mehvahdjukaar.amendments.common.block;

import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.FarmersDelightCompat;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DirectionalCakeBlock extends CakeBlock implements SimpleWaterloggedBlock {

    protected static final VoxelShape[] SHAPES_NORTH = new VoxelShape[] { Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 3.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 5.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 7.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 9.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 11.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 13.0, 15.0, 8.0, 15.0) };

    protected static final VoxelShape[] SHAPES_SOUTH = new VoxelShape[] { Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 13.0), Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 11.0), Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 9.0), Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 7.0), Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 5.0), Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 3.0) };

    protected static final VoxelShape[] SHAPES_EAST = new VoxelShape[] { Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0), Block.box(1.0, 0.0, 1.0, 13.0, 8.0, 15.0), Block.box(1.0, 0.0, 1.0, 11.0, 8.0, 15.0), Block.box(1.0, 0.0, 1.0, 9.0, 8.0, 15.0), Block.box(1.0, 0.0, 1.0, 7.0, 8.0, 15.0), Block.box(1.0, 0.0, 1.0, 5.0, 8.0, 15.0), Block.box(1.0, 0.0, 1.0, 3.0, 8.0, 15.0) };

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public final CakeRegistry.CakeType type;

    public DirectionalCakeBlock(CakeRegistry.CakeType type) {
        super(Utils.copyPropertySafe(type.cake).dropsLike(type.cake));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_51180_, 0)).m_61124_(FACING, Direction.WEST)).m_61124_(WATERLOGGED, false));
        this.type = type;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return this.useGeneric(state, level, pos, player, handIn, hit, true);
    }

    protected InteractionResult useGeneric(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, boolean canEat) {
        ItemStack itemstack = player.m_21120_(handIn);
        Item item = itemstack.getItem();
        if (CompatHandler.FARMERS_DELIGHT && this.type == CakeRegistry.VANILLA) {
            InteractionResult res = FarmersDelightCompat.onCakeInteract(state, pos, level, itemstack);
            if (res.consumesAction()) {
                return res;
            }
        }
        if (itemstack.is(ItemTags.CANDLES) && (Integer) state.m_61143_(f_51180_) == 0 && state.m_60713_((Block) ModRegistry.DIRECTIONAL_CAKE.get())) {
            Block block = Block.byItem(item);
            if (block instanceof CandleBlock) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(pos, CandleCakeBlock.byCandle(block));
                level.m_142346_(player, GameEvent.BLOCK_CHANGE, pos);
                player.awardStat(Stats.ITEM_USED.get(item));
                return InteractionResult.SUCCESS;
            }
        }
        return !canEat ? InteractionResult.PASS : this.eatSliceD(level, pos, state, player, getHitDir(player, hit));
    }

    public static Direction getHitDir(Player player, BlockHitResult hit) {
        return hit.getDirection().getAxis() != Direction.Axis.Y ? hit.getDirection() : player.m_6350_().getOpposite();
    }

    public InteractionResult eatSliceD(LevelAccessor world, BlockPos pos, BlockState state, Player player, Direction dir) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(2, 0.1F);
            if (!world.m_5776_()) {
                this.removeSlice(state, pos, world, dir);
            }
            return InteractionResult.sidedSuccess(world.m_5776_());
        }
    }

    public void removeSlice(BlockState state, BlockPos pos, LevelAccessor world, Direction dir) {
        int i = (Integer) state.m_61143_(f_51180_);
        if (i < 6) {
            if (i == 0 && (Boolean) CommonConfigs.DIRECTIONAL_CAKE.get()) {
                state = (BlockState) state.m_61124_(FACING, dir);
            }
            world.m_7731_(pos, (BlockState) state.m_61124_(f_51180_, i + 1), 3);
        } else {
            world.m_7471_(pos, false);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.CAKE);
    }

    @Override
    public MutableComponent getName() {
        return Blocks.CAKE.getName();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case EAST ->
                SHAPES_EAST[state.m_61143_(f_51180_)];
            case SOUTH ->
                SHAPES_SOUTH[state.m_61143_(f_51180_)];
            case NORTH ->
                SHAPES_NORTH[state.m_61143_(f_51180_)];
            default ->
                f_51181_[state.m_61143_(f_51180_)];
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
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
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if (CompatHandler.SUPPLEMENTARIES) {
            SuppCompat.spawnCakeParticles(level, pos, rand);
        }
    }
}