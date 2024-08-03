package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.item.PrimordialArmorItem;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DinosaurChopBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public final Map<BlockState, VoxelShape> shapeMap = new HashMap();

    private final int foodAmount;

    private final float saturationAmount;

    public DinosaurChopBlock(int foodAmount, float saturationAmount) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(1.0F, 1.0F).sound(SoundType.CANDLE).noOcclusion().dynamicShape().randomTicks());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.UP)).m_61124_(BITES, 0)).m_61124_(WATERLOGGED, false));
        this.foodAmount = foodAmount;
        this.saturationAmount = saturationAmount;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.getChopShape(state);
    }

    protected VoxelShape getChopShape(BlockState state) {
        if (this.shapeMap.containsKey(state)) {
            return (VoxelShape) this.shapeMap.get(state);
        } else {
            int bites = (Integer) state.m_61143_(BITES);
            VoxelShape shape;
            if (bites == 0) {
                shape = Shapes.block();
            } else {
                Direction facing = (Direction) state.m_61143_(FACING);
                VoxelShape merge = ThinBoneBlock.SHAPE_Y;
                switch(facing.getAxis()) {
                    case X:
                        merge = ThinBoneBlock.SHAPE_X;
                        break;
                    case Y:
                        merge = ThinBoneBlock.SHAPE_Y;
                        break;
                    case Z:
                        merge = ThinBoneBlock.SHAPE_Z;
                }
                shape = Shapes.join(merge, calculateShapeForRotation(facing, bites), BooleanOp.OR);
            }
            this.shapeMap.put(state, shape);
            return shape;
        }
    }

    private static VoxelShape calculateShapeForRotation(Direction facing, int bites) {
        float minHeight = (float) (4 * bites);
        float height = 16.0F - minHeight;
        switch(facing) {
            case UP:
                return Block.box(0.0, 0.0, 0.0, 16.0, (double) height, 16.0);
            case DOWN:
                return Block.box(0.0, (double) minHeight, 0.0, 16.0, 16.0, 16.0);
            case NORTH:
                return Block.box(0.0, 0.0, (double) minHeight, 16.0, 16.0, 16.0);
            case SOUTH:
                return Block.box(0.0, 0.0, 0.0, 16.0, 16.0, (double) height);
            case EAST:
                return Block.box(0.0, 0.0, 0.0, (double) height, 16.0, 16.0);
            case WEST:
                return Block.box((double) minHeight, 0.0, 0.0, 16.0, 16.0, 16.0);
            default:
                return Shapes.empty();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED, BITES, FACING);
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        ItemStack itemstack = player.m_21120_(hand);
        if (level.isClientSide) {
            if (this.eat(level, blockPos, blockState, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }
        return this.eat(level, blockPos, blockState, player);
    }

    protected InteractionResult eat(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            int extraShanksFromArmor = this == ACBlockRegistry.DINOSAUR_CHOP.get() ? PrimordialArmorItem.getExtraSaturationFromArmor(player) : 0;
            player.getFoodData().eat(this.foodAmount + extraShanksFromArmor, this.saturationAmount + (float) extraShanksFromArmor * 0.125F);
            int i = (Integer) blockState.m_61143_(BITES);
            levelAccessor.gameEvent(player, GameEvent.EAT, blockPos);
            if (i < 3) {
                levelAccessor.m_7731_(blockPos, (BlockState) blockState.m_61124_(BITES, i + 1), 3);
            } else {
                levelAccessor.m_7471_(blockPos, false);
                levelAccessor.m_7731_(blockPos, (BlockState) ACBlockRegistry.THIN_BONE.get().defaultBlockState().m_61124_(ThinBoneBlock.f_55923_, ((Direction) blockState.m_61143_(FACING)).getAxis()), 4);
                levelAccessor.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void randomTick(BlockState currentState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(4) == 0 && this == ACBlockRegistry.DINOSAUR_CHOP.get() && this.isFireBelow(level, blockPos.below())) {
            BlockState blockstate1 = (BlockState) ((BlockState) ACBlockRegistry.COOKED_DINOSAUR_CHOP.get().defaultBlockState().m_61124_(FACING, (Direction) currentState.m_61143_(FACING))).m_61124_(BITES, (Integer) currentState.m_61143_(BITES));
            level.m_46597_(blockPos, blockstate1);
        }
    }

    private boolean isFireBelow(Level level, BlockPos pos) {
        while (level.getBlockState(pos).m_60795_() && pos.m_123342_() > level.m_141937_()) {
            pos = pos.below();
        }
        BlockState fireState = level.getBlockState(pos);
        return fireState.m_204336_(ACTagRegistry.COOKS_MEAT_BLOCKS);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return getOutputSignal((Integer) blockState.m_61143_(BITES));
    }

    public static int getOutputSignal(int i) {
        return (7 - i) * 2;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos pos, BlockState blockState, Fluid fluid) {
        return (Integer) blockState.m_61143_(BITES) != 0 && fluid == Fluids.WATER;
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if ((Integer) blockState.m_61143_(BITES) != 0 && (Boolean) blockState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            levelAccessor.m_7731_(blockPos, (BlockState) blockState.m_61124_(BlockStateProperties.WATERLOGGED, false), 3);
            if (!blockState.m_60710_(levelAccessor, blockPos)) {
                levelAccessor.m_46961_(blockPos, true);
            }
            return new ItemStack(Items.WATER_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }
}