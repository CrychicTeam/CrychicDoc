package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import net.mehvahdjukaar.moonlight.api.block.ISoftFluidConsumer;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PancakeBlock extends WaterBlock implements ISoftFluidConsumer {

    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[8];

    public static final IntegerProperty PANCAKES = ModBlockProperties.PANCAKES_1_8;

    public static final EnumProperty<ModBlockProperties.Topping> TOPPING = ModBlockProperties.TOPPING;

    public PancakeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(PANCAKES, 1)).m_61124_(TOPPING, ModBlockProperties.Topping.NONE)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack stack = player.m_21120_(handIn);
        Item item = stack.getItem();
        Pair<ModBlockProperties.Topping, Item> found = ModBlockProperties.Topping.fromItem(stack);
        ModBlockProperties.Topping t = (ModBlockProperties.Topping) found.getFirst();
        if (t != ModBlockProperties.Topping.NONE) {
            if (state.m_61143_(TOPPING) == ModBlockProperties.Topping.NONE) {
                if (!worldIn.isClientSide) {
                    worldIn.setBlock(pos, (BlockState) state.m_61124_(TOPPING, t), 3);
                    worldIn.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                Item empty = (Item) found.getSecond();
                ItemStack returnItem = empty == null ? ItemStack.EMPTY : empty.getDefaultInstance();
                if (!player.isCreative()) {
                    Utils.swapItem(player, handIn, returnItem);
                }
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
        } else {
            if (item == this.m_5456_()) {
                return InteractionResult.PASS;
            }
            if (player.canEat(false)) {
                player.getFoodData().eat(1, 0.1F);
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                if (!worldIn.isClientSide) {
                    this.removeLayer(state, pos, worldIn, player);
                    return InteractionResult.CONSUME;
                }
                Minecraft.getInstance().particleEngine.destroy(player.m_20183_().above(1), state);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private void removeLayer(BlockState state, BlockPos pos, Level world, Player player) {
        int i = (Integer) state.m_61143_(PANCAKES);
        if (i == 8) {
            BlockPos up = pos.above();
            BlockState upState = world.getBlockState(up);
            if (upState.m_60734_() == state.m_60734_()) {
                this.removeLayer(upState, up, world, player);
                return;
            }
        }
        if (i > 1) {
            world.setBlock(pos, (BlockState) state.m_61124_(PANCAKES, i - 1), 3);
        } else {
            world.removeBlock(pos, false);
        }
        if (state.m_61143_(TOPPING) != ModBlockProperties.Topping.NONE) {
            player.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockstate.m_60713_(this)) {
            return (BlockState) blockstate.m_61124_(PANCAKES, Math.min(8, (Integer) blockstate.m_61143_(PANCAKES) + 1));
        } else {
            boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
            return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag);
        }
    }

    protected boolean isValidGround(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return !state.m_60812_(worldIn, pos).getFaceShape(Direction.UP).isEmpty() || state.m_60783_(worldIn, pos, Direction.UP);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.m_8055_(blockpos), worldIn, blockpos);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        return !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.m_43722_().getItem() == this.m_5456_() && (Integer) state.m_61143_(PANCAKES) < 8 || super.m_6864_(state, useContext);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[state.m_61143_(PANCAKES) - 1];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PANCAKES, TOPPING, WATERLOGGED);
    }

    @Override
    public boolean tryAcceptingFluid(Level world, BlockState state, BlockPos pos, SoftFluidStack fluid) {
        ModBlockProperties.Topping topping = ModBlockProperties.Topping.fromFluid(fluid.fluid());
        if (state.m_61143_(TOPPING) == ModBlockProperties.Topping.NONE && topping != ModBlockProperties.Topping.NONE) {
            world.setBlock(pos, (BlockState) state.m_61124_(TOPPING, topping), 2);
            world.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
            return true;
        } else {
            return false;
        }
    }

    static {
        Arrays.setAll(SHAPE_BY_LAYER, l -> Block.box(2.0, 0.0, 2.0, 14.0, 2.0 + (double) (l * 2), 14.0));
    }
}