package com.mna.blocks.artifice;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.blocks.tileentities.FluidJugTile;
import com.mna.items.ItemInit;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidJugBlock extends HorizontalDirectionalBlock implements EntityBlock, IDontCreateBlockItem, SimpleWaterloggedBlock {

    public static final ResourceLocation JUG = new ResourceLocation("jug");

    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    private final boolean is_infinite;

    private final ResourceLocation infinite_fluid_type;

    public FluidJugBlock(boolean is_infinite, ResourceLocation fluidType) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).noOcclusion().strength(2.0F));
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false));
        this.is_infinite = is_infinite;
        this.infinite_fluid_type = fluidType;
    }

    public FluidJugBlock() {
        this(false, null);
    }

    public final boolean is_infinite() {
        return this.is_infinite;
    }

    @Nullable
    public final ResourceLocation getFluidType() {
        return this.infinite_fluid_type;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidJugTile(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(f_54117_);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState base = (BlockState) this.m_49966_().m_61124_(f_54117_, ctx.m_8125_());
        BlockPos blockpos = ctx.getClickedPos();
        BlockState blockstate = ctx.m_43725_().getBlockState(blockpos);
        if (blockstate.m_60713_(this)) {
            return (BlockState) blockstate.m_61124_(BlockStateProperties.WATERLOGGED, false);
        } else {
            FluidState fluidstate = ctx.m_43725_().getFluidState(blockpos);
            return (BlockState) base.m_61124_(BlockStateProperties.WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return SimpleWaterloggedBlock.super.placeLiquid(worldIn, pos, state, fluidStateIn);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(worldIn, pos, state, fluidIn);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(BlockStateProperties.WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return stateIn;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        switch(type) {
            case LAND:
                return false;
            case WATER:
                return worldIn.getFluidState(pos).is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.m_21120_(hand);
        if (heldItem.isEmpty()) {
            return InteractionResult.FAIL;
        } else {
            LazyOptional<IFluidHandler> handler = FluidUtil.getFluidHandler(world, position, Direction.DOWN);
            if (handler.isPresent()) {
                if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                    FluidStack blockFluidStack = ((IFluidHandler) handler.resolve().get()).getFluidInTank(0);
                    int amt = blockFluidStack.getAmount();
                    if (amt >= 100) {
                        BlockEntity be = world.getBlockEntity(position);
                        if (be != null && be instanceof FluidJugTile fluidTile) {
                            fluidTile.drain(100, IFluidHandler.FluidAction.EXECUTE);
                            player.m_21008_(hand, ItemUtils.createFilledResult(heldItem, player, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                            player.m_6674_(hand);
                        }
                    }
                } else if (FluidUtil.interactWithFluidHandler(player, hand, (IFluidHandler) handler.resolve().get())) {
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
            return InteractionResult.FAIL;
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return true;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        if (!this.is_infinite && stack.hasTag()) {
            FluidStack flStack = ItemInit.FLUID_JUG.get().getFluidTagData(stack);
            if (!flStack.isEmpty()) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be != null && be instanceof FluidJugTile fluidTile) {
                    fluidTile.fill(flStack, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (!this.is_infinite) {
            BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (blockentity instanceof FluidJugTile jug) {
                FluidStack jugFluid = jug.getFluidInTank(0);
                if (!jugFluid.isEmpty()) {
                    ItemStack jugStack = new ItemStack(ItemInit.FLUID_JUG.get());
                    FluidUtil.getFluidHandler(jugStack).ifPresent(f -> f.fill(jugFluid, IFluidHandler.FluidAction.EXECUTE));
                    return Arrays.asList(jugStack);
                }
            }
        }
        return super.m_49635_(state, builder);
    }
}